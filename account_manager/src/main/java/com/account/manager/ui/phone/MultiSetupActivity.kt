package com.account.manager.ui.phone

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.account.manager.R
import kotlinx.android.synthetic.main.activity_multi_setup.*


class MultiSetupActivity : AppCompatActivity() {

    companion object {
        const val MULTI_SETUP_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }

        setContentView(R.layout.activity_multi_setup)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title  = resources.getString(R.string.bt_multi_setup)
        }
        //todo 选择性改机
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {

            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
