package com.task.cn

import com.task.cn.PkgConstant.Companion.JD_PKG
import com.task.cn.PkgConstant.Companion.KUAISHOU_PKG
import com.task.cn.PkgConstant.Companion.PDD_PKG
import com.task.cn.PkgConstant.Companion.TIKTOK_PKG
import com.task.cn.PkgConstant.Companion.WECHAT_PKG

/**
 * Description:
 * Created by Quinin on 2020-03-03.
 **/
class URL {
    companion object {
        /**
         * 获取设备信息
         */
        const val GET_DEVICE_INFO_URL = "http://www.10gbuy.com/?s=App.User.GetDevice&platform="
        /**
         * 上传任务信息
         * 1、账号信息
         * 2、设备信息
         * 3、IP信息
         */
        const val UPLOAD_TASK_INFO_URL = ""
        /**
         * 获取任务信息
         */
        const val GET_TASK_INFO_URL = ""
    }
}

class PkgConstant {
    companion object {
        const val WECHAT_PKG = "com.tencent.mm"
        const val TIKTOK_PKG = "com.ss.android.ugc.aweme"
        const val KUAISHOU_PKG = "com.smile.gifmaker"   //快手
        const val JD_PKG = "com.jingdong.app.mall"  //京东的包名
        const val PDD_PKG = "com.xunmeng.pinduoduo" //拼多多
    }
}

class SPConstant {
    companion object {
        const val SP_DEVICE_INFO = "sp_device_info"
        const val SP_IP_INFO = "sp_ip_info"

        const val KEY_IP = "ip"
        const val KEY_CITY_CODE = "city_code"
        const val KEY_CITY_NAME = "city_name"
        const val KEY_DEVICE_ID = "device_id"
    }
}

fun getPlatformPkg(platform: Int): String {
    return when (platform) {
        1 -> WECHAT_PKG
        2 -> TIKTOK_PKG
        3 -> KUAISHOU_PKG
        4 -> JD_PKG
        5 -> PDD_PKG
        else -> ""
    }
}

fun getPlatformByAppName(name: String): Int {
    return when (name) {
        "微信" -> 1
        "抖音" -> 2
        "快手" -> 3
        "京东" -> 4
        "拼多多" -> 5
        else -> -1
    }
}

fun getPlatformByInt(platform: Int): String {
    return when (platform) {
        1 -> "微信"
        2 -> "抖音"
        3 -> "快手"
        4 -> "京东"
        5 -> "拼多多"
        else -> "其他平台"
    }
}


class DeviceConstant {
    companion object {
        const val BLUETOOTH_MACADDRESS_KEY = "android.bluetooth.BluetoothAdapter.MacAddress"
        const val BLUETOOTH_NAME_KEY = "android.bluetooth.BluetoothAdapter.name"
        const val DISPLAY_DPI = "android.content.res.display.dpi"
        const val LANGUAGE = "android.content.res.language"
        const val LATITUDE_KEY = "android.location.Location.getLatitude"
        const val LONGITUDE_KEY = "android.location.Location.getLongitude"
        const val NETWORK_TYPE_KEY = "android.net.NetworkInfo.getType"
        const val NETIP_ADDR_KEY = "android.net.wifi.WifiInfo.NetIpAddr"
        const val BSSID_KEY = "android.net.wifi.WifiInfo.getBSSID"
        const val WIFI_MACADDRESS_KEY = "android.net.wifi.WifiInfo.getMacAddress"
        const val WIFI_SSID_KEY = "android.net.wifi.WifiInfo.getSSID"
        const val BUILD_ID_KEY = "android.os.Build.ID"
        const val VERSION_RELEASE_KEY = "android.os.Build.VERSION.RELEASE"
        const val VERSION_SDK_KEY = "android.os.Build.VERSION.SDK"
        const val DESCRIPTION_KEY = "android.os.Build.description"
        const val FINGERPRINT_KEY = "android.os.Build.fingerprint"
        const val MANUFACTURER_KEY = "android.os.Build.ro.product.manufacturer"
        const val MODEL_KEY = "android.os.Build.ro.product.model"
        const val SERIALNO_KEY = "android.os.Build.ro.serialno"
        const val ANDROID_ID_KEY = "android.os.SystemProperties.android_id"
        const val DEVICEID_KEY = "android.telephony.TelephonyManager.getDeviceId"
        const val LINE1NUMBER_KEY = "android.telephony.TelephonyManager.getLine1Number"
        const val SIMCOUNTRYISO_KEY = "android.telephony.TelephonyManager.getSimCountryIso"
        const val SIMOPERATOR_KEY = "android.telephony.TelephonyManager.getSimOperator"
        const val SIMOPERATORNAME_KEY = "android.telephony.TelephonyManager.getSimOperatorName"
        const val SIMSERIALNUMBER_KEY = "android.telephony.TelephonyManager.getSimSerialNumber"
        const val /**/SIMSTATE_KEY = "android.telephony.TelephonyManager.getSimState"
        const val SUBSCRIBERID_KEY = "android.telephony.TelephonyManager.getSubscriberId"
        const val USERAGENT_KEY = "android.webview.WebSettings.setUserAgentString"
    }
}


class ProxyConstant {
    companion object {
        /**
         * 获取IP
         */
        // const val PROXY_IP_URL = "http://192.168.38.1:8096/open?api=acheqcie&close_time=1200&area="  //440000
        const val PROXY_IP_URL = "http://192.168.38.1:8096/open?api=uchdnwhc&close_time=3600"
        const val CITY_CODE_URL = "http://ip.25ios.com:8089/6796324d5300e5978673d71c50780067.php"
        const val PING_URL = "http://pv.sohu.com/cityjson?ie=utf-8"    //https://2020.ip138.com/
        // const val LOCATION_URL =
        //     "http://api.map.baidu.com/geocoder/v2/?ak=134db1b9cf1f1f2b4427210932b34dcb&location=23.125535,113.37&output=json"
        const val LOCATION_URL = "http://ip-api.com/json/"
        const val DATA_TYPE = "multipart/form-data"
        const val POST_PARAM_METHOD = "method"
        const val POST_PARAM_IMEI = "imei"
        const val POST_PARAM_PLATFORMID = "platformId"
        const val POST_PARAM_AREA = "area"
        const val POST_PARAM_PORT = "port"

        const val SP_CITY_LIST = "sp_city_data"
        //data key
        const val KEY_CITY_DATA = "key_cities"              //城市列表
        const val KEY_CITY_GET_DATE = "key_city_get_date"   //获取城市列表的时间
    }
}

data class Result<R>(var code: StatusCode, var r: R, var msg: String)


enum class StatusCode(val code: Int) {
    SUCCEED(200),
    FAILED(400)
}

enum class StatusMsg(val msg: String) {
    DEFAULT("default failed"),
    SUCCEED("succeed")
}

enum class StatusTask(val taskStatus: Int) {
    TASK_UNSTART(0),
    TASK_RUNNING(1),
    TASK_FINISHED(2),
    TASK_EXCEPTION(-1)
}