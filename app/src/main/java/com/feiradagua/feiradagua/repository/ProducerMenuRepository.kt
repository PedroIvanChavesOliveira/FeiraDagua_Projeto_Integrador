package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.ORDERS_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class ProducerMenuRepository {
    private val auth by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    private val token by lazy {
        FirebaseMessaging.getInstance().token
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    private val ordersDB by lazy {
        Firebase.firestore.collection(ORDERS_COLLECTION)
    }

    private val productsDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    private suspend fun getToken(): String {
        return token.await()
    }

    suspend fun updateToken() {
        userDB.update("token", getToken()).await()
    }

    suspend fun getProducerDb(): DocumentSnapshot? {
        return userDB.get().await()
    }

    suspend fun getProductsDB(): QuerySnapshot? {
        return productsDB.whereEqualTo("producerId", auth?.uid).get().await()
    }

    suspend fun getOrdersDB(): QuerySnapshot? {
        return ordersDB.whereEqualTo("confirmation", 0).whereEqualTo("producerId", auth?.uid).get().await()
    }
}