package com.account.manager.ui.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.account.manager.base.BaseViewModel
import com.task.cn.Result
import com.task.cn.StatusCode
import com.task.cn.jbean.TaskBean
import com.task.cn.jbean.VerifyIpBean
import com.task.cn.manager.TaskManager
import com.task.cn.proxy.IpListener
import com.task.cn.proxy.PingManager
import com.task.cn.task.ITaskControllerView
import com.task.cn.util.AppUtils
import com.utils.common.DevicesUtil
import com.utils.common.Utils

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
    private val _appList = MutableLiveData<String>()
    val appList: LiveData<String> = _appList


    fun initPhoneInfo(){
        settingPhoneInfo()
        settingIPInfo()
    }

    /**
     * 一键改机
     */
    fun setupPhone() {
        TaskManager.Companion.TaskBuilder()
            .setIpSwitch(false)     //todo 测试IP
            .setDeviceSwitch(true)
            .setPlatformList(arrayListOf(-1))
            .setTaskControllerView(object : ITaskControllerView {
                override fun onTaskPrepared(result: Result<TaskBean>) {
                    if (result.code == StatusCode.SUCCEED) {
                        settingPhoneInfo()
                        settingIPInfo()
                    }

                    _tipSetup.value = result.msg
                }
            })
            .build()
            .startTask()
    }

    /**
     * 设置手机信息
     */
    private fun settingPhoneInfo() {
        _phoneModel.value = "${DevicesUtil.getNAME()}-${DevicesUtil.getModel()}"
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


}