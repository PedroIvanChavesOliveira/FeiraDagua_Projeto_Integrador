package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.ProducerProfileRepository
import kotlinx.coroutines.launch

class ProducerProfileViewModel: ViewModel() {
    private val repository by lazy {
        ProducerProfileRepository()
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}