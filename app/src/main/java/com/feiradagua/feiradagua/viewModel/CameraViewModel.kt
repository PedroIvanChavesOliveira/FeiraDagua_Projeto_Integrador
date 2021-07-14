package com.feiradagua.feiradagua.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.CameraRepository
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {
    var getUri: MutableLiveData<Uri> = MutableLiveData()
    private val repository by lazy {
        CameraRepository()
    }

    fun putFileToStorage(uri: Uri) {
        viewModelScope.launch {
            getUri.postValue(repository.putFileToStorage(uri))
        }
    }
}