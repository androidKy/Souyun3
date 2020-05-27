package com.dj.ip.proxy.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.dj.ip.proxy.*
import com.dj.ip.proxy.base.BaseActivity
import com.dj.ip.proxy.network.NetworkLog
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.utils.common.PermissionUtils
import com.utils.common.SPUtils
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        tv_version.text = BuildConfig.VERSION_NAME

        val permissions = arrayListOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        requestPermission(permissions)
    }

    private fun requestPermission(permissions: List<String>) {
        PermissionUtils.isGranted(permissions).let {
            if (it)
                startMainActivity()
            else PermissionUtils.permission(permissions).callback(object :
                PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    startMainActivity()
                }

                override fun onDenied() {
                    requestPermission(permissions)
                }
            }).request()
        }
    }

    private fun startMainActivity() {
        tv_version.postDelayed({
            val psw = SPUtils.getInstance(Constants.IP_PROXY_SP).getString(Constants.PSW_KEY)
            if (psw.isEmpty()) {
                startActivity(Intent(this, PswActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 1000)
    }
}