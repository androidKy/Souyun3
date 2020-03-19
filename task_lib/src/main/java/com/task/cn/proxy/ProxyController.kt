package com.task.cn.proxy

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.*
import com.task.cn.ProxyConstant.Companion.CITY_CODE_URL
import com.task.cn.ProxyConstant.Companion.DATA_TYPE
import com.task.cn.ProxyConstant.Companion.KEY_CITY_DATA
import com.task.cn.ProxyConstant.Companion.KEY_CITY_GET_DATE
import com.task.cn.ProxyConstant.Companion.POST_PARAM_IMEI
import com.task.cn.ProxyConstant.Companion.POST_PARAM_METHOD
import com.task.cn.ProxyConstant.Companion.POST_PARAM_PLATFORMID
import com.task.cn.ProxyConstant.Companion.SP_CITY_LIST
import com.task.cn.jbean.CityListBean
import com.task.cn.jbean.IpInfoBean
import com.task.cn.jbean.VerifyIpBean
import com.utils.common.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:
 * Created by Quinin on 2020-03-05.
 **/
class ProxyController : IProxy {

    private var proxyRequestListener: ProxyRequestListener? = null
    private var mCityCode: String? = ""
    private var mCityName: String? = ""

    fun setCityData(cityCode: String?, cityName: String?): ProxyController {
        mCityCode = cityCode
        mCityName = cityName
        return this
    }

    override fun startProxy(proxyRequestListener: ProxyRequestListener) {
        this.proxyRequestListener = proxyRequestListener

        if (mCityCode.isNullOrEmpty()) {
            if (mCityName.isNullOrEmpty())
                changeIpByCityCode("")
            else getCityCode(mCityName!!)
        } else {
            changeIpByCityCode(mCityCode!!)
        }
    }

    private fun getCityCode(cityName: String) {
        getCityList(cityName)
    }

    /**
     * 通过城市代码更改IP
     */
    private fun changeIpByCityCode(cityCode: String) {
        L.d("cityCode: $cityCode")
        //val proxyUrl ="$PROXY_IP_URL$cityCod e&ip=${DevicesUtil.getIPAddress(Utils.getApp())}"
        var proxyUrl = ProxyConstant.PROXY_IP_URL
        if (cityCode.isNotEmpty()) {
            proxyUrl = "${ProxyConstant.PROXY_IP_URL}&area=$cityCode"
        }

        Result(StatusCode.FAILED, IpInfoBean(), "获取代理IP失败").run {
            AndroidNetworking.get(proxyUrl)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        L.d("请求更换接口: $response")
                        if (!response.isNullOrEmpty() && response == "ok") {
                            this@run.code = StatusCode.SUCCEED
                            this@run.msg = StatusMsg.SUCCEED.msg
                            this@run.r.city_code = cityCode.toLong()
                        }

                        checkIP(this@run)
                    }

                    override fun onError(anError: ANError?) {
                        val errorMsg = anError?.response?.message()
                        if (!errorMsg.isNullOrEmpty())
                            this@run.msg = errorMsg

                        setRequestResult(this@run)
                    }
                })
        }
    }

    private fun checkIP(ipResult: Result<IpInfoBean>) {
        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, verifyIpBean: VerifyIpBean?) {
                if (result) {
                    verifyIpBean?.apply {
                        ipResult.r.ip = this.cip
                        ipResult.r.city_code = this.cid.toLong()
                        ipResult.r.city = this.cname

                        saveIpInfo(this.cip, this.cname, this.cid)
                    }
                } else {
                    ipResult.code = StatusCode.FAILED
                    ipResult.msg = "网络连接失败"
                }

                setRequestResult(ipResult)
            }

        })
    }

    private fun saveIpInfo(ip: String, cityName: String, cityCode: String) {
        SPUtils.getInstance(SPConstant.SP_IP_INFO).apply {
            put(SPConstant.KEY_IP, ip)
            put(SPConstant.KEY_CITY_NAME, cityName)
            put(SPConstant.KEY_CITY_CODE, cityCode)
        }
    }

    /**
     * 获取城市列表
     */
    private fun getCityList(cityName: String) {
        val currentDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).run { format(Date()) }
        L.i("currentDate: $currentDate")
        val lastGetCityDate = SPUtils.getInstance(SP_CITY_LIST).getString(KEY_CITY_GET_DATE)

        if (lastGetCityDate.isNullOrEmpty() || TimeUtils.getDays(
                currentDate,
                lastGetCityDate
            ) >= 1
        ) { //对比当前时间与上次获取的时间
            //重新获取
            L.d("重新获取城市ID")
            //getCityList()
            val imei = DevicesUtil.getIMEI(Utils.getApp())
            L.d("imei: $imei")
            AndroidNetworking.post(CITY_CODE_URL)
                .setContentType(DATA_TYPE)
                .addBodyParameter(POST_PARAM_METHOD, "getCity")
                .addBodyParameter(POST_PARAM_IMEI, imei)
                .addBodyParameter(POST_PARAM_PLATFORMID, "2")   //Android:2 IOS:1
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        response?.run {
                            val strResult = toString()
                            L.i("threadID = ${ThreadUtils.isMainThread()} \ngetCityList result: $strResult")
                            //保存城市列表，隔一天再重新获取
                            SPUtils.getInstance(SP_CITY_LIST).apply {
                                put(KEY_CITY_DATA, strResult)
                                put(KEY_CITY_GET_DATE,
                                    SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss",
                                        Locale.CHINA
                                    ).run { format(Date()) })
                            }
                            getCityCodeFromCityList(cityName, strResult)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        L.d("getCityList error: ${anError?.errorDetail} result: ${anError?.response?.body()?.string()}")
                        val result = Result(
                            StatusCode.FAILED,
                            IpInfoBean(),
                            "getCityList error：${anError?.response?.body()?.string()}"
                        )
                        setRequestResult(result)
                    }
                })
        } else {
            val data = SPUtils.getInstance(SP_CITY_LIST).getString(KEY_CITY_DATA)
            getCityCodeFromCityList(cityName, data)
        }
    }

    /**
     * 从城市列表校验得到城市ID
     */
    private fun getCityCodeFromCityList(cityName: String, cityList: String) {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
            override fun doInBackground(): String {
                var result: String = ""

                val cityListBean = Gson().fromJson(cityList, CityListBean::class.java)
                if (cityListBean.code == 0) {   //获取数据成功
                    val provinceList = cityListBean.data.cityList   //省
                    for (province in provinceList) {
                        for (city in province.data) {
                            if (cityName == city.name) {
                                result = city.cityid
                                L.i("target cityName: $cityName cityID: $result")
                                break
                            }
                        }
                    }
                }

                return result
            }

            override fun onSuccess(result: String?) {
                //城市ID获取完成，开始打开端口
                if (!result.isNullOrEmpty()) {
                    changeIpByCityCode(result)
                } else {
                    /*  L.i("$cityName 该城市没有IP，重新获取地址")
                      Result(StatusCode.FAILED, IpInfoBean(), "$cityName 该城市没有IP，重新获取地址").run {
                          setRequestResult(this)
                      }*/
                    changeIpByCityCode("")
                }
            }

            override fun onCancel() {
                L.i("获取城市ID取消")
            }

            override fun onFail(t: Throwable?) {
                L.i("获取城市ID失败: ${t?.message}")
                Result(StatusCode.FAILED, IpInfoBean(), "获取城市ID失败: ${t?.message}").run {
                    setRequestResult(this)
                }
            }

        })
    }

    private fun setRequestResult(result: Result<IpInfoBean>) {
        proxyRequestListener?.onProxyResult(result)
    }
}