package com.android.souyun.base

import android.app.Application
import com.task.cn.util.AppUtils

/**
 * Description:
 * Created by Quinin on 2020-03-06.
 **/
class BaseApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        AppUtils.init(this)

    }
}