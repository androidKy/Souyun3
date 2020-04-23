package com.dj.ip.proxy.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dj.ip.proxy.R
import com.dj.ip.proxy.ui.MainActivity
import com.utils.common.Utils


/**
 * description:
 * author:kyXiao
 * date:2020/4/22
 */
class NotificationStarter {
    companion object {
        val CHANNEL_ID = Utils.getApp()?.packageName!!

        fun startNotification(msg: String) {
            // val msg = if (isNetActived) "连接成功" else "未连接"

            val context = Utils.getApp()

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "网络状态"
                val descriptionText = "网络切换通知"
                val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                notificationManager.createNotificationChannel(channel)
            }
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(com.dj.ip.proxy.R.drawable.ic_stat_name)
                .setContentTitle(context.resources.getString(com.dj.ip.proxy.R.string.app_name))
                .setContentText(msg)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(msg)
                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.addAction(R.drawable.ip_connect, "打开", pendingIntent)
                .build()

           // notification.flags = Notification.FLAG_ONGOING_EVENT


            notificationManager.notify(1000, notification)
        }
    }
}