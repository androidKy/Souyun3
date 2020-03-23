package com.dj.ip.proxy.proxy

import com.dj.ip.proxy.bean.IpBean

/**
 * description:
 * author:kyXiao
 * date:2020/3/23
 */
interface ProxyRequestListener {
    fun onIpResult(result: Boolean, ipBean: IpBean?)
}