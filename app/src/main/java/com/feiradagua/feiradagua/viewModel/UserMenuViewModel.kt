package com.feiradagua.feiradagua.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.repository.UserMenuRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class UserMenuViewModel(): ViewModel() {
    var userInfo: MutableLiveData<User?> = MutableLiveData()
    var producerList: MutableLiveData<MutableList<Producer>?> = MutableLiveData()
    private val repository by lazy {
        UserMenuRepository()
    }

    fun getUserDB(/*lastModified: String*/) {
        viewModelScope.launch {
            repository.getUserDb()?.let {
                userInfo.postValue(it.toObject(User::class.java))
            }?: run {
                userInfo.postValue(null)
            }
        }
    }

    fun updateToken() {
        viewModelScope.launch {
            repository.updateToken()
        }
    }

    fun getProducers(deliveryArea: String, /*lastDate: String,*/ context: Context, id: String) {
        viewModelScope.launch {
            producerList.postValue(repository.getProducers(deliveryArea, context, id))
        }
    }

//    fun insertUserDateInRoom(context: Context) {
//        viewModelScope.launch {
//            repository.insertUserDateInRoom(context)
//        }
//    }

}