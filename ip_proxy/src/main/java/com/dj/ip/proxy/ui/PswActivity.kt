package com.dj.ip.proxy.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.dj.ip.proxy.Constants
import com.dj.ip.proxy.R
import com.dj.ip.proxy.base.BaseActivity
import com.utils.common.SPUtils
import com.utils.common.ToastUtils
import kotlinx.android.synthetic.main.activity_launch.*

class PswActivity : BaseActivity(), View.OnClickListener {

    private var mPsw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        iv_back.setOnClickListener(this)

        initData()
    }

    private fun initData() {
        intent?.getStringExtra(Constants.PSW_KEY)?.apply {
            if (this.isNotEmpty()) {
                mPsw = this
                et_psw.setText(this)
            }
        }?:et_psw.setText(SPUtils.getInstance(Constants.IP_PROXY_SP).getString(Constants.PSW_KEY))

        et_psw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                if (mPsw.isNotEmpty()) {
                    savePsw()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra(Constants.PSW_KEY, et_psw.text.toString())
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }
        }
    }


    fun login(view: View) {
        if (et_psw.text.toString().isEmpty()) {
            ToastUtils.showToast(this, "密钥不能为空")
            return
        }
        savePsw()
        if(mPsw.isNotEmpty())
        {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(Constants.PSW_KEY, et_psw.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
        }else{
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(Constants.PSW_KEY, et_psw.text.toString())
            })
        }
        finish()
    }

    private fun savePsw() {
        val psw = et_psw.text.toString()
        SPUtils.getInstance(Constants.IP_PROXY_SP).put(Constants.PSW_KEY, psw)
    }
}
