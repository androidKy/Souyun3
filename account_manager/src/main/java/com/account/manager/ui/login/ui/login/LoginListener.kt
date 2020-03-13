package com.account.manager.ui.login.ui.login

import com.account.manager.ui.login.data.Result
import com.account.manager.ui.login.data.model.LoggedInUser

/**
 * Description:
 * Created by Quinin on 2020-03-13.
 **/
interface LoginListener {
    fun onLoginResult(result:Result<LoggedInUser>)
}