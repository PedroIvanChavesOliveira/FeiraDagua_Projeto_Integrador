package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.REGISTERED_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val auth by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    private val token by lazy {
        FirebaseMessaging.getInstance().token
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    private val registeredDB by lazy {
        Firebase.firestore.collection(REGISTERED_COLLECTION).document("${auth?.uid}" ?: "")
    }

    fun getUser(): FirebaseUser? {
        return auth
    }

    suspend fun getToken(): String {
        return token.await()
    }

    suspend fun getUserDb(): DocumentSnapshot? {
        return userDB.get().await()
    }

    suspend fun addUserOnDataBase(user: User) {
        userDB.set(user, SetOptions.merge()).await()
    }

    suspend fun getRegisteredDb(): DocumentSnapshot? {
        return registeredDB.get().await()
    }
}