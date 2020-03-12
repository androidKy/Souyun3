package com.account.manager.ui.home

import androidx.lifecycle.Observer
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseViewModelFragment<HomeViewModel>() {

    private lateinit var mViewModel: HomeViewModel

    override fun getViewId(): Int {
        return R.layout.fragment_home
    }


    override fun initData() {
        mViewModel = getViewModel(HomeViewModel::class.java)
            .apply {
                text.observe(viewLifecycleOwner, Observer {
                    text_home.text = it
                })
            }
    }


}