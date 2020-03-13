package com.account.manager.ui.phone

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.widget.RelativeLayout
import butterknife.OnClick
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import com.account.manager.ui.phone.MultiSetupActivity.Companion.MULTI_SETUP_CODE
import com.utils.common.ToastUtils
import com.utils.common.Utils

class PhoneSetupFragment : BaseViewModelFragment<PhoneSetupViewModel>() {

    override fun getViewId(): Int {
        return R.layout.fragment_phone_setup
    }

    override fun initData() {
        getViewModel(PhoneSetupViewModel::class.java).apply {

        }
    }

    @OnClick(R.id.bt_phone_setup)
    fun phoneSetup() {
        //对所有用户应用进行改机和切换IP
        //弹框提示正在修改
        context?.apply {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(R.layout.dialog_setup_tip)
                .create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(null)
        }
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