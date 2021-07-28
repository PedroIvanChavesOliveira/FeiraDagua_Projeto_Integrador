package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    var userOk: MutableLiveData<FirebaseUser?> = MutableLiveData()
    var dbOk: MutableLiveData<DocumentSnapshot> = MutableLiveData()
    var registeredOk: MutableLiveData<Registered?> = MutableLiveData()
    val token: MutableLiveData<String> = MutableLiveData()

    private val repository by lazy {
        LoginRepository()
    }

    fun getUser() {
        viewModelScope.launch {
            userOk.postValue(repository.getUser())
        }
    }

    fun getUserDb() {
        viewModelScope.launch {
            dbOk.postValue(repository.getUserDb())
        }
    }

    fun getToken() {
        viewModelScope.launch {
            token.postValue(repository.getToken())
        }
    }

    fun addUserOnDataBase(user: User) {
        viewModelScope.launch {
            repository.addUserOnDataBase(user)
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