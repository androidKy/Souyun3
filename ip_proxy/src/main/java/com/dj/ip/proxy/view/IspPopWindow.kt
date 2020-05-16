package com.dj.ip.proxy.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dj.ip.proxy.IspAdapter
import com.dj.ip.proxy.R
import com.dj.ip.proxy.bean.IspBean
import com.dj.ip.proxy.utils.PopUtils

/**
 * description:
 * author:kyXiao
 * date:2020/5/16
 */
class IspPopWindow(val context: Context) {

    private lateinit var mContentView: View
    private lateinit var mPopWindow: PopupWindow

    private lateinit var mOnItemClickListener: OnItemClickListener
    private var mIspDataList: List<IspBean> = arrayListOf<IspBean>().apply {
        add(IspBean("随机", 0, 1))
        add(IspBean("电信", 1,0))
        add(IspBean("联通", 2,0))
        add(IspBean("移动", 3,0))
        add(IspBean("广电", 4,0))
    }
    private var mCurIspBean: IspBean = IspBean("随机", 0, 1)

    init {
        //initView(context)
    }

    private fun initView(context: Context) {
        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_ip_isp, null, false)
        mPopWindow = PopupWindow(
            mContentView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            animationStyle = R.style.AnimBottom
            setBackgroundDrawable(ColorDrawable())
            isOutsideTouchable = false
        }

        mPopWindow.setOnDismissListener {
            PopUtils.setBackgroundAlpha(context, 1f)
        }

        mContentView.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            mOnItemClickListener.onItemClick(mCurIspBean)
            if (mPopWindow.isShowing) {
                mPopWindow.dismiss()
            }
        }
        mContentView.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            if (mPopWindow.isShowing)
                mPopWindow.dismiss()
        }

        val recyclerView = mContentView.findViewById<RecyclerView>(R.id.rcy_isp_list)
        recyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recyclerView.addItemDecoration(SpaceItemDecoration(context))
        recyclerView.adapter = IspAdapter(context, ispDataList = mIspDataList).apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(ispBean: IspBean) {
                    mCurIspBean = ispBean
                    changeCheckStatus(ispBean.ispFlag)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            })
        }



        // mPopWindow.showAsDropDown(mContentView)
    }

    fun show() {
        initView(context)
        PopUtils.setBackgroundAlpha(context, 0.5f)
        mPopWindow?.apply {
            if (!isShowing)
                showAtLocation(mContentView, Gravity.BOTTOM, 0, 0)
        }
    }

    fun dismiss() {
        if (mPopWindow.isShowing)
            mPopWindow?.dismiss()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun changeCheckStatus(ispFlag: Int) {
        mIspDataList.forEach { ispBean ->
            ispBean.status = if (ispBean.ispFlag == ispFlag) 1 else 0
        }
    }

    interface OnItemClickListener {
        fun onItemClick(ispBean: IspBean)
    }
}