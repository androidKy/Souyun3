package com.account.manager

/**
 * Description:
 * Created by Quinin on 2020-03-13.
 **/

class LoginConstant {
    companion object {
        const val REQUEST_LOGIN_CODE = 2000

        const val SP_LOGIN_INFO = "sp_login_info"

        const val KEY_USER_ID = "user_id"
        const val KEY_USER_TOKEN = "user_token"
        const val KEY_LOGIN_TIME = "login_time"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_PHONE = "user_phone"
        const val KEY_USER_PSW = "user_psw"
    }
}

class UrlConstant{
    companion object{
        const val LOGIN_URL = "http://www.10gbuy.com/?s=App.User.Login"//&phone=13269684931&password=1478473981
        const val REGISTER_URL= "http://www.10gbuy.com/?s=App.User.Reg"
        const val GET_ACCOUNTS_URL = "http://www.10gbuy.com/?s=App.User.GetList"
        const val POST_ACCOUNT_URL = "http://www.10gbuy.com/?s=App.User.SaveOne"
        const val UPDATE_DEVICE_INFO_URL = "http://www.10gbuy.com/?s=App.User.SetDevice"

        const val CONTENT_TYPE = "multipart/form-data"
    }
}
