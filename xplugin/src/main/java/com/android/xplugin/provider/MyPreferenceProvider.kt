package com.android.xplugin.provider

import com.android.xplugin.util.XSPUtils.Companion.DATA_HOOK_SP
import com.android.xplugin.util.XSPUtils.Companion.SP_AUTHORITY
import com.crossbowffs.remotepreferences.RemotePreferenceProvider

/**
 * description:
 * author:kyXiao
 * date:2020/3/31
 */
class MyPreferenceProvider : RemotePreferenceProvider {
    constructor() : super(SP_AUTHORITY, arrayOf(DATA_HOOK_SP))

}