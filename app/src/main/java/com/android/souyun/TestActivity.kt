package com.android.souyun

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.safframework.log.L
import com.task.cn.Result
import com.task.cn.StatusCode
import com.task.cn.StatusTask
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


class TestActivity : AppCompatActivity(), View.OnClickListener {
    private var mPkgName: String = ""
    private var mCityCode: String = ""
    private lateinit var mTaskBean: TaskBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        L.init("proxy_test")
        initListener()

        //L.d("ip: ${DevicesUtil.getIPAddress(this.applicationContext)}")
        val localIp = DevicesUtil.getIPAddress(this.applicationContext)
        /* val proxyUrl = "${ProxyConstant.PROXY_IP_URL}440900"
         AndroidNetworking.get(proxyUrl)
             .build()
             .getAsString(object : StringRequestListener {
                 override fun onResponse(response: String?) {
                     tv_tip.text = response
                 }

                 override fun onError(anError: ANError?) {
                     tv_tip.text = "代理请求失败"
                 }
             })*/

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
        bt_choose_proxy.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        tv_tip.text = ""
        when (v?.id!!) {
            R.id.bt_proxy -> {
                startProxy(mCityCode)
            }
            R.id.bt_task -> {
                startTask(mCityCode)
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

            }
            R.id.bt_douyin -> {
                bt_wechat.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                bt_douyin.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                mPkgName = "com.ss.android.ugc.aweme"
            }
            R.id.bt_one_key -> {
                oneKey(mCityCode)
            }
            R.id.bt_choose_device -> {
                startActivityForResult(Intent(this, DeviceInfoActivity::class.java), 2000)
            }
            R.id.bt_choose_proxy -> {
                startActivityForResult(Intent(this, ProxyInfoActivity::class.java), 3000)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            data?.apply {
                val device_info = this.extras?.get("device_info") as DeviceInfoBean
                L.d("接收的Device_info: $device_info")
                mTaskBean.device_info = device_info

                val key = this.extras?.getString("device_key")

                tv_device_key.text = key
                AlertDialog.Builder(this@TestActivity)
                    .setTitle("一键改机")
                    .setMessage("是否一键改机？选择的设备信息是：$key")
                    .setNegativeButton("否") { dialog, which -> dialog?.dismiss() }
                    .setPositiveButton("是") { dialog, which ->
                        dialog?.dismiss()
                        oneKey(mCityCode)
                    }
                    .create()
                    .show()
            }
        } else if (requestCode == 3000) {
            val cityName = data?.extras?.getString("cityName")
            mCityCode = data?.extras?.getString("cityCode")!!
            bt_choose_proxy.text = "$cityName : $mCityCode"
        }
    }

    private fun oneKey(cityName: String) {
        tv_tip.text = "正在改机和切换IP..."
        if (cityName.isEmpty()) {
            Toast.makeText(this, "未设置代理IP", Toast.LENGTH_SHORT).show()
            tv_tip.text = "未设置代理IP"
            return
        }

        if (mTaskBean.device_info == null) {
            Toast.makeText(this, "未选择设备信息", Toast.LENGTH_SHORT).show()
            tv_tip.text = "未选择设备信息"
            return
        }

        val backup_info = BackupInfoBean()
        backup_info.pkg_name = mPkgName
        mTaskBean.backup_info = backup_info

        L.d("传入的TaskBean: $mTaskBean")

        TaskManager.Companion.TaskBuilder()
            .setLastTaskStatus(StatusTask.TASK_FINISHED)
            .setAccountSwitch(false)
            .setDeviceSwitch(false)
            .setIpSwitch(true)
            .setTaskInfoSwitch(true)
            .setPlatformList(arrayListOf(mPkgName))
            .setCityCode(cityName)
            .setTaskBean(mTaskBean)
            .setTaskControllerView(object : ITaskControllerView {
                override fun onTaskPrepared(result: Result<TaskBean>) {
                    //L.d("task end: $result")
                    L.d("任务： $result")
                    if (result.code == StatusCode.SUCCEED) {
                        tv_tip.text = "一键改机和更换IP成功"

                        tv_location.text =
                            "${result.r.device_info.latitude},${result.r.device_info.longitude}"
                       /* tv_ip.text = result.r.ip_info.ip
                        tv_address.text = result.r.ip_info.city*/

                        ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
                            override fun doInBackground(): Boolean {
                                CMDUtil().execCmd("pm clear $mPkgName")
                                return true
                            }

                            override fun onSuccess(result: Boolean?) {
                                val packageManager = this@TestActivity.getPackageManager()
                                val intent =
                                    packageManager.getLaunchIntentForPackage(mPkgName)
                                startActivity(intent)
                            }

                            override fun onCancel() {
                            }

                            override fun onFail(t: Throwable?) {
                            }
                        })
                    } else {
                        tv_tip.text = "改机失败，请检查网络情况"
                    }
                    ToastUtils.showToast(this@TestActivity, tv_tip.text.toString())
                }
            })
            .build()
            .startTask()
    }

    private fun getCurrentIp() {
        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?) {
                if (!result) {
                    tv_tip.text = "查询IP失败"
                    return
                }
                tv_tip.text = "查询IP成功"
                verifyIpBean?.apply {
                    tv_ip.text = this.cip
                    tv_address.text = this.cname
                }
            }
        })
    }

    private fun startProxy(cityName: String) {
        ProxyManager()
            .setCityCode(cityName)
            //.setCityName(cityName)
            .setProxyRequestListener(object : ProxyRequestListener {
                override fun onProxyResult(result: Result<IpInfoBean>) {
                    L.d("代理： $result")
                    ToastUtils.showToast(this@TestActivity, "代理: $result")
                    //tv_tip.text = result.msg
                    if (result.code == StatusCode.SUCCEED) {
                        tv_tip.text = "代理成功"
                        tv_ip.text = result.r.ip
                        tv_address.text = result.r.city
                    } else tv_tip.text = "代理失败"
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
                    if (latitude != "0.0")
                        tv_tip.text = "获取经纬度成功"

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
