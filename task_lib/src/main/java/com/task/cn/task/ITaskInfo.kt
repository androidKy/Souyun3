package com.task.cn.task

import com.task.cn.jbean.TaskBean


/**
 * Description:
 * Created by Quinin on 2020-03-03.
 **/
interface ITaskInfo {
    fun getTaskInfo()
    fun getTaskInfo(taskBean: TaskBean)
    fun getAccountInfo()
    fun getIpInfo(cityCode:String,cityName:String)
    fun getDeviceInfo(platformList:List<String>)

    fun getLocationByIP(ip:String)

    fun changeDeviceInfo(taskBean: TaskBean,platformList: List<String>?)
}