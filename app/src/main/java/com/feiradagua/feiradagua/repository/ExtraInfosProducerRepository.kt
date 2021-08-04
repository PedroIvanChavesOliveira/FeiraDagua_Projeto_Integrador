package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ExtraInfosProducerRepository {
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    private val userDB by lazy {
        Firebase.firestore.collection(USER_COLLECTION).document("${auth?.uid}" ?: "")
    }

    suspend fun setExtraInfosDB(description: String, deliveryDate: MutableList<String>,
                                deliveryLocation: MutableList<String>, payment: MutableList<String>, category: MutableList<String>) {
        userDB.update(mapOf("description" to description, "deliveryDate" to deliveryDate,
                "deliveryLocation" to deliveryLocation, "payment" to payment, "category" to category)).await()
    }
}