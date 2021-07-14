package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.api.APIService
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.REGISTERED_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ExtraInfosRepository {
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    private val registeredDB by lazy {
        Firebase.firestore.collection(REGISTERED_COLLECTION).document("${auth?.uid}" ?: "")
    }

    suspend fun getUserDB(): DocumentSnapshot {
        return userDB.get().await()
    }

    suspend fun setExtraInfosDB(type: Int, adress: String, registered: Registered, phone: String) {
        userDB.update(mapOf("type" to type, "adress" to adress, "phone" to phone)).await()
        registeredDB.set(registered, SetOptions.merge()).await()
    }

    suspend fun viaCepResponse(cep: Int): ResponseAPI {
        return try {
            val response = APIService.viaCepApi.getCep(cep)

            if(response.isSuccessful) {
                ResponseAPI.Success(response.body())
            }else {
                if(response.code() == 400) {
                    ResponseAPI.Error("CEP Digitado Errado")
                }else {
                    ResponseAPI.Error("CEP errado")
                }
            }
        }catch (exception: Exception) {
            ResponseAPI.Error(exception.toString())
        }
    }
}