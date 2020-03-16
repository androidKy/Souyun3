package com.account.manager.net

import com.account.manager.LoginConstant
import com.account.manager.UrlConstant.Companion.CONTENT_TYPE
import com.account.manager.UrlConstant.Companion.LOGIN_URL
import com.account.manager.UrlConstant.Companion.POST_ACCOUNT_URL
import com.account.manager.model.BaseData
import com.account.manager.model.CommitAccountBean
import com.account.manager.model.CommitAccountResult
import com.account.manager.ui.login.data.model.LoginData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.SPConstant
import com.task.cn.database.RealmHelper
import com.task.cn.getPlatformByAppName
import com.utils.common.SPUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:
 * Created by Quinin on 2020-03-13.
 **/
class NetManager {

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
                    error(anError)
                }
            })
    }

    /**
     * {"account": "1784389748","password": "abc123456","platform": "1","device_id": "1","login_info":
     * {"ip": "192.89.194.3","city": "广州市","city_code": "55500","login_date": "2020-3-2 17:53"},
     * "register_date": "2020-3-3 17:53"}
     * 提交账号到服务器
     */
    fun commitAccount(platform: String, accountName: String, psw: String) {
        val commitAccountBean = CommitAccountBean().apply {
            this.platform = getPlatformByAppName(platform).toString()
            this.account = accountName
            this.password = psw
            this.register_date =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).run { format(Date()) }
            this.device_id =
                SPUtils.getInstance(SPConstant.SP_DEVICE_INFO).getString(SPConstant.KEY_DEVICE_ID)
        }
        val loginInfoBean = CommitAccountBean.LoginInfoBean().apply {
            val spUtils = SPUtils.getInstance(SPConstant.SP_IP_INFO)
            this.ip = spUtils.getString(SPConstant.KEY_IP)
            this.city = spUtils.getString(SPConstant.KEY_CITY_NAME)
            this.city_code = spUtils.getString(SPConstant.KEY_CITY_CODE)
            this.login_date =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).run { format(Date()) }
        }

        commitAccountBean.login_info = loginInfoBean
        val accountJson = Gson().toJson(commitAccountBean, CommitAccountBean::class.java)

        L.d("提交的账号信息：$accountJson")

        AndroidNetworking.post(POST_ACCOUNT_URL)
            .setContentType(CONTENT_TYPE)
            .addBodyParameter(
                "uid",
                SPUtils.getInstance(LoginConstant.SP_LOGIN_INFO).getString(LoginConstant.KEY_USER_ID)
            )
            .addBodyParameter(
                "token",
                SPUtils.getInstance(LoginConstant.SP_LOGIN_INFO).getString(LoginConstant.KEY_USER_TOKEN)
            )
            .addBodyParameter("account_info", accountJson)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    L.d("提交账号结果：$response")
                    val commitAccountResult =
                        Gson().fromJson(response, CommitAccountResult::class.java)
                    requestListener?.onSucceed(commitAccountResult)
                }

                override fun onError(anError: ANError?) {
                    error(anError)
                }
            })
    }

    fun error(anError: ANError?) {
        anError?.errorDetail?.let {
            requestListener?.onError(it)
        }
    }
}

interface RequestListener {
    fun onSucceed(result: BaseData)
    fun onError(msg: String)
}
