package com.dj.ip.proxy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dj.ip.proxy.bean.IspBean
import com.dj.ip.proxy.view.IspPopWindow

/**
 * description:
 * author:kyXiao
 * date:2020/5/16
 */
class IspAdapter(private val context: Context, private val ispDataList: List<IspBean>) :
    RecyclerView.Adapter<IspAdapter.IspViewHolder>() {

    private var onItemClickListener: IspPopWindow.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IspViewHolder {
        return IspViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.pop_isp_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IspViewHolder, position: Int) {
        ispDataList[position].let {
            holder.ivCheckStatus.visibility = if (it.status == 1) View.VISIBLE else View.GONE
        }

        holder.tvIspName.text = ispDataList[position].ispName

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(
                ispDataList[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return ispDataList.size
    }

    inner class IspViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIspName = view.findViewById<TextView>(R.id.tv_isp_name)
        val ivCheckStatus = view.findViewById<ImageView>(R.id.iv_check_status)
    }

    fun setOnItemClickListener(onItemClickListener: IspPopWindow.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}