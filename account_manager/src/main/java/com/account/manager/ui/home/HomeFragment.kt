package com.account.manager.ui.home

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.account.manager.R
import com.account.manager.base.BaseViewModelFragment
import com.account.manager.tableview.TableViewAdapter
import com.account.manager.tableview.TableViewListener
import com.evrencoskun.tableview.filter.Filter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseViewModelFragment<HomeViewModel>() {

    private lateinit var mViewModel: HomeViewModel


    override fun getViewId(): Int {
        return R.layout.fragment_home
    }


    override fun initData() {
        mViewModel = getViewModel(HomeViewModel::class.java)
            .apply {
                tableViewModel.observe(this@HomeFragment, Observer {
                    refreshLayout.finishRefresh()

                    if (it.cellList == null || it.cellList.size == 0) {
                        et_search.visibility = View.GONE
                        table_view.visibility = View.GONE
                        ll_account_tip.visibility = View.VISIBLE
                        return@Observer
                    }
                    et_search.visibility = View.VISIBLE
                    table_view.visibility = View.VISIBLE
                    ll_account_tip.visibility = View.GONE

                    val tableViewAdapter = TableViewAdapter(it)
                    table_view.adapter = tableViewAdapter
                    table_view.tableViewListener = TableViewListener(table_view)

                    tableViewAdapter.setAllItems(it.columnHeaderList, it.rowHeaderList, it.cellList)

                    val tableViewFilter = Filter(table_view)
                    et_search.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {

                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            tableViewFilter.set(s.toString())
                        }
                    })
                })
            }

        refreshData()
    }

    private fun refreshData() {
        refreshLayout.setOnRefreshListener {
            mViewModel.getAccountData()
            //it.finishRefresh(2000)
        }
        refreshLayout.setEnableLoadMore(false)
        //refreshLayout.autoRefresh()

        ll_account_tip.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                refreshLayout.autoRefresh()
            }
        })
    }
}