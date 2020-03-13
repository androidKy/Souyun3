package com.account.manager.ui.login.data

import com.account.manager.LoginConstant
import com.account.manager.model.BaseData
import com.account.manager.net.NetManager
import com.account.manager.net.RequestListener
import com.account.manager.ui.login.data.model.LoggedInUser
import com.account.manager.ui.login.data.model.LoginData
import com.account.manager.ui.login.ui.login.LoginListener
import com.utils.common.SPUtils
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(
        username: String,
        password: String,
        loginListener: LoginListener
    ) {
        try {
            NetManager()
                .setRequestListener(object : RequestListener {
                    override fun onSucceed(result: BaseData) {
                        val loginData = result as LoginData
                        if (loginData.ret == 200) {
                            saveLoginInfo(username, password, loginData.data)
                            loginListener.onLoginResult(
                                Result.Success(
                                    LoggedInUser(
                                        loginData.data.user_id.toString(),
                                        loginData.data.token,
                                        username
                                    )
                                )
                            )
                        } else {
                            loginListener.onLoginResult(Result.Error(IOException("登录失败")))
                        }
                    }

                    override fun onError(msg: String) {
                        loginListener.onLoginResult(Result.Error(IOException(msg)))
                    }
                })
                .login(username, password)
        } catch (e: Throwable) {
            loginListener.onLoginResult(Result.Error(IOException("登录失败", e)))
        }
    }

    private fun saveLoginInfo(
        username: String,
        password: String,
        login: LoginData.Login
    ) {
        SPUtils.getInstance(LoginConstant.SP_LOGIN_INFO)
            .apply {
                put(LoginConstant.KEY_USER_ID, login.user_id.toString())
                put(LoginConstant.KEY_USER_NAME, username)
                put(LoginConstant.KEY_USER_PHONE, username)
                put(LoginConstant.KEY_USER_PSW, password)
                put(LoginConstant.KEY_USER_TOKEN, login.token)
                put(LoginConstant.KEY_LOGIN_TIME, login.login_time)
            }
    }

    fun logout() {

    }
}

