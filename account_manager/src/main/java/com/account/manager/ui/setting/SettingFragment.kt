package com.account.manager.ui.setting

import android.widget.CompoundButton
import android.widget.RadioGroup
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import com.android.xplugin.util.XSPUtils
import com.utils.common.SPUtils
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseViewModelFragment<SettingViewModel>(),
    CompoundButton.OnCheckedChangeListener {

    private lateinit var mViewModel: SettingViewModel

    override fun getViewId(): Int {
        return R.layout.fragment_setting
    }

    override fun initData() {
        mViewModel = getViewModel(SettingViewModel::class.java).apply {

        }

        initView()
    }

    private fun initView() {
        SPUtils.getInstance(XSPUtils.DATA_HOOK_SP).apply {
            sw_wx_setting.isChecked = getBoolean(XSPUtils.LOGIN_WECHAT_KEY)
            sw_tiktok_setting.isChecked = getBoolean(XSPUtils.LOGIN_TIKTOK_SWITCH_KEY)
            sw_ks_setting.isChecked = getBoolean(XSPUtils.LOGIN_KS_SWTICH_KEY)
        }


    }

    override fun initListener() {
        sw_wx_setting.setOnCheckedChangeListener(this)
        sw_jd_setting.setOnCheckedChangeListener(this)
        sw_ks_setting.setOnCheckedChangeListener(this)
        sw_pdd_setting.setOnCheckedChangeListener(this)
        sw_tiktok_setting.setOnCheckedChangeListener(this)

        tv_exit_login.setOnClickListener {

        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mViewModel.saveSwitchState(buttonView?.id, isChecked)
    }
}