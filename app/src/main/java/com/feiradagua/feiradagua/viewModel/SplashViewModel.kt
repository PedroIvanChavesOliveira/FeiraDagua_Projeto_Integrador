package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.repository.SplashRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel: ViewModel() {
    var userOk: MutableLiveData<FirebaseUser?> = MutableLiveData()
    var registeredOk: MutableLiveData<Registered> = MutableLiveData()
    private val repository by lazy {
        SplashRepository()
    }

    fun getUser() {
        viewModelScope.launch {
            repository.getUser()?.let {
                userOk.postValue(it)
            }?: run {
                userOk.postValue(null)
            }
        }
    }

    fun getRegisteredDB() {
        viewModelScope.launch {
            repository.getRegisteredDb()?.let {
                registeredOk.postValue(it.toObject(Registered::class.java))
            }?: run {
                registeredOk.postValue(null)
            }
        }
    }
}