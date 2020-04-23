package com.dj.ip.proxy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.dj.ip.proxy.notification.NotificationStarter
import com.safframework.log.L

/**
 * description:
 * author:kyXiao
 * date:2020/4/23
 */
class NetStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        L.d("网络状态变化")
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
            NotificationStarter.startNotification("")
        }
    }
}