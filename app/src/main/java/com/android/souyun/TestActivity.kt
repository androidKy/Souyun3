package com.android.souyun

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.safframework.log.L
import com.task.cn.ProxyConstant
import com.task.cn.Result
import com.task.cn.StatusTask
import com.task.cn.device.DeviceInfoController
import com.task.cn.jbean.DeviceInfoBean
import com.task.cn.jbean.IpInfoBean
import com.task.cn.jbean.VerifyIpBean
import com.task.cn.manager.LocationListener
import com.task.cn.manager.LocationManager
import com.task.cn.manager.TaskManager
import com.task.cn.proxy.IpListener
import com.task.cn.proxy.PingManager
import com.task.cn.proxy.ProxyManager
import com.task.cn.proxy.ProxyRequestListener
import com.task.cn.task.ITaskControllerView
import com.utils.common.CMDUtil
import com.utils.common.DevicesUtil
import com.utils.common.ToastUtils
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.concurrent.thread

class TestActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        L.init("proxy_test")

        /* Thread {
             PingManager.verifyIP("广州市", object : IpListener {
                 override fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?) {
                     L.d("thread name : ${Thread.currentThread().name}")
                     ToastUtils.showToast(this@TestActivity, "ip校验结果: $result")
                 }
             })
         }.start()*/

        bt_proxy.setOnClickListener(this)
        bt_task.setOnClickListener(this)
        bt_location.setOnClickListener(this)
        bt_ip.setOnClickListener(this)


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

       // DeviceInfoController().addDeviceInfo("com.tencent.mm", DeviceInfoBean())
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
                    com.safframework.log.L.d("代理： $result")
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
                override fun onTaskPrepared(result: Result<Boolean>) {
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
