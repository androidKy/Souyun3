package com.account.manager.ui.add

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import butterknife.OnClick
import com.account.manager.LoginConstant
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import com.task.cn.SPConstant
import com.utils.common.SPUtils
import com.utils.common.ToastUtils
import com.utils.common.Utils
import kotlinx.android.synthetic.main.fragment_add.*
import org.angmarch.views.OnSpinnerItemSelectedListener

class AddAccountFragment : BaseViewModelFragment<AddAccountViewModel>() {

    private lateinit var mViewModel: AddAccountViewModel

    override fun getViewId(): Int {
        return R.layout.fragment_add
    }

    override fun initData() {
        mViewModel = getViewModel(AddAccountViewModel::class.java).apply {
            commitResult.observe(this@AddAccountFragment, Observer {
                tv_add_account_tip.text =
                    if (it) resources.getString(R.string.add_account_succeed)
                    else resources.getString(R.string.add_account_failed)

                hideDialog()
            })
        }

        val platformDatas = arrayListOf("请选择应用平台", "微信", "抖音", "快手", "京东", "拼多多")
        nice_spinner.attachDataSource(platformDatas)
    }

    @OnClick(R.id.bt_commit_account)
    fun commitAccount() {
        val userid =
            SPUtils.getInstance(LoginConstant.SP_LOGIN_INFO).getString(LoginConstant.KEY_USER_ID)
        if (userid.isNullOrEmpty()) {
            tv_add_account_tip.text = resources.getString(R.string.unlogin_tip)
            ToastUtils.showToast(Utils.getApp(), resources.getString(R.string.unlogin_tip))
            return
        }
        val deviceId =
            SPUtils.getInstance(SPConstant.SP_DEVICE_INFO).getString(SPConstant.KEY_DEVICE_ID)
        if (deviceId.isNullOrEmpty()) {
            tv_add_account_tip.text = resources.getString(R.string.unchanged_tip)
            ToastUtils.showToast(Utils.getApp(), resources.getString(R.string.unchanged_tip))
            return
        }

        val platform: String = nice_spinner.selectedItem as String
        val accountName: String = et_account.text.toString()
        val accountPsw: String = et_psw.text.toString()

        if (platform == "请选择应用平台") {
            ToastUtils.showToast(Utils.getApp(), "请选择应用平台")
            return
        }
        if (accountName.isEmpty()) {
            et_account.error = "账号不能为空"
            return
        }
        if (accountPsw.isEmpty()) {
            et_psw.error = "密码不能为空"
            return
        }

        activity?.apply {
            AlertDialog.Builder(this)
                .setTitle("添加账号")
                .setMessage("确定添加账号($accountName)吗?")
                .setNegativeButton(
                    "取消"
                ) { dialog, which -> dialog.dismiss() }
                .setPositiveButton("确定")
                { dialog, which ->
                    dialog.dismiss()
                    showDialog()
                    mViewModel.commitAccount(platform, accountName, accountPsw)
                }.create().show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}