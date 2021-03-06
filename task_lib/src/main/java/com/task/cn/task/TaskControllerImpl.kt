package com.task.cn.task

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.Result
import com.task.cn.StatusCode
import com.task.cn.StatusMsg
import com.task.cn.StatusTask
import com.task.cn.database.RealmHelper
import com.task.cn.jbean.AccountInfoBean
import com.task.cn.jbean.DeviceInfoBean
import com.task.cn.jbean.IpInfoBean
import com.task.cn.jbean.TaskBean
import com.task.cn.manager.TaskManager
import com.utils.common.ToastUtils
import com.utils.common.Utils

/**
 * Description:
 * Created by Quinin on 2020-03-04.
 **/
class TaskControllerImpl(private val taskControllerView: ITaskControllerView) : ITaskController,
    TaskInfoView {


    companion object {
        const val MSG_TASK_COUNT: Int = 1000
    }

    private var mPlatformList: List<String>? = null

    private var mTaskStatus: StatusTask = StatusTask.TASK_UNSTART   //任务状态
    @Volatile
    private var mTaskStartCount: Int = 0    //已开始的任务数量
    @Volatile
    private var mTaskErrorCount: Int = 0  //错误的任务数量
    @Volatile
    private var mTaskFinished: Boolean = false  //切换任务是否完成

    private var mIpSwitch = false
    private var mTaskBuilder: TaskManager.Companion.TaskBuilder? = null

    private var mErrorStringBuilder: StringBuilder = StringBuilder()

    private var mTaskBean: TaskBean = TaskBean()
    private var mRealmHelper: RealmHelper? = null

    private var mTaskExecutor: TaskInfoImpl? = null

    private val mHandler: Handler = Handler(Looper.getMainLooper()) {
        if (it.what == MSG_TASK_COUNT) {
            if (mTaskStartCount <= 0 && !mTaskFinished) { //已开始的任务完成
                dealTask()
            } else {
                sendTaskMsg(500)
            }
        }

        false
    }

    @Synchronized
    private fun dealTask() {
        val jsonTaskBean = Gson().toJson(mTaskBean, TaskBean::class.java)
        L.d("task finished: $jsonTaskBean")
        updateTaskToRealm(mTaskBean)
        if (mTaskErrorCount == 0) {
            mTaskFinished = true

            mHandler.postDelayed({
                mTaskExecutor?.getLocationByIP("")
            }, 2000)
        } else {
            mTaskFinished = true
            val errorMsg = mErrorStringBuilder.toString()
            L.d("任务出现错误: $errorMsg")
            taskControllerView.onTaskPrepared(Result(StatusCode.FAILED, mTaskBean, errorMsg))
        }
    }

    override fun startTask(taskBuilder: TaskManager.Companion.TaskBuilder) {
        /**
         * 上次的任务未执行完成
         */
        if (taskBuilder.getLastTaskStatus() == StatusTask.TASK_RUNNING) {
            //ToastUtils.showToast("上次的任务未执行完成")
            taskControllerView.onTaskPrepared(Result(StatusCode.FAILED, mTaskBean, "上次的任务未执行完成"))
            return
        }
        /**
         * 任务正在执行
         */
        if (mTaskStatus == StatusTask.TASK_RUNNING) {
            taskControllerView.onTaskPrepared(Result(StatusCode.FAILED, mTaskBean, "任务正在执行"))
            ToastUtils.showToast(Utils.getApp(), "任务正在执行")
            return
        }
        /*if(taskBuilder.getCityName().isNullOrEmpty())
        {
            taskControllerView.onTaskPrepared(Result(StatusCode.FAILED,false,"未指定城市名"))
            return
        }*/
        mRealmHelper = RealmHelper()

        setTaskStatus(StatusTask.TASK_RUNNING)

        mTaskStartCount = 0
        mTaskErrorCount = 0
        mTaskFinished = false
        mPlatformList = taskBuilder.getPlatformList()

        mTaskExecutor = TaskInfoImpl(this)

        mIpSwitch = taskBuilder.getIpSwitch()
        if (mIpSwitch) {
            mTaskBuilder = taskBuilder
            mTaskStartCount++
            mTaskExecutor?.getIpInfo(taskBuilder.getCityCode(), taskBuilder.getCityName())
            return
        }

        getDataAfterIpChanged(taskBuilder)
    }

    private fun getDataAfterIpChanged(taskBuilder: TaskManager.Companion.TaskBuilder) {
        if (taskBuilder.getTaskInfoSwitch()) {
            mTaskStartCount++
            if (taskBuilder.getTaskBean() == null)
                mTaskExecutor?.getTaskInfo()
            else mTaskExecutor?.getTaskInfo(taskBuilder.getTaskBean()!!)
        }

        if (taskBuilder.getAccountSwitch()) {
            mTaskStartCount++
            mTaskExecutor?.getAccountInfo()
        }

        if (taskBuilder.getDeviceSwitch()) {
            mTaskStartCount++
            mTaskExecutor?.getDeviceInfo(taskBuilder.getPlatformList())
        }
    }

    override fun onResponTaskInfo(result: Result<TaskBean>) {
        L.d("任务信息获取结果：code=${result.code} ${result.r}")
        if (result.code == StatusCode.FAILED) {
            dealError(result.msg)
        } else
            mTaskBean = result.r
        sendTaskResult()

        showToast("获取任务：${result.code}")
    }

    override fun onResponIpInfo(result: Result<IpInfoBean>) {
        L.d("IP切换结果：code=${result.code} ${result.r}")
        if (result.code == StatusCode.FAILED) {
            dealError(result.msg)
        } else
            mTaskBean.ip_info = result.r
        sendTaskResult()

        showToast("更换IP:${result.code}")

        if (mIpSwitch) {
            mTaskBuilder?.also {
                getDataAfterIpChanged(it)
            }
        }
    }

    override fun onResponDeviceInfo(result: Result<DeviceInfoBean>) {
        L.d("设备信息获取结果：code=${result.code} ${result.r}")
        if (result.code == StatusCode.FAILED) {
            dealError(result.msg)
        } else
            mTaskBean.device_info = result.r
        sendTaskResult()

        showToast("获取设备信息:${result.code}")
    }

    override fun onResponAccountInfo(result: Result<AccountInfoBean>) {
        L.d("账号信息获取结果: code=${result.code} ${result.r}")
        if (result.code == StatusCode.FAILED) {
            dealError(result.msg)
        } else
            mTaskBean.account_info = result.r
        sendTaskResult()

        showToast("获取账号:${result.code}")
    }

    override fun onResponIPAddress(latitude: String, longitude: String) {
        L.d("根据IP获取的经纬度: latitude=$latitude longitude=$longitude")

        showToast("获取经纬度:$latitude,$longitude")

        if (latitude.isEmpty() || latitude == "0.0") {
            dealError("获取经纬度失败")
            taskControllerView.onTaskPrepared(Result(StatusCode.FAILED, mTaskBean, "获取经纬度失败"))
            return
        }

        mTaskBean.device_info.latitude = latitude
        mTaskBean.device_info.longitude = longitude

        mTaskExecutor?.changeDeviceInfo(mTaskBean, mPlatformList)
    }

    override fun onChangeDeviceInfo(result: Result<Boolean>) {
        showToast("更改设备信息:${result.code}")

        val finalResult = Result<TaskBean>(StatusCode.FAILED, mTaskBean, result.msg)

        if (result.code == StatusCode.FAILED) {
            setTaskStatus(StatusTask.TASK_EXCEPTION)
        } else {
            finalResult.code = StatusCode.SUCCEED
            finalResult.msg = StatusMsg.SUCCEED.msg
            setTaskStatus(StatusTask.TASK_FINISHED)
        }

        taskControllerView.onTaskPrepared(finalResult)

        updateTaskToRealm(mTaskBean)
    }

    override fun destroy() {
        mRealmHelper?.closeRealm()
    }

    @Synchronized
    private fun sendTaskResult() {
        mTaskStartCount--
        sendTaskMsg(0)
    }


    private fun sendTaskMsg(delayTime: Long) {
        mHandler.sendEmptyMessageDelayed(MSG_TASK_COUNT, delayTime)
    }

    private fun dealError(msg: String) {
        if (mErrorStringBuilder.isEmpty())
            mErrorStringBuilder.append(msg)
        else mErrorStringBuilder.append("|$msg")

        mTaskErrorCount++
        setTaskStatus(StatusTask.TASK_EXCEPTION)
    }

    private fun setTaskStatus(statusTask: StatusTask) {
        mTaskStatus = statusTask
        mTaskBean.task_status = statusTask.taskStatus
    }

    private fun updateTaskToRealm(taskBean: TaskBean) {
        mRealmHelper?.insertTask(taskBean)
    }

    private fun showToast(msg: String) {
        Handler(Looper.getMainLooper()).post {
            ToastUtils.showToast(Utils.getApp(), msg)
        }

    }

}
