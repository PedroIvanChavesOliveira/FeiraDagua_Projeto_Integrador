package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.api.APIService
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.CACHE
import com.feiradagua.feiradagua.utils.Constants.Firebase.LAST_MODIFIED_FIELD
import com.feiradagua.feiradagua.utils.Constants.Firebase.ORDERS_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.SERVER
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.feiradagua.feiradagua.utils.deleteOrderSolved
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class OrderDetailsRepository {

    private val orderDB by lazy {
        Firebase.firestore.collection(ORDERS_COLLECTION)
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION)
    }

    suspend fun getUserDataById(id: String): DocumentSnapshot? {
        return userDB.document(id).get(CACHE).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val exist = task.result?.exists()
                if(exist != true) {
                    userDB.document(id).get(SERVER)
                }
            }
        }.await()
    }

    suspend fun updateOrderConfirmation(id: String, confirmation: Int): Boolean {
        orderDB.document(id).update(mapOf("confirmation" to confirmation)).await()
        ProducerMenuActivity.ORDERS?.deleteOrderSolved(id)
        return true
//        return if(confirmation == 2) {
//            orderDB.document(id).delete().await()
//            ProducerMenuActivity.ORDERS?.deleteOrderSolved(id)
//            true
//        }else {
//            orderDB.document(id).update(mapOf("confirmation" to confirmation)).await()
//            ProducerMenuActivity.ORDERS?.deleteOrderSolved(id)
//            true
//        }
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