package com.dj.ip.proxy.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.safframework.log.L
import com.utils.common.Utils

/**
 * description:
 * author:kyXiao
 * date:2020/4/23
 */
class NetworkMonitor {
    companion object {
        const val MSG_NETSTATE_WHAT = 0x0001
        const val ACTION_LOCAL_BROADCAST = "com.dj.ip.local.receiver"
        const val KEY_NET_STATE = "key_net_state"

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkMonitor()
        }

        val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == MSG_NETSTATE_WHAT) {
                    val netState = NetworkUtils.isDefaultNetworkActive(Utils.getApp())
                    if (!netState) {
                        LocalBroadcastManager.getInstance(Utils.getApp())
                            .sendBroadcast(Intent(ACTION_LOCAL_BROADCAST).apply {
                                putExtra(KEY_NET_STATE, netState)
                            })
                    }
                }
            }
        }
    }

    private var mNetworkCallbackImpl: ConnectivityManager.NetworkCallback? = null
    private val mNetworkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
                sendNetState(NetworkUtils.isDefaultNetworkActive(Utils.getApp()))
            }
        }
    }

    private val connectivityManager: ConnectivityManager =
        Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun register(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mNetworkCallbackImpl = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    L.d("onAvailable")
                    sendNetState(true)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    L.d("onCapabilitiesChanged")
                }

                override fun onUnavailable() {
                    L.d("onUnavailable")
                }

                override fun onLost(network: Network) {
                    L.d("onLost")
                    sendNetState(false)
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    L.d("onLinkPropertiesChanged")
                }
            }
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> connectivityManager.registerDefaultNetworkCallback(
                mNetworkCallbackImpl!!
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networkRequest: NetworkRequest = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallbackImpl!!)
            }
            else -> context.applicationContext.registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun unregister(context: Context) {

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ->
                if (mNetworkCallbackImpl != null)
                    connectivityManager.unregisterNetworkCallback(mNetworkCallbackImpl!!)
            else -> context.applicationContext.unregisterReceiver(mNetworkReceiver)
        }
    }

    private fun sendNetState(netState: Boolean) {
        //如果网络不通，延迟15秒后去检测网络，网络继续不通则提示网络连接失败，并尝试去重新连接代理
        if (!netState) {
            mHandler.sendEmptyMessageDelayed(MSG_NETSTATE_WHAT, 8 * 1000)
        } else {
            mHandler.removeCallbacksAndMessages(null)
            sendBroadcast(netState)
        }
    }

    private fun sendBroadcast(netState: Boolean) {
        LocalBroadcastManager.getInstance(Utils.getApp())
            .sendBroadcast(Intent(ACTION_LOCAL_BROADCAST).apply {
                putExtra(KEY_NET_STATE, netState)
            })
    }
}