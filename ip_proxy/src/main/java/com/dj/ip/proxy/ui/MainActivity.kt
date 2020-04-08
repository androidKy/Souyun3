package com.dj.ip.proxy.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.dj.ip.proxy.Constants
import com.dj.ip.proxy.R
import com.dj.ip.proxy.base.BaseActivity
import com.dj.ip.proxy.bean.IpBean
import com.dj.ip.proxy.network.NetStateObserver
import com.dj.ip.proxy.network.NetworkDetector
import com.dj.ip.proxy.network.NetworkType
import com.dj.ip.proxy.proxy.IpListener
import com.dj.ip.proxy.proxy.PingManager
import com.dj.ip.proxy.proxy.ProxyController
import com.dj.ip.proxy.proxy.ProxyRequestListener
import com.dj.ip.proxy.view.CityPicker
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.CustomCityData
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import com.safframework.log.L
import com.utils.common.SPUtils
import com.utils.common.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private var mPsw: String = ""
    private var mCityCode: String = ""
    private var mCityName: String = ""

    private var mAnimator: Animator? = null

    private val mNetStateObserver: NetStateObserver = object : NetStateObserver {
        override fun onDisconnected() {
            tv_net_error.visibility = View.VISIBLE
            tv_ip.text = ""
            tv_address_value.text = ""
        }

        override fun onConnected(networkType: NetworkType?) {
            tv_net_error.visibility = View.GONE

            getIpInfo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListener()
        initData()
    }


    private fun initListener() {
        tv_address_choose.setOnClickListener(this)
        tv_psw_change.setOnClickListener(this)
        rl_ip_connect.setOnClickListener(this)
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

        initNetWork()
    }

    private fun initNetWork() {
        NetworkDetector.getInstance().addObserver(mNetStateObserver)
    }

    private fun initCity() {
        if (mCityName.isNotEmpty())
            tv_address_choose.text = mCityName
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
                if (mAnimator != null && mAnimator?.isRunning!!) {
                    ToastUtils.showToast(this, "正在连接，请勿重复点击")
                    return
                }
                if (mCityCode.isEmpty()) {
                    ToastUtils.showToast(this, "请选择地区")
                    return
                }
                showConnectAnimation()
                connectIP()
            }
        }
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
            .showCityPicker(this, object : OnCustomCityPickerItemClickListener() {
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
                }
            })
    }

    /**
     * 连接IP
     */
    private fun connectIP() {
        ProxyController().setData(mPsw, mCityName, mCityCode)
            .setProxyRequestListener(object : ProxyRequestListener {
                override fun onIpResult(result: Boolean, ipBean: IpBean?) {
                    if (result) {
                        L.d("IP连接成功")
                        showIpInfo(ipBean)
                        setIpStatus(resources.getString(R.string.ip_connect_succeed))
                        saveIpInfo()
                    } else {
                        ToastUtils.showToast(this@MainActivity, "IP连接失败")
                        setIpStatus(resources.getString(R.string.ip_connect_failed))
                    }
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
        mAnimator?.cancel()
        NetworkDetector.getInstance().removeObserver(mNetStateObserver)
        NetworkDetector.getInstance().deInit(this)
    }

}