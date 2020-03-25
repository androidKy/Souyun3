package com.android.souyun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.souyun.adapter.DeviceAdapter
import com.google.gson.Gson
import com.safframework.log.L
import com.task.cn.StatusCode
import com.task.cn.StatusMsg
import com.task.cn.jbean.DeviceInfoBean
import com.task.cn.jbean.TaskBean
import com.utils.common.ThreadUtils
import com.utils.common.Utils
import kotlinx.android.synthetic.main.activity_device_info.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class DeviceInfoActivity : AppCompatActivity() {

    private var dataList = ArrayList<String>()
    private var jsonObj: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)

        initData()
    }

    private fun initData() {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<Boolean>() {
            override fun doInBackground(): Boolean {
                try {
                    val inputStream = assets.open("devices_info_new.json")

                    val content = BufferedReader(InputStreamReader(inputStream))
                        .lineSequence()
                        .fold(StringBuilder()) { buff, line -> buff.append(line) }
                        .toString()
                        .replace("\\s+".toRegex(), "")
                    L.d(content)

                    JSONObject(content).apply {
                        for (key in keys()) {
                            dataList.add(key)
                        }
                        jsonObj = this
                    }

                    return true
                } catch (e: Exception) {
                    L.d(e.message)
                }

                return false
            }

            override fun onSuccess(result: Boolean) {
                if (result)
                    initView()
            }

            override fun onCancel() {

            }

            override fun onFail(t: Throwable?) {

            }

        })
    }

    private fun initView() {
        rcy_device.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dataList.subList(0, 30).apply {
            val deviceAdapter = DeviceAdapter(this@DeviceInfoActivity, this)
            deviceAdapter.setOnItemClickListener(DeviceItemClickListener())
            rcy_device.adapter = deviceAdapter
        }
    }

    inner class DeviceItemClickListener : DeviceAdapter.OnItemClickListener {
        override fun onItemClicked(position: Int, key: String) {
            val deviceObj = jsonObj?.getJSONObject(key)
            deviceObj?.apply {
                val deviceBean = Gson().fromJson(this.toString(), DeviceInfoBean::class.java)
                val dataIntent = Intent()
                dataIntent.putExtra("device_key",key)
                dataIntent.putExtra("device_info", deviceBean)
                L.d("device_info: $deviceBean")
                setResult(200,dataIntent)
                finish()
            }
        }
    }
}
