package com.feiradagua.feiradagua.api

import com.feiradagua.feiradagua.utils.Constants.BuscaCepAPI.VIA_CEP_BASE_URL
import com.feiradagua.feiradagua.utils.Constants.Notification.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    val viaCepApi: BuscaCepAPI = getViaCepClient().create(BuscaCepAPI::class.java)
    private fun getViaCepClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(VIA_CEP_BASE_URL)
            .client(getViaCepInterceptor())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getViaCepInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptor = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)

        return interceptor.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(getViaCepInterceptor())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api by lazy {
        retrofit.create(NotificationAPI::class.java)
    }
}