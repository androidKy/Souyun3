package com.account.manager.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.account.manager.model.BaseData
import com.account.manager.model.CommitAccountResult
import com.account.manager.net.NetManager
import com.account.manager.net.RequestListener

class AddAccountViewModel : ViewModel() {

    private val _commitResult = MutableLiveData<Boolean>()
    val commitResult: LiveData<Boolean> = _commitResult


    fun commitAccount(platform: String, accountName: String, psw: String) {
        NetManager()
            .setRequestListener(object : RequestListener {
                override fun onSucceed(result: BaseData) {
                    val commitAccountResult = result as CommitAccountResult
                    _commitResult.value = commitAccountResult.ret == 200
                }

                override fun onError(msg: String) {
                    _commitResult.value = false
                }
            })
            .commitAccount(platform, accountName, psw)
    }
}