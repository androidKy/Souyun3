package com.account.manager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.account.manager.model.Account
import com.account.manager.model.AccountsModel
import com.account.manager.model.BaseData
import com.account.manager.net.NetManager
import com.account.manager.net.RequestListener
import com.account.manager.tableview.TableViewModel
import com.account.manager.tableview.model.Cell
import com.account.manager.tableview.model.ColumnHeader
import com.account.manager.tableview.model.RowHeader
import com.task.cn.getPlatformNameByInt
import com.utils.common.ThreadUtils

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _tableViewModel = MutableLiveData<TableViewModel>()
    val tableViewModel: LiveData<TableViewModel> = _tableViewModel

    fun getAccountData() {
        NetManager()
            .setRequestListener(object : RequestListener {
                override fun onSucceed(result: BaseData) {
                    val accountsModel = result as AccountsModel
                    if (accountsModel.ret == 200 && accountsModel.data.lists.size > 0) {
                        val accountList = accountsModel.data.lists

                        setAccountData(accountList)
                    } else {
                        _tableViewModel.value = TableViewModel()
                    }
                }

                override fun onError(msg: String) {
                    _tableViewModel.value = TableViewModel()
                }
            })
            .getAccountData()
    }

    private fun setAccountData(accountList: List<Account>) {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<TableViewModel>() {
            override fun doInBackground(): TableViewModel {
                return TableViewModel().apply {
                    this.accountList = accountList.reversed()
                    columnHeaderList = this@HomeViewModel.getColumnHeaderList()
                    rowHeaderList = this@HomeViewModel.getRowHeaderList(accountList)
                    cellList = this@HomeViewModel.getCellList(accountList)
                }
            }

            override fun onSuccess(result: TableViewModel) {
                _tableViewModel.value = result
            }

            override fun onCancel() {
            }

            override fun onFail(t: Throwable?) {
                _tableViewModel.value = TableViewModel()
            }
        })
    }

    private fun getColumnHeaderList(): List<ColumnHeader> {
        return ArrayList<ColumnHeader>().apply {
            add(ColumnHeader("1", "应用平台"))
            add(ColumnHeader("2", "账号"))
            add(ColumnHeader("3", "密码"))
            add(ColumnHeader("4", "登录状态"))
            add(ColumnHeader("5", "登录日期"))
            add(ColumnHeader("6", "登录城市"))
            add(ColumnHeader("7", "登录设备"))
        }
    }

    private fun getRowHeaderList(accountList: List<Account>): List<RowHeader> {
        return ArrayList<RowHeader>().apply {
            for (account in accountList) {
                add(RowHeader(account.id.toString(), account.id.toString()))
            }
            reverse()
        }
    }

    private fun getCellList(accountList: List<Account>): List<List<Cell>> {
        return ArrayList<List<Cell>>().apply {
            for (account in accountList) {
                val cellList = arrayListOf<Cell>()
                for (column in getColumnHeaderList().indices) {
                    when (column) {
                        0 -> cellList.add(
                            Cell(
                                column.toString(),
                                getPlatformNameByInt(account.platform.toInt())
                            )
                        )
                        1 -> cellList.add(Cell(column.toString(), account.account))
                        2 -> cellList.add(Cell(column.toString(), account.password))
                        3 -> cellList.add(
                            Cell(
                                column.toString(),
                                if (account.status == 0) "未登录" else "正使用"
                            )
                        )
                        4 -> cellList.add(Cell(column.toString(), account.login_info.login_date))
                        5 -> cellList.add(Cell(column.toString(), account.login_info.city))
                        6 -> cellList.add(
                            Cell(
                                column.toString(),
                                "${account.deviceInfoBean.manufacturer}-${account.deviceInfoBean.model}"
                            )
                        )
                    }
                }

                add(cellList)
            }
            reverse()
        }
    }
}