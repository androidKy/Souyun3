package com.android.souyun

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.Result
import com.task.cn.StatusTask
import com.task.cn.device.DeviceInfoController
import com.task.cn.jbean.*
import com.task.cn.manager.LocationListener
import com.task.cn.manager.LocationManager
import com.task.cn.manager.TaskManager
import com.task.cn.proxy.IpListener
import com.task.cn.proxy.PingManager
import com.task.cn.proxy.ProxyManager
import com.task.cn.proxy.ProxyRequestListener
import com.task.cn.task.ITaskControllerView
import com.utils.common.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.concurrent.thread

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private var mPkgName: String = ""
    private var mTaskBean: TaskBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        L.init("proxy_test")
        initListener()

        //L.d("ip: ${DevicesUtil.getIPAddress(this.applicationContext)}")
        val localIp = DevicesUtil.getIPAddress(this.applicationContext)
        val proxyUrl = "${ProxyConstant.IP_URL}440900"
        AndroidNetworking.get(proxyUrl)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    tv_tip.text = response
                }

                override fun onError(anError: ANError?) {
                    tv_tip.text = "代理请求失败"
                }
            })

        Thread(Runnable {
            CMDUtil().execCmd("cd /data/")
        }).start()

        mTaskBean = TaskBean()


        mPkgName = "com.tencent.mm"
        bt_wechat.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
        bt_douyin.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        // DeviceInfoController().addDeviceInfo("com.tencent.mm", DeviceInfoBean())
    }

    private fun initListener() {
        bt_proxy.setOnClickListener(this)
        bt_task.setOnClickListener(this)
        bt_location.setOnClickListener(this)
        bt_ip.setOnClickListener(this)
        bt_choose_device.setOnClickListener(this)
        bt_wechat.setOnClickListener(this)
        bt_douyin.setOnClickListener(this)
        bt_one_key.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        val cityName = et_cityName.text.toString()
        when (v?.id!!) {
            R.id.bt_proxy -> {
                startProxy(cityName)
            }
            R.id.bt_task -> {
                startTask(cityName)
            }
            R.id.bt_location -> {
                startLocation()
            }
            R.id.bt_ip -> {
                getCurrentIp()
            }
            R.id.bt_wechat -> {
                bt_wechat.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                bt_douyin.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                mPkgName = "com.tencent.mm"

                Thread(Runnable {
                    CmdListUtil.getInstance().execCmd("pm clear $mPkgName")
                }).start()
            }
            R.id.bt_douyin -> {
                bt_wechat.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                bt_douyin.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                mPkgName = "com.ss.android.ugc.aweme"

                Thread(Runnable {
                    CmdListUtil.getInstance().execCmd("pm clear $mPkgName")
                }).start()
            }
            R.id.bt_one_key -> {
                oneKey(cityName)
            }
            R.id.bt_choose_device -> {
                startActivityForResult(Intent(this, DeviceInfoActivity::class.java), 2000)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            data?.apply {
                val device_info = this.extras?.get("device_info") as DeviceInfoBean
                L.d("接收的Device_info: $device_info")
                mTaskBean?.device_info = device_info

                tv_device_key.text = this.extras?.getString("device_key")
            }
        }
    }

    private fun oneKey(cityName: String) {
        if (mTaskBean != null) {
            if(cityName.isEmpty())
            {
                Toast.makeText(this, "未选择城市", Toast.LENGTH_SHORT).show()
                return
            }

            if (mTaskBean?.device_info == null) {
                Toast.makeText(this, "未选择设备信息", Toast.LENGTH_SHORT).show()
                return
            }

            val backup_info = BackupInfoBean()
            backup_info.pkg_name = mPkgName
            mTaskBean?.backup_info = backup_info

            L.d("传入的TaskBean: $mTaskBean")

            TaskManager.Companion.TaskBuilder()
                .setLastTaskStatus(StatusTask.TASK_FINISHED)
                .setAccountSwitch(false)
                .setDeviceSwitch(false)
                .setIpSwitch(true)
                .setTaskInfoSwitch(true)
                .setCityName(cityName)
                .setTaskBean(mTaskBean!!)
                .setTaskControllerView(object : ITaskControllerView {
                    override fun onTaskPrepared(result: Result<TaskBean>) {
                        //L.d("task end: $result")
                        com.safframework.log.L.d("任务： $result")
                        tv_tip.text = result.msg
                        ToastUtils.showToast(this@TestActivity, "任务: $result")
                    }
                })
                .build()
                .startTask()
        }
    }

    private fun getCurrentIp() {
        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?) {
                if (!result)
                    tv_tip.text = "查询IP失败"
                verifyIpBean?.apply {
                    tv_ip.text = this.cip
                    tv_address.text = this.cname
                }
            }
        })
    }

    private fun startProxy(cityName: String) {
        ProxyManager()
            .setCityName(cityName)
            .setProxyRequestListener(object : ProxyRequestListener {
                override fun onProxyResult(result: Result<IpInfoBean>) {
                    L.d("代理： $result")
                    ToastUtils.showToast(this@TestActivity, "代理: $result")
                    tv_tip.text = result.msg
                    tv_ip.text = result.r.ip
                    tv_address.text = result.r.city
                }
            })
            .startProxy()
    }

    private fun startLocation() {
        val sha1 = DevicesUtil.getSHA1(this)
        L.d("sha1: $sha1")

        LocationManager()
            .setLocationListener(object : LocationListener {
                override fun onLocationResult(latitude: String, longitude: String) {
                    tv_location.text = "$latitude,$longitude"
                }
            })
            .startLocation("")
    }

    private fun startTask(cityName: String) {
        TaskManager.Companion.TaskBuilder()
            .setLastTaskStatus(StatusTask.TASK_FINISHED)
            .setAccountSwitch(false)
            .setDeviceSwitch(false)
            .setIpSwitch(false)
            .setTaskInfoSwitch(true)
            .setCityName(cityName)
            .setTaskControllerView(object : ITaskControllerView {
                override fun onTaskPrepared(result: Result<TaskBean>) {
                    //L.d("task end: $result")
                    com.safframework.log.L.d("任务： $result")
                    tv_tip.text = result.msg
                    ToastUtils.showToast(this@TestActivity, "任务: $result")
                }
            })
            .build()
            .startTask()
    }
}
