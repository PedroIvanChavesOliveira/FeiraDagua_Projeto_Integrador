package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.REGISTERED_COLLECTION
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SplashRepository {
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    private val registeredDB by lazy {
        Firebase.firestore.collection(REGISTERED_COLLECTION).document("${auth?.uid}" ?: "")
    }

    fun getUser(): FirebaseUser? {
        return auth
    }

    suspend fun getRegisteredDb(): DocumentSnapshot? {
        return registeredDB.get().await()
    }
}