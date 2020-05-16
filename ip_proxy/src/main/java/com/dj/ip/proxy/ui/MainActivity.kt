package com.dj.ip.proxy.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dj.ip.proxy.Constants
import com.dj.ip.proxy.R
import com.dj.ip.proxy.base.BaseActivity
import com.dj.ip.proxy.bean.CityListBean
import com.dj.ip.proxy.bean.IpBean
import com.dj.ip.proxy.bean.IspBean
import com.dj.ip.proxy.network.NetworkMonitor
import com.dj.ip.proxy.notification.NotificationStarter
import com.dj.ip.proxy.proxy.IpListener
import com.dj.ip.proxy.proxy.PingManager
import com.dj.ip.proxy.proxy.ProxyController
import com.dj.ip.proxy.proxy.ProxyRequestListener
import com.dj.ip.proxy.view.CityPicker
import com.dj.ip.proxy.view.IspPopWindow
import com.google.gson.Gson
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.safframework.log.L
import com.utils.common.SPUtils
import com.utils.common.ThreadUtils
import com.utils.common.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.Closeable

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class MainActivity : BaseActivity(), View.OnClickListener, IspPopWindow.OnItemClickListener {


    private var mPsw: String = ""
    private var mCityCode: String = ""
    private var mCityName: String = ""

    private var mCityData: ArrayList<CustomCityData> = arrayListOf()

    private var mAnimator: Animator? = null

    private lateinit var mPopupIsp: IspPopWindow

    private var mCurIspBean: IspBean = IspBean("随机", 0, 1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        L.d("onCreate")
        initView()
        initListener()
        initData()

        LocalBroadcastManager.getInstance(this.applicationContext).registerReceiver(
            mReceiver,
            IntentFilter(NetworkMonitor.ACTION_LOCAL_BROADCAST)
        )
        NetworkMonitor.instance.register(this)
    }

    private fun initView() {
        mPopupIsp = IspPopWindow(this)
        mPopupIsp.setOnItemClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Debug.stopMethodTracing()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }


    private fun initListener() {
        tv_address_choose.setOnClickListener(this)
        tv_psw_change.setOnClickListener(this)
        rl_ip_connect.setOnClickListener(this)
        tv_auto_refresh.setOnClickListener(this)
        tv_ip_isp.setOnClickListener(this)
    }

    private fun initData() {
        SPUtils.getInstance(Constants.IP_PROXY_SP).apply {
            mPsw = getString(Constants.PSW_KEY)
            mCityName = getString(Constants.CITY_NAME_KEY)
            mCityCode = getString(Constants.CITY_CODE_KEY)
        }

        intent?.getStringExtra(Constants.PSW_KEY)?.apply {
            mPsw = this
        }

        initCity()

        getIpInfo()

    }

    private fun initCity() {
        if (mCityName.isNotEmpty())
            tv_address_choose.text = mCityName

        ThreadUtils.executeByCached(object : ThreadUtils.Task<ArrayList<CustomCityData>>() {
            override fun doInBackground(): ArrayList<CustomCityData> {
                val cityData = arrayListOf<CustomCityData>()
                val inputStream = this@MainActivity.assets.open("cityList_final.json")
                var bufferedInputStream: BufferedInputStream? = null
                try {
                    bufferedInputStream = BufferedInputStream(inputStream)
                    bufferedInputStream.readBytes().run {
                        val cityLissetBean = Gson().fromJson(String(this), CityListBean::class.java)
                        for (province in cityLissetBean.data.cityList) {
                            val provinceCustom = CustomCityData("", province.name)
                            val customCityList = arrayListOf<CustomCityData>()
                            for (city in province.data) {
                                if (city.name != "0") {
                                    val customCityData = CustomCityData(city.cityid, city.name)
                                    customCityList.add(customCityData)
                                }
                            }
                            provinceCustom.list = customCityList
                            cityData.add(provinceCustom)
                        }
                    }

                } catch (e: Exception) {
                    L.e(e.message, e)
                } finally {
                    closeStream(inputStream)
                }

                return cityData
            }

            override fun onSuccess(result: ArrayList<CustomCityData>) {
                mCityData.clear()
                mCityData.addAll(result)
            }

            override fun onCancel() {
            }

            override fun onFail(t: Throwable?) {

            }

            private fun closeStream(closeable: Closeable?) {
                try {
                    closeable?.close()
                } catch (e: Exception) {
                    L.e(e.message, e)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_address_choose -> {
                chooseAddress()
            }
            R.id.tv_psw_change -> {
                val intent = Intent(this, PswActivity::class.java).apply {
                    putExtra(Constants.PSW_KEY, mPsw)
                }
                startActivityForResult(intent, 2000)
            }
            R.id.rl_ip_connect -> {
                startConnectIp()
            }
            R.id.tv_auto_refresh -> {
                val curText = tv_auto_refresh.text.toString()
                if (curText == resources.getString(R.string.auto_refresh)) {
                    tv_auto_refresh.text = resources.getString(R.string.close_auto_refresh)
                } else tv_auto_refresh.text = resources.getString(R.string.auto_refresh)
            }
            R.id.tv_ip_isp -> {
               mPopupIsp.show()
            }
        }
    }

    override fun onItemClick(ispBean: IspBean) {
        tv_ip_isp.text = String.format("运营商：%s", ispBean.ispName)
        mCurIspBean = ispBean
    }

    private fun startConnectIp() {
        if (mAnimator != null && mAnimator?.isRunning!!) {
            ToastUtils.showToast(this, "正在连接...")
            return
        }
        if (mCityCode.isEmpty()) {
            ToastUtils.showToast(this, "请选择地区")
            return
        }
        showConnectAnimation()
        connectIP()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                mPsw = data?.getStringExtra(Constants.PSW_KEY) ?: ""
                L.d("psw: $mPsw")
            }
        }
    }

    private fun chooseAddress() {
        CityPicker()
            .showCityPicker(this, mCityData, object : OnCustomCityPickerItemClickListener() {
                override fun onSelected(
                    province: CustomCityData?,
                    city: CustomCityData?,
                    district: CustomCityData?
                ) {
                    city?.apply {
                        mCityCode = this.id
                    }
                    tv_address_choose.text = "${province?.name}${city?.name}"
                    mCityName = "${province?.name}${city?.name}"

                    saveIpInfo()
                }
            })
    }

    /**
     * 连接IP
     */
    private fun connectIP() {
        ProxyController().setData(mPsw, mCityName, mCityCode, mCurIspBean.ispFlag)
            .setProxyRequestListener(object : ProxyRequestListener {
                override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                    var tip = resources.getString(R.string.ip_connect_failed)
                    if (result) {
                        L.d("IP连接成功")
                        showIpInfo(ipBean)
                        setIpStatus(resources.getString(R.string.ip_connect_succeed_refresh))
                        saveIpInfo()
                        tip = resources.getString(R.string.ip_connect_succeed)
                    } else {
                        ToastUtils.showToast(this@MainActivity, "IP连接失败")
                        setIpStatus(resources.getString(R.string.ip_connect_failed))
                    }
                    ToastUtils.showToast(this@MainActivity, tip)
                    NotificationStarter.startNotification(tip)
                    mAnimator?.cancel()
                }
            })
            .startProxy()
    }

    /**
     * 连接动画
     */
    private fun showConnectAnimation() {
        setIpStatus(resources.getString(R.string.ip_connecting))
        iv_ip_connect.setBackgroundResource(R.drawable.ip_connect)
        /* AnimationUtils.loadAnimation(this, R.anim.shrink_anim).apply {
            // fillAfter = true
             iv_ip_connect.startAnimation(this)
         }*/
        mAnimator?.apply {
            cancel()
            start()
            return
        }

        AnimatorInflater.loadAnimator(this, R.animator.shrink_anim).apply {
            mAnimator = this
            this.setTarget(iv_ip_connect)
            this.start()
        }
    }

    /**
     * 查询IP
     */
    private fun getIpInfo() {
        PingManager.getNetIP(object : IpListener {
            override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                if (result) {
                    showIpInfo(ipBean)
                }
            }
        })
    }

    /**
     * 展示IP信息
     */
    private fun showIpInfo(ipBean: IpBean?) {
        tv_ip.text = ipBean?.cip ?: ""
        tv_address_value.text = ipBean?.cname ?: ""
    }

    /**
     * 设置IP状态
     */
    private fun setIpStatus(ipStatus: String) {
        tv_ip_status.text = ipStatus
    }

    /**
     * 保存IP信息
     */
    private fun saveIpInfo() {
        SPUtils.getInstance(Constants.IP_PROXY_SP).apply {
            put(Constants.CITY_CODE_KEY, mCityCode)
            put(Constants.CITY_NAME_KEY, mCityName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        L.d("onDestroy")
        mAnimator?.cancel()
        NetworkMonitor.instance.unregister(this)
        LocalBroadcastManager.getInstance(this.applicationContext).unregisterReceiver(mReceiver)
        mPopupIsp?.let {
            it.dismiss()
        }
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == NetworkMonitor.ACTION_LOCAL_BROADCAST) {
                val netState = intent.getBooleanExtra(NetworkMonitor.KEY_NET_STATE, false)
                L.d("netState:$netState")
                val curConnectSetting = tv_auto_refresh.text.toString()
                if (!netState && curConnectSetting != resources.getString(R.string.auto_refresh))
                    startConnectIp()
            }
        }
    }
}