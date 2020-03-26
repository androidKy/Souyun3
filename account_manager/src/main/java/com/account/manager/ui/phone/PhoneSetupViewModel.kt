package com.account.manager.ui.phone

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.account.manager.base.BaseViewModel
import com.account.manager.view.AppPicker
import com.account.manager.view.CityPicker
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.safframework.log.L
import com.task.cn.Result
import com.task.cn.StatusCode
import com.task.cn.jbean.AppInfo
import com.task.cn.jbean.TaskBean
import com.task.cn.jbean.VerifyIpBean
import com.task.cn.manager.LocationListener
import com.task.cn.manager.LocationManager
import com.task.cn.manager.TaskManager
import com.task.cn.proxy.IpListener
import com.task.cn.proxy.PingManager
import com.task.cn.task.ITaskControllerView
import com.task.cn.util.AppUtils
import com.utils.common.DevicesUtil
import com.utils.common.ToastUtils
import com.utils.common.Utils
import java.lang.StringBuilder

class PhoneSetupViewModel : BaseViewModel() {

    private val _tipSetup = MutableLiveData<String>()
    val tipSetup: LiveData<String> = _tipSetup
    private val _phoneModel = MutableLiveData<String>()
    val phoneModel: LiveData<String> = _phoneModel
    private val _phoneImei = MutableLiveData<String>()
    val phoneImei: LiveData<String> = _phoneImei
    private val _ip = MutableLiveData<String>()
    val ip: LiveData<String> = _ip
    private val _ipCity = MutableLiveData<String>()
    val ipCity: LiveData<String> = _ipCity

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location
    private val _appList = MutableLiveData<String>()
    val appList: LiveData<String> = _appList

    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> = _cityName

    private val _cityCode = MutableLiveData<String>()
    private val cityCode: LiveData<String> = _cityCode
    private val _pkgList = MutableLiveData<ArrayList<String>>()
    private val pkgList: LiveData<ArrayList<String>> = _pkgList

    private val mAppInfoList: ArrayList<AppInfo> = arrayListOf()

    private var mIsStarting = false


    fun initPhoneInfo() {
        settingPhoneInfo()
        settingIPInfo()
        initLocation()
    }

    /**
     * 一键改机
     */
    fun setupPhone() {
        if(mIsStarting)
        {
            ToastUtils.showToast(Utils.getApp(),"正在改机...")
            return
        }

        mIsStarting = true
        _tipSetup.value = "开始改机..."
        TaskManager.Companion.TaskBuilder()
            .setIpSwitch(true)
            .setDeviceSwitch(true)
            .setTaskControllerView(object : ITaskControllerView {
                override fun onTaskPrepared(result: Result<TaskBean>) {
                    var msg = result.msg
                    if (result.code == StatusCode.SUCCEED) {
                        L.d("一键改机成功")
                        msg = "设备信息和IP已更改"
                        settingDeviceInfo(result.r)
                    }
                    _tipSetup.value = msg

                    settingIPInfo()
                    initLocation()

                    mIsStarting = false
                }
            })
            .build()
            .startTask()
    }

    private fun settingDeviceInfo(taskBean: TaskBean) {
        _phoneModel.value = taskBean.device_info.model
        _phoneImei.value = taskBean.device_info.android_id
    }

    /**
     * 批量改机
     */
    fun multiSetupPhone() {
        if(mIsStarting)
        {
            ToastUtils.showToast(Utils.getApp(),"正在改机...")
            return
        }

        val cityCode = cityCode.value ?: ""
        if (cityCode.isEmpty()) {
            ToastUtils.showToast(Utils.getApp(), "请选择IP地区")
            return
        }

        val pkgList = pkgList.value ?: arrayListOf<String>()
        if (pkgList.isEmpty()) {
            ToastUtils.showToast(Utils.getApp(), "请至少选择一个应用")
            return
        }
        _tipSetup.value = "开始改机..."
        mIsStarting = true

        TaskManager.Companion.TaskBuilder()
            .setIpSwitch(true)
            .setDeviceSwitch(true)
            .setPlatformList(pkgList)
            .setCityCode(cityCode)
            .setTaskControllerView(object : ITaskControllerView {
                override fun onTaskPrepared(result: Result<TaskBean>) {
                    var msg = result.msg
                    if (result.code == StatusCode.SUCCEED) {
                        L.d("批量改机成功")
                        msg = "设备信息和IP已更改"
                        settingDeviceInfo(result.r)
                    }
                    _tipSetup.value = msg

                    settingIPInfo()
                    initLocation()

                    mIsStarting = false
                }
            })
            .build()
            .startTask()
    }

    /**
     * 设置手机信息
     */
    private fun settingPhoneInfo() {
        _phoneModel.value = "${DevicesUtil.getManufacture()}-${DevicesUtil.getModel()}"
        _phoneImei.value = DevicesUtil.getIMEI(Utils.getApp())
    }

    /**
     * 设置IP信息
     */
    private fun settingIPInfo() {
        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?) {
                if (result) {
                    _ip.value = verifyIpBean?.cip
                    _ipCity.value = verifyIpBean?.cname
                } else {
                    _ip.value = ""
                    _ipCity.value = ""
                }
            }
        })
    }

    /**
     * 获取经纬度
     */
    private fun initLocation() {
        LocationManager()
            .setLocationListener(object : LocationListener {
                override fun onLocationResult(latitude: String, longitude: String) {
                    //taskInfoView.onResponIPAddress(latitude, longitude)
                    _location.value = "经度：$latitude 纬度：$longitude"
                }
            })
            .startLocation("")
    }

    fun chooseIp(context: Context) {
        CityPicker()
            .showCityPicker(context, object : OnCustomCityPickerItemClickListener() {
                override fun onSelected(
                    province: CustomCityData,
                    city: CustomCityData,
                    district: CustomCityData
                ) {
                    _cityCode.value = city.id
                    _cityName.value = "${province.name}${city.name}"
                }
            })
    }

    fun chooseApps(context: Context) {
        AppPicker(context)
            .setSelectedAppList(mAppInfoList)
            .setOnSelectedListener(object : AppPicker.OnSelectedListener {
                override fun onSelected(appInfo: AppInfo) {
                    L.d("已选择的app：${appInfo.toString()}")
                    mAppInfoList.add(appInfo)

                    _pkgList.value = ArrayList<String>().apply {
                        for (app in mAppInfoList) {
                            add(app.pkgName)
                        }
                    }

                    _appList.value = StringBuilder().apply {
                        for (app in mAppInfoList) {
                            append("${app.appName},")
                        }
                    }.toString()
                }
            })
            .showPicker()
    }

    fun clearChooseInfo() {
        mAppInfoList.clear()
        _pkgList.value = arrayListOf()
        _cityCode.value = ""
        _cityName.value = ""
    }

}