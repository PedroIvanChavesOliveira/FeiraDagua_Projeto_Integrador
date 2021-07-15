package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.UserProfileRepository
import kotlinx.coroutines.launch

class UserProfileViewModel: ViewModel() {
    private val repository by lazy {
        UserProfileRepository()
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}