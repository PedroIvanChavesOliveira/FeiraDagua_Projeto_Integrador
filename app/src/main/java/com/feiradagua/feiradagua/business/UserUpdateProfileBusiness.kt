package com.feiradagua.feiradagua.business

import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.cep.ViaCep
import com.feiradagua.feiradagua.repository.UserUpdateProfileRepository

class UserUpdateProfileBusiness {
    private val repository by lazy {
        UserUpdateProfileRepository()
    }

    suspend fun viaCepResponse(cep: Int): ResponseAPI {
        val response = repository.viaCepResponse(cep)

        return if(response is ResponseAPI.Success) {
            val viaCep = response.data as ViaCep
            ResponseAPI.Success(viaCep)
        }else {
            response
        }
    }
}