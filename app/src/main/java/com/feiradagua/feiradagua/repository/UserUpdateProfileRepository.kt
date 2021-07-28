package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.api.APIService
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.updateProducer
import com.feiradagua.feiradagua.utils.updateUser
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserUpdateProfileRepository {
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    private val userDB by lazy {
        Firebase.firestore.collection(Constants.Firebase.USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    suspend fun updateUser(user: User): Boolean {
        userDB.set(user, SetOptions.merge()).await()
        UserMenuActivity.USER.updateUser(user)
        return true
    }

    suspend fun viaCepResponse(cep: Int): ResponseAPI {
        return try {
            val response = APIService.viaCepApi.getCep(cep)

            if(response.isSuccessful) {
                ResponseAPI.Success(response.body())
            }else {
                if(response.code() == 400) {
                    ResponseAPI.Error(response.errorBody().toString())
                }else {
                    ResponseAPI.Error(response.errorBody().toString())
                }
            }
        }catch (exception: Exception) {
            ResponseAPI.Error(exception.toString())
        }
    }
}