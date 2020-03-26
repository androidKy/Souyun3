package com.account.manager.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Description:
 * Created by Quinin on 2020-03-12.
 **/
abstract class BaseViewModelFragment<VM : ViewModel> : BaseFragment() {

    fun getViewModel(vmClass: Class<VM>): VM {
        return ViewModelProvider(this).get(vmClass)
    }

    override fun initListener() {

    }
}