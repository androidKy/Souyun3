package com.account.manager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.account.manager.R
import com.safframework.log.L

/**
 * Description:
 * Created by Quinin on 2020-03-12.
 **/
abstract class BaseFragment : Fragment() {

    private lateinit var unBinder: Unbinder
    private var mDialog: AlertDialog? = null

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

    override fun onStart() {
        super.onStart()
        initData()
        initListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    abstract fun getViewId(): Int

    abstract fun initData()

    abstract fun initListener()


    override fun onDestroyView() {
        super.onDestroyView()
        unBinder.unbind()
    }

    fun showDialog() {
        L.d("展示弹框")
        try {
            activity?.apply {
                if(mDialog != null && !mDialog!!.isShowing)
                {
                    mDialog?.dismiss()
                    mDialog = null
                }
                mDialog = AlertDialog.Builder(this)
                    .setView(R.layout.dialog_setup_tip)
                    .create().apply {
                        setCanceledOnTouchOutside(false)
                        show()
                        window?.setBackgroundDrawable(null)
                    }
            }
        } catch (e: Exception) {
        }
    }

    fun hideDialog() {
        L.d("隐藏弹框")
        try {
            activity?.apply {
                mDialog?.let {
                    it.hide()
                    it.dismiss()
                    mDialog = null
                }
            }
        } catch (e: Exception) {
        }
    }
}