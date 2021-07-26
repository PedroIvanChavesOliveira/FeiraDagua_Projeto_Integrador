package com.feiradagua.feiradagua.viewModel

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.CameraRepository
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File

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

    fun putFileToStorageProducts(uri: Uri, id: String) {
        viewModelScope.launch {
            getUri.postValue(repository.putFileToStorageProducts(uri, id))
        }
    }
}