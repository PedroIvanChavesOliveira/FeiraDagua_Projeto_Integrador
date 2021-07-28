package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.api.APIService
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.utils.Constants.Firebase.ORDERS_COLLECTION
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserShopCartRepository {
    private val orderDB by lazy {
        Firebase.firestore.collection(ORDERS_COLLECTION)
    }

    suspend fun sendNewOrder(id: String, order: Order): Boolean {
        orderDB.document(id).set(order, SetOptions.merge()).await()
        return true
    }

    suspend fun sendNotification(notification: PushNotification) {
        try {
            val response = APIService.api.postNotification(notification)
            if(response.isSuccessful) {
                ResponseAPI.Success(response.body())
            }else {
                if(response.code() == 400) {
                    ResponseAPI.Error(response.errorBody().toString())
                }else {
                    ResponseAPI.Error(response.errorBody().toString())
                }
            }
        }catch (e: Exception) {

        }
    }
}