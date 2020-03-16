package com.account.manager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.account.manager.LoginConstant.Companion.REQUEST_LOGIN_CODE
import com.account.manager.ui.login.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.safframework.log.L
import com.utils.common.SPUtils

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mLoginSP: SPUtils
    private var mLoginStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_add, R.id.nav_phone_setup,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initLogin(navView)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_home -> {
                    L.d("home")
                }
            }
        }
    }

    private fun initLogin(navView: NavigationView) {
        mLoginSP = SPUtils.getInstance(LoginConstant.SP_LOGIN_INFO)
        //val userName = mLoginSP.getString(LoginConstant.KEY_USER_NAME)
        val userPhone = mLoginSP.getString(LoginConstant.KEY_USER_PHONE)

        val headerView = navView.getHeaderView(0)
        headerView.run {
            findViewById<ImageView>(R.id.iv_user_pic).let {
                it.setOnClickListener(this@MainActivity)
            }

            /* findViewById<TextView>(R.id.tv_user_name).let {
                 it.tipSetup = "用户名"
             }*/

            findViewById<TextView>(R.id.tv_user_phone).let {
                it.text =
                    if (userPhone.isEmpty()) resources.getString(R.string.default_user_phone) else userPhone
            }
        }

        if (mLoginSP.getString(LoginConstant.KEY_USER_TOKEN).isNotEmpty()) {
            mLoginStatus = true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_user_pic -> {
                if (!mLoginStatus) {
                    startActivityForResult(
                        Intent(this, LoginActivity::class.java),
                        REQUEST_LOGIN_CODE
                    )
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                initLogin(findViewById(R.id.nav_view))
            }
        }
    }
}
