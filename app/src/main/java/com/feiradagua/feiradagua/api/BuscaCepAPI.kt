package com.feiradagua.feiradagua.api

import com.feiradagua.feiradagua.model.cep.ViaCep
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BuscaCepAPI {
    @GET("ws/{cep}/json")
    suspend fun getCep(
            @Path("cep") cep: Int
    ): Response<ViaCep>
}