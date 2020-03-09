package com.android.souyun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.souyun.adapter.ProxyInfoAdapter
import kotlinx.android.synthetic.main.activity_proxy_info.*

class ProxyInfoActivity : AppCompatActivity() {

    companion object {
        val CITY_ARRAY = arrayOf(
            "江西省-赣州市", "广东省-广州市", "广东省-佛山市", "山东省-泰安市", "河北省-保定市", "安徽省-宿州市",
            "河南省-周口市", "广东省-汕头市", "山东省-潍坊市", "山东省-菏泽市", "河南省-商丘市", "浙江省-台州市", "海南省-海口市", "河南省-驻马店市",
            "广东省-深圳市", "辽宁省-大连市", "广东省-揭阳市", "安徽省-阜阳市", "河南省-平顶山市"
        )
        val CODE_ARRAY = arrayOf(
            "360700",
            "440100",
            "440600",
            "370900",
            "130600",
            "341300",
            "411600",
            "440500",
            "370700",
            "371700",
            "411400",
            "331000",
            "460100",
            "411700",
            "440300",
            "210200",
            "445200",
            "341200",
            "410400"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proxy_info)

        initView()
    }


    private fun initView() {
        rcy_proxy.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val proxyAdatper = ProxyInfoAdapter(CITY_ARRAY, CODE_ARRAY)
        proxyAdatper.setOnItemClickListener(ProxyOnItemClickListener())
        rcy_proxy.adapter = proxyAdatper

    }

    inner class ProxyOnItemClickListener : ProxyInfoAdapter.OnItemClickListener {

        override fun onItemClicked(cityName: String, cityCode: String) {
            val intent = Intent()
            intent.putExtra("cityName", cityName)
            intent.putExtra("cityCode", cityCode)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}
