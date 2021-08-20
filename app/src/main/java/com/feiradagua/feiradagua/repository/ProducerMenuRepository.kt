package com.feiradagua.feiradagua.repository

import android.content.Context
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.CACHE
import com.feiradagua.feiradagua.utils.Constants.Firebase.LAST_MODIFIED_FIELD
import com.feiradagua.feiradagua.utils.Constants.Firebase.ORDERS_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.utils.Constants.Firebase.SERVER
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.getLastModifiedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
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

    suspend fun getProducerDb(lastDate: String): DocumentSnapshot? {
//        return userDB.get().await()
        return userDB.get(CACHE).addOnCompleteListener {
            if(it.isSuccessful) {
                val exists = it.result?.exists()
                val cacheDate = it.result?.get(LAST_MODIFIED_FIELD)?.toString()
                if(exists == false) {
                    userDB.get(SERVER)
                }else {
                    if (cacheDate != null) {
                        if(cacheDate < lastDate) {
                            userDB.get(SERVER)
                        }
                    }
                }
            }
        }.await()
    }

    suspend fun getProductsDB(/*lastDate: String*/): QuerySnapshot? {
       return  productsDB.whereEqualTo("producerId", auth?.uid).get().await()
//        val orderedQuery = query.orderBy(LAST_MODIFIED_FIELD, Query.Direction.DESCENDING).whereGreaterThan(LAST_MODIFIED_FIELD, lastDate)
//        return query.get(CACHE).addOnCompleteListener { task ->
//            if(task.isSuccessful) {
//                orderedQuery.get(CACHE).addOnCompleteListener {
//                    val isEmpty = task.result?.isEmpty
//                    if(isEmpty == true) {
//                        orderedQuery.get(SERVER)
//                    }
//                }
//            }
//        }.await()
    }

    suspend fun getOrdersDB(/*lastDate: String*/): QuerySnapshot? {
        return ordersDB.whereEqualTo("confirmation", 0).whereEqualTo("producerId", auth?.uid?:"").get().await()
//        val orderedQuery = query.orderBy(LAST_MODIFIED_FIELD, Query.Direction.DESCENDING).whereGreaterThan(LAST_MODIFIED_FIELD, lastDate)
//        return query.get(CACHE).addOnCompleteListener { task ->
//            if(task.isSuccessful) {
//                orderedQuery.get(CACHE).addOnCompleteListener {
//                    val isEmpty = it.result?.isEmpty
//                    if(isEmpty == true) {
//                        orderedQuery.get(SERVER)
//                    }
//                }
//            }
//        }.await()
    }
}