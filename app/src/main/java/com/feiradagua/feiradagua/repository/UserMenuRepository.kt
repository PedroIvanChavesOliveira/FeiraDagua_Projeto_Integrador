package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UserMenuRepository {
    private val auth by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    private val token by lazy {
        FirebaseMessaging.getInstance().token
    }

    private val producerDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION)
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    private suspend fun getToken(): String {
        return token.await()
    }

    suspend fun updateToken() {
        userDB.update("token", getToken()).await()
    }

    suspend fun getUserDb(): DocumentSnapshot? {
        return userDB.get().await()
    }

    suspend fun getProducers(): MutableList<Producer>? {
        val query = producerDB.whereEqualTo("type", 2).get().await()
        return query.toObjects(Producer::class.java)
    }
}