package com.account.manager.net

import com.account.manager.UrlConstant.Companion.CONTENT_TYPE
import com.account.manager.UrlConstant.Companion.LOGIN_URL
import com.account.manager.model.BaseData
import com.account.manager.ui.login.data.model.LoginData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L

/**
 * Description:
 * Created by Quinin on 2020-03-13.
 **/
class  NetManager{

    private var requestListener: RequestListener? = null

    fun setRequestListener(requestListener: RequestListener): NetManager {
        this.requestListener = requestListener
        return this
    }

    fun login(username: String, userpsw: String) {
        AndroidNetworking.post(LOGIN_URL)
            .setContentType(CONTENT_TYPE)
            .addBodyParameter("phone", username)
            .addBodyParameter("password", userpsw)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val loginData = Gson().fromJson(response, LoginData::class.java)
                    requestListener?.onSucceed(loginData)
                }

                override fun onError(anError: ANError?) {
                    anError?.errorDetail?.let {
                        requestListener?.onError(it)
                    }
                }
            })
    }
}

interface RequestListener {
    fun onSucceed(result: BaseData)
    fun onError(msg: String)
}
