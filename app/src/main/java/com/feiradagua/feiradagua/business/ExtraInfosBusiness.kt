package com.feiradagua.feiradagua.business

import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.model.cep.ViaCep
import com.feiradagua.feiradagua.repository.ExtraInfosRepository

class ExtraInfosBusiness {
    private val repository by lazy {
        ExtraInfosRepository()
    }

    suspend fun getUserDB(): String? {
        val photo = repository.getUserDB().toObject(User::class.java)
        return photo?.photo
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