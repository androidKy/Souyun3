package com.account.manager.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.account.manager.R
import com.utils.common.SPUtils

class SettingViewModel : ViewModel() {
    fun saveSwitchState(id: Int?, checked: Boolean) {
        when (id) {
            R.id.sw_wx_setting -> {
                SPUtils.getInstance(com.android.xplugin.util.XSPUtils.DATA_HOOK_SP)
                    .put(com.android.xplugin.util.XSPUtils.LOGIN_WECHAT_KEY, checked)
            }
            R.id.sw_jd_setting -> {

            }
            R.id.sw_ks_setting -> {

            }
            R.id.sw_pdd_setting -> {

            }
            else -> {

            }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is send Fragment"
    }
    val text: LiveData<String> = _text
}