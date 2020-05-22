package com.dj.ip.proxy.utils

import android.content.Context
import com.dj.ip.proxy.bean.CityListBean
import com.google.gson.Gson
import com.safframework.log.L
import com.utils.common.ThreadUtils
import java.io.BufferedInputStream
import java.io.Closeable

/**
 * description:
 * author:kyXiao
 * date:2020/5/22
 */
class CityUtils {

    companion object {
        fun getCityCodeByCityName(
            context: Context,
            cityName: String? = "广州市",
            cityCodeListener: CityCodeListener?
        ) {
            ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
                override fun doInBackground(): String {
                    val inputStream = context.assets.open("cityList_final.json")
                    var bufferedInputStream: BufferedInputStream? = null
                    try {
                        bufferedInputStream = BufferedInputStream(inputStream)
                        bufferedInputStream.readBytes().run {
                            val cityLissetBean =
                                Gson().fromJson(String(this), CityListBean::class.java)
                            for (province in cityLissetBean.data.cityList) {
                                for (city in province.data) {
                                    if (city.name != "0" && !cityName.isNullOrEmpty()) {
                                        if (city.name == cityName || city.name.contains(cityName)) {
                                            return city.cityid
                                        }
                                    }
                                }
                            }
                        }

                    } catch (e: Exception) {
                        L.e(e.message, e)
                    } finally {
                        closeStream(inputStream)
                    }

                    return "440100"
                }

                override fun onSuccess(result: String) {
                    cityCodeListener?.onCityCode(result)
                }

                override fun onCancel() {
                }

                override fun onFail(t: Throwable?) {

                }

                private fun closeStream(closeable: Closeable?) {
                    try {
                        closeable?.close()
                    } catch (e: Exception) {

                    }
                }
            })
        }
    }

    interface CityCodeListener {
        fun onCityCode(cityCode: String)
    }
}