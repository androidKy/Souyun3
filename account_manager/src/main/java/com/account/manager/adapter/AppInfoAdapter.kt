package com.account.manager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.account.manager.R
import com.task.cn.jbean.AppInfo

/**
 * Description:
 * Created by Quinin on 2020-03-25.
 **/
class AppInfoAdapter(private val appInfoList: List<AppInfo>) :
    RecyclerView.Adapter<AppInfoAdapter.AppViewHolder>() {

    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_app_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appInfoList.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        appInfoList[position].apply {
            holder.run {
                ivIcon.setImageDrawable(this@apply.iconDrawable)
                tvName.text = this@apply.appName
                tvPkg.text = this@apply.pkgName
                tvVersion.text = this@apply.versionName
            }
        }

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(null, it, position, it.id.toLong())

        }
    }


    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.iv_app_icon)
        val tvName: TextView = view.findViewById(R.id.tv_app_name)
        val tvPkg: TextView = view.findViewById(R.id.tv_app_pkg)
        val tvVersion: TextView = view.findViewById(R.id.tv_app_version)
    }

    fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }
}

