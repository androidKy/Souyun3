package com.account.manager.ui.phone

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import butterknife.OnClick
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import com.account.manager.ui.phone.MultiSetupActivity.Companion.MULTI_SETUP_CODE
import com.utils.common.ToastUtils
import com.utils.common.Utils
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
                    hideDialog()
                    tv_setup_tip.text = t
                })
            phoneModel.observe(this@PhoneSetupFragment,
                Observer<String> {t -> tv_phone_type.text = t})
            phoneImei.observe(this@PhoneSetupFragment,
                Observer<String> {t -> tv_phone_imei.text = t})
            ip.observe(this@PhoneSetupFragment,
                Observer<String> {t -> tv_ip_value.text = t})
            ipCity.observe(this@PhoneSetupFragment,
                Observer<String> {t -> tv_ip_city.text = t})
            appList.observe(this@PhoneSetupFragment,
                Observer<String> {t -> tv_apps.text = t})

            initPhoneInfo()
        }
    }

    @OnClick(R.id.bt_phone_setup)
    fun phoneSetup() {
        //对所有用户应用进行改机和切换IP
        //弹框提示正在修改
        showDialog()
        mViewModel.setupPhone()
    }

    @OnClick(R.id.bt_multi_setup)
    fun multiSetup() {
        activity?.apply {
            startActivityForResult(Intent(this, MultiSetupActivity::class.java), MULTI_SETUP_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MULTI_SETUP_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}