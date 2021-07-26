package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.NewProductRepository
import kotlinx.coroutines.launch

class NewProductViewModel: ViewModel() {
    var deleteDone: MutableLiveData<Boolean> = MutableLiveData()
    private val repository by lazy {
        NewProductRepository()
    }

    fun deleteById(id: String, photo: String) {
        viewModelScope.launch {
            deleteDone.postValue(repository.deleteById(id, photo))
        }
    }
}