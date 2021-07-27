package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.utils.Constants.Firebase.ORDERS_COLLECTION
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserShopCartRepository {
    private val orderDB by lazy {
        Firebase.firestore.collection(ORDERS_COLLECTION)
    }

    suspend fun sendNewOrder(id: String, order: Order): Boolean {
        orderDB.document(id).set(order, SetOptions.merge()).await()
        return true
    }
}