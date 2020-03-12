package com.account.manager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Description:
 * Created by Quinin on 2020-03-12.
 **/
abstract class BaseFragment : Fragment() {

    private lateinit var unBinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getViewId(), container, false)
        unBinder = ButterKnife.bind(this, view)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    abstract fun getViewId(): Int

    abstract fun initData()

    override fun onDestroyView() {
        super.onDestroyView()
        unBinder.unbind()
    }
}