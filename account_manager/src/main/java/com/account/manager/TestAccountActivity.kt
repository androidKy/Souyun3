package com.account.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.PkgConstant
import com.task.cn.device.DeviceInfoController
import com.task.cn.device.DeviceInfoListener
import com.task.cn.device.DeviceManager
import com.task.cn.device.MockDeviceController
import com.task.cn.manager.LocationListener
import com.task.cn.manager.LocationManager
import com.task.cn.model.DeviceModel
import kotlinx.android.synthetic.main.activity_test_account.*

class TestAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_account)
    }

    fun requestLocation(view: View) {
        tv_location.text = ""
        LocationManager()
            .setLocationListener(object : LocationListener {
                override fun onLocationResult(latitude: String, longitude: String) {
                    tv_location.text = "经度：$latitude 纬度：$longitude"
                }
            })
            .startLocation("茂名市")
    }

    fun requestDevice(view: View) {
        tv_device.text = ""
        DeviceManager().apply {
            setRequestListener(object : StringRequestListener {
                override fun onResponse(response: String) {
                    try {
                        val deviceModel = Gson().fromJson(response, DeviceModel::class.java)
                        setOnMockedListener(object : MockDeviceController.OnMockedListener {
                            override fun onMocked(status: Boolean, msg: String) {
                                tv_device.text = if (status) "修改设备信息成功" else msg
                            }
                        })
                        mockDevice(
                            arrayListOf(PkgConstant.PDD_PKG, PkgConstant.WECHAT_PKG),
                            deviceModel.data.deviceInfoBean
                        )
                    } catch (e: Exception) {
                    }
                }

                override fun onError(anError: ANError?) {

                }
            })
            getDeviceInfo(-1)
        }
    }
}
