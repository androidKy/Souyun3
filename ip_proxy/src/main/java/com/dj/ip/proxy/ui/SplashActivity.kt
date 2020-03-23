package com.dj.ip.proxy.ui

import android.content.Intent
import android.os.Bundle
import com.dj.ip.proxy.Constants
import com.dj.ip.proxy.R
import com.dj.ip.proxy.base.BaseActivity
import com.utils.common.SPUtils

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val psw = SPUtils.getInstance(Constants.IP_PROXY_SP).getString(Constants.PSW_KEY)

        if (psw.isEmpty()) {
            startActivity(Intent(this, PswActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}