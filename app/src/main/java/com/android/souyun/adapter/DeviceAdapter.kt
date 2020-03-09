package com.android.souyun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.souyun.R

/**
 * Description:
 * Created by Quinin on 2020-03-07.
 **/
class DeviceAdapter(val context: Context, val dataList: List<String>) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private var onItemClickListener:OnItemClickListener?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_device_layout, parent, false)

        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.tvDeviceKey.text = dataList[position]


        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                onItemClickListener?.onItemClicked(position,dataList[position])
            }
        })
    }


    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDeviceKey: TextView = itemView.findViewById(R.id.tv_key)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, key: String)
    }
}