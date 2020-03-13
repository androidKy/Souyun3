package com.account.manager

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.safframework.log.L
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.task.cn.util.AppUtils

/**
 * Description:
 * Created by Quinin on 2020-03-12.
 **/
class App : Application() {

    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                //layout.setPrimaryColorsId(android.R.color.white) //全局设置主题颜色
                ClassicsHeader(context)
            }

            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                //layout.setPrimaryColorsId(android.R.color.white, android.R.color.white)
                ClassicsFooter(context)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        L.init("AccountManager")
        AppUtils.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}