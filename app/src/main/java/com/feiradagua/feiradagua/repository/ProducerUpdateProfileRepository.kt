package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.api.APIService
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.LAST_MODIFIED_FIELD
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.feiradagua.feiradagua.utils.updateProducer
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class ProducerUpdateProfileRepository {
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    suspend fun updateProducer(producer: Producer): Boolean {
        userDB.set(producer, SetOptions.merge()).await()
        userDB.update(mapOf(LAST_MODIFIED_FIELD to Calendar.getInstance().time.toString())).await()
        ProducerMenuActivity.PRODUCER.updateProducer(producer)
        return true
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