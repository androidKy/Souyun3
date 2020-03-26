package com.account.manager.ui.phone

import android.view.View
import androidx.lifecycle.Observer
import butterknife.OnClick
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_phone_setup.*

class PhoneSetupFragment : BaseViewModelFragment<PhoneSetupViewModel>() {

    private lateinit var mViewModel: PhoneSetupViewModel

    override fun getViewId(): Int {
        return R.layout.fragment_phone_setup
    }

    override fun initData() {
        mViewModel = getViewModel(PhoneSetupViewModel::class.java).apply {
            tipSetup.observe(this@PhoneSetupFragment,
                Observer<String> { t ->
                    if (t == "开始改机...") {
                        //弹框提示正在修改
                        showDialog()
                    } else {
                        hideDialog()
                    }
                    tv_setup_tip.text = t
                })
            phoneModel.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_phone_type.text = t })
            phoneImei.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_phone_imei.text = t })
            ip.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_ip_value.text = t })
            ipCity.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_ip_city.text = t })
            appList.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_apps.text = t })
            cityName.observe(this@PhoneSetupFragment,
                Observer<String> { t -> tv_ip_city_name.text = t })
            location.observe(this@PhoneSetupFragment,
                Observer<String>{t -> tv_cur_location.text = t})

            initPhoneInfo()
        }
    }

    override fun initListener() {
        rg_choose.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_onekey_setup) {
                ll_choose.visibility = View.GONE
                bt_phone_setup.text = "一键改机"
                tv_apps.text = resources.getString(R.string.app_list)

                mViewModel.clearChooseInfo()

            } else {
                ll_choose.visibility = View.VISIBLE
                bt_phone_setup.text = "开始改机"
                tv_apps.text = ""
            }
        }
    }


    @OnClick(R.id.bt_phone_setup)
    fun phoneSetup() {
        //判断是一键改机还是选择改机
        if (rb_onekey_setup.isChecked)
            mViewModel.setupPhone()
        else mViewModel.multiSetupPhone()
    }

    @OnClick(R.id.bt_choose_apps)
    fun chooseApps() {
        activity?.apply {
            mViewModel.chooseApps(this)
        }
    }

    @OnClick(R.id.bt_choose_ip)
    fun chooseIp() {
        activity?.apply {
            mViewModel.chooseIp(this)
        }

    }

}