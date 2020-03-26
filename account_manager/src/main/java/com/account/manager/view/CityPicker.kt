package com.account.manager.view

import android.content.Context
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.lljjcoder.citywheel.CustomConfig
import com.lljjcoder.style.citycustome.CustomCityPicker

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
class CityPicker {
    private val mWheelType = CustomConfig.WheelType.PRO_CITY
    private val mCityData: ArrayList<CustomCityData> = arrayListOf()

    private fun initData() {
        val ahProvince = CustomCityData("340300", "安徽省")
        val fhCity = CustomCityData("340300", "蚌埠市")
        val hzCity = CustomCityData("341600", "亳州市")
        val hyCity = CustomCityData("341200", "阜阳市")
        val hbCity = CustomCityData("340600", "淮北市")
        val hsCity = CustomCityData("341000", "黄山市")
        val laCity = CustomCityData("341500", "六安市")
        val szCity = CustomCityData("341300", "宿州市")
        ahProvince.list = arrayListOf<CustomCityData>().apply {
            add(fhCity)
            add(hzCity)
            add(hyCity)
            add(hbCity)
            add(hsCity)
            add(laCity)
            add(szCity)
        }

        val fjProvince = CustomCityData("", "福建省")
        val fzCity = CustomCityData("350100", "福州市")
        val lyCity = CustomCityData("350800", "龙岩市")
        val smCity = CustomCityData("350400", "三明市")
        val zzCity = CustomCityData("350600", "漳州市")
        fjProvince.list = arrayListOf<CustomCityData>().apply {
            add(fzCity)
            add(lyCity)
            add(smCity)
            add(zzCity)
        }

        val gdProvince = CustomCityData("", "广东省")
        val dgCity = CustomCityData("441900", "东莞市")
        val fsCity = CustomCityData("440600", "佛山市")
        val gzCity = CustomCityData("440100", "广州市")
        val huizhouCity = CustomCityData("441300", "惠州市")
        val jmCity = CustomCityData("440700", "江门市")
        val jyCity = CustomCityData("445200", "揭阳市")
        val mzCity = CustomCityData("441400", "梅州市")
        val stCity = CustomCityData("440500", "汕头市")
        val swCity = CustomCityData("441500", "汕尾市")
        val sgCity = CustomCityData("440200", "韶关市")
        val shenzhenCity = CustomCityData("440300", "深圳市")
        val zjCity = CustomCityData("440800", "湛江市")
        val zsCity = CustomCityData("442000", "中山市")
        gdProvince.list = arrayListOf<CustomCityData>().apply {
            add(dgCity)
            add(fsCity)
            add(gzCity)
            add(huizhouCity)
            add(jmCity)
            add(jyCity)
            add(mzCity)
            add(stCity)
            add(swCity)
            add(sgCity)
            add(sgCity)
            add(shenzhenCity)
            add(zjCity)
            add(zsCity)
        }

        val gzProvince = CustomCityData("", "贵州省")
        val bjCity = CustomCityData("520500", "毕节市")
        val zyCity = CustomCityData("520300", "遵义市")
        gzProvince.list = arrayListOf<CustomCityData>().apply {
            add(bjCity)
            add(zyCity)
        }

        val hkProvince = CustomCityData("", "海南省")
        val hkCity = CustomCityData("460100", "海口市")
        hkProvince.list = arrayListOf<CustomCityData>().apply {
            add(hkCity)
        }

        val hbProvince = CustomCityData("", "河北省")
        val bdCity = CustomCityData("130600", "保定市")
        val czCity = CustomCityData("130900", "沧州市")
        val gdCity = CustomCityData("130400", "邯郸市")
        val lfCity = CustomCityData("131000", "廊坊市")
        val qhdCity = CustomCityData("130300", "秦皇岛市")
        val sjzCity = CustomCityData("130100", "石家庄市")
        val tsCity = CustomCityData("130200", "唐山市")
        val xtCity = CustomCityData("130500", "邢台市")
        hbProvince.list = arrayListOf<CustomCityData>().apply {
            add(bdCity)
            add(czCity)
            add(gdCity)
            add(lfCity)
            add(qhdCity)
            add(sjzCity)
            add(tsCity)
            add(xtCity)
        }

        val hnProvince = CustomCityData("", "河南省")
        val ayCity = CustomCityData("410500", "安阳市")
        val luoyangCity = CustomCityData("410300", "洛阳市")
        val pdsCity = CustomCityData("410400", "平顶山市")
        val sqCity = CustomCityData("411400", "商丘市")
        val xyCity = CustomCityData("411500", "信阳市")
        val xcCity = CustomCityData("411000", "许昌市")
        val zkCity = CustomCityData("411600", "周口市")
        val zmdCity = CustomCityData("411700", "驻马店市")
        hnProvince.list = arrayListOf<CustomCityData>().apply {
            add(ayCity)
            add(luoyangCity)
            add(pdsCity)
            add(sqCity)
            add(xyCity)
            add(xcCity)
            add(zkCity)
            add(zmdCity)
        }

        val hljProvince = CustomCityData("", "黑龙江省")
        val dqCity = CustomCityData("230600", "大庆市")
        val hebCity = CustomCityData("230100", "哈尔滨市")
        val jmsCity = CustomCityData("230800", "佳木斯市")
        hljProvince.list = arrayListOf<CustomCityData>().apply {
            add(dqCity)
            add(hebCity)
            add(jmsCity)
        }

        val hubeiProvince = CustomCityData("", "湖北省")
        val esCity = CustomCityData("422800", "恩施市")
        val jzCity = CustomCityData("421000", "荆州市")
        val whCity = CustomCityData("420100", "武汉市")
        hubeiProvince.list = arrayListOf<CustomCityData>().apply {
            add(esCity)
            add(jzCity)
            add(whCity)
        }

        val hunanProvince = CustomCityData("", "湖南省")
        val hengyangCity = CustomCityData("430400", "衡阳市")
        val syCity = CustomCityData("430500", "邵阳市")
        val yzCity = CustomCityData("431100", "永州市")
        val csCity = CustomCityData("430100", "长沙市")
        val zhuzhouCity = CustomCityData("430200", "株洲市")
        hunanProvince.list = arrayListOf<CustomCityData>().apply {
            add(hengyangCity)
            add(syCity)
            add(yzCity)
            add(csCity)
            add(zhuzhouCity)
        }

        val jlProvince = CustomCityData("", "吉林省").apply {
            list = arrayListOf<CustomCityData>().run {
                this.add(CustomCityData("220100", "长春市"))
                this
            }
        }

        val jsProvince = CustomCityData("", "江苏省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("320400", "常州市"))
                add(CustomCityData("320700", "连云港市"))
                add(CustomCityData("320500", "苏州市"))
                add(CustomCityData("320200", "无锡市"))
                add(CustomCityData("320300", "徐州市"))
                add(CustomCityData("320900", "盐城市"))
                this
            }
        }

        val jxProvince = CustomCityData("", "江西省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("361000", "抚州市"))
                add(CustomCityData("360700", "赣州市"))
                add(CustomCityData("360800", "吉安市"))
                add(CustomCityData("360400", "九江市"))
                add(CustomCityData("360900", "宜春市"))
                this
            }
        }

        val llProvince = CustomCityData("", "辽宁省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("210300", "鞍山市"))
                add(CustomCityData("211300", "朝阳市"))
                add(CustomCityData("210200", "大连市"))
                add(CustomCityData("210700", "锦州市"))
                add(CustomCityData("211000", "辽阳市"))
                add(CustomCityData("211100", "盘锦市"))
                add(CustomCityData("210100", "沈阳市"))
                this
            }
        }

        val sdProvince = CustomCityData("", "山东省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("371600", "滨州市"))
                add(CustomCityData("371400", "德州市"))
                add(CustomCityData("371700", "菏泽市"))
                add(CustomCityData("370800", "济宁市"))
                add(CustomCityData("371500", "聊城市"))
                add(CustomCityData("371300", "临沂市"))
                add(CustomCityData("370200", "青岛市"))
                add(CustomCityData("370900", "泰安市"))
                add(CustomCityData("370700", "潍坊市"))
                add(CustomCityData("370600", "烟台市"))
                this
            }
        }

        val sxProvince = CustomCityData("", "山西省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("140800", "运城市"))
                this
            }
        }
        val shanxiProvince = CustomCityData("", "陕西省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("610900", "安康市"))
                add(CustomCityData("610100", "西安市"))
                this
            }
        }

        val shProvince = CustomCityData("", "上海市").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("310000", "上海市"))
                this
            }
        }

        val scProvince = CustomCityData("", "四川省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("510100", "成都市"))
                this
            }
        }

        val tjProvince = CustomCityData("", "天津市").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("120000", "天津市"))
                this
            }
        }

        val zjProvince = CustomCityData("", "浙江省").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("330100", "杭州市"))
                add(CustomCityData("330700", "金华市"))
                add(CustomCityData("330200", "宁波市"))
                add(CustomCityData("330600", "绍兴市"))
                add(CustomCityData("331000", "台州市"))
                add(CustomCityData("330300", "温州市"))
                this
            }
        }
        val cqProvince = CustomCityData("", "重庆市").apply {
            list = arrayListOf<CustomCityData>().run {
                add(CustomCityData("120000", "重庆市"))
                this
            }
        }

        mCityData.apply {
            add(jxProvince)
            add(llProvince)
            add(jlProvince)
            add(hunanProvince)
            add(hubeiProvince)
            add(ahProvince)
            add(fjProvince)
            add(gdProvince)
            add(gzProvince)
            add(hbProvince)
            add(hkProvince)
            add(hljProvince)
            add(hnProvince)
            add(cqProvince)
            add(zjProvince)
            add(tjProvince)
            add(scProvince)
            add(shProvince)
            add(sxProvince)
            add(shanxiProvince)
            add(sdProvince)
        }
    }

    fun showCityPicker(context: Context, itemClickListener: OnCustomCityPickerItemClickListener) {
        initData()
        val config = CustomConfig.Builder()
            .title("选择城市")
            .visibleItemsCount(5)
            .setCityData(mCityData)
            .provinceCyclic(false)
            .cityCyclic(false)
            .districtCyclic(false)
            .drawShadows(true)
            .setCityWheelType(mWheelType)
            .build()
        CustomCityPicker(context).apply {
            setCustomConfig(config)
            setOnCustomCityPickerItemClickListener(itemClickListener)
            showCityPicker()
        }
    }
}