package com.account.manager.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.account.manager.R
import com.account.manager.adapter.AppInfoAdapter
import com.task.cn.jbean.AppInfo
import com.task.cn.util.AppUtils

/**
 * Description:
 * Created by Quinin on 2020-03-25.
 **/
class AppPicker(private val context: Context) {
    private lateinit var popView: View
    private lateinit var popwindow: PopupWindow

    private var onSelectedListener: OnSelectedListener? = null
    private var mSelectedAppInfoList: ArrayList<AppInfo>? = null


    private fun initView() {
        popView = LayoutInflater.from(context).inflate(R.layout.layout_app_picker, null, false)

        val tvCancel = popView.findViewById<TextView>(R.id.tv_cancel)
        val rcyApps = popView.findViewById<RecyclerView>(R.id.rcy_apps)

        this.popwindow = PopupWindow(this.popView, -1, -2)
        this.popwindow.animationStyle = com.lljjcoder.style.citypickerview.R.style.AnimBottom
        this.popwindow.setBackgroundDrawable(ColorDrawable())
        this.popwindow.isTouchable = true
        this.popwindow.isOutsideTouchable = false
        this.popwindow.isFocusable = true
        this.popwindow.setOnDismissListener {

        }

        tvCancel.setOnClickListener {
            if (popwindow.isShowing) {
                popwindow.dismiss()
            }
        }
        val appInfoList = filterAppInfoList()

        rcyApps.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcyApps.addItemDecoration(CommonDecoration())
        rcyApps.adapter = AppInfoAdapter(appInfoList).apply {
            setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                val appInfo = appInfoList[position]
                onSelectedListener?.onSelected(appInfo)
                hidePicker()
            })
        }
    }

    private fun filterAppInfoList(): ArrayList<AppInfo> {
        return arrayListOf<AppInfo>().apply {
            addAll(AppUtils.getUserApps())

            if (mSelectedAppInfoList != null && mSelectedAppInfoList!!.size > 0) {
                val iterator = this.iterator()
                while (iterator.hasNext()) {
                    val appInfo = iterator.next()
                    for (selectedApp in mSelectedAppInfoList!!) {
                        if (appInfo.pkgName == selectedApp.pkgName) {
                            iterator.remove()
                            continue
                        }
                    }
                }
            }
        }

    }

    fun showPicker() {
        initView()
        if (!popwindow.isShowing)
            this.popwindow.showAtLocation(this.popView, 80, 0, 0);
    }

    fun hidePicker() {
        if (popwindow.isShowing)
            popwindow.dismiss()
    }

    fun setOnSelectedListener(onSelectedListener: OnSelectedListener): AppPicker {
        this.onSelectedListener = onSelectedListener
        return this
    }

    fun setSelectedAppList(selectedAppList: ArrayList<AppInfo>): AppPicker {
        this.mSelectedAppInfoList = selectedAppList
        return this
    }

    interface OnSelectedListener {
        fun onSelected(appInfo: AppInfo)
    }
}