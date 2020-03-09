package com.android.souyun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.souyun.R

/**
 * Description:
 * Created by Quinin on 2020-03-09.
 **/
class ProxyInfoAdapter(private val cityArray: Array<String>, private val codeArray: Array<String>) :
    RecyclerView.Adapter<ProxyInfoAdapter.ProxyViewHolder>() {

    private var onItemClickListener: ProxyInfoAdapter.OnItemClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProxyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_proxy_layout, parent, false)

        return ProxyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityArray.size
    }

    override fun onBindViewHolder(holder: ProxyViewHolder, position: Int) {
        holder.tvCityCode.text = codeArray[position]
        holder.tvCityName.text = cityArray[position]

        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                onItemClickListener?.onItemClicked(cityArray[position],codeArray[position])
            }
        })
    }


    class ProxyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCityCode: TextView = itemView.findViewById(R.id.tv_cityCode)
        var tvCityName: TextView = itemView.findViewById(R.id.tv_cityName)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClicked(cityName: String, cityCode: String)
    }
}