package com.feiradagua.feiradagua.repository

import android.content.Context
import com.feiradagua.feiradagua.model.`class`.Order
//import com.feiradagua.feiradagua.database.FeiraDaguaDatabase
//import com.feiradagua.feiradagua.database.entitys.DataDao
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.CACHE
import com.feiradagua.feiradagua.utils.Constants.Firebase.LAST_MODIFIED_FIELD
import com.feiradagua.feiradagua.utils.Constants.Firebase.SERVER
import com.feiradagua.feiradagua.utils.Constants.Firebase.USER_COLLECTION
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.getLastModifiedPreferences
import com.feiradagua.feiradagua.utils.checkingIfExistProducer
import com.feiradagua.feiradagua.utils.convertToTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.coroutines.coroutineContext

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

    private val ordersDB by lazy {
        Firebase.firestore.collection(Constants.Firebase.ORDERS_COLLECTION)
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

    suspend fun getUserDb(/*lastModified: String*/): DocumentSnapshot? {
        return userDB.get().await()
//        return userDB.get(CACHE).addOnCompleteListener {
//            if(it.isSuccessful) {
//                val exists = it.result?.exists()
//                val cacheDate = it.result?.getDate(LAST_MODIFIED_FIELD)?.toString()
//                if(exists == false) {
//                    userDB.get(SERVER)
//                }else {
//                    if (cacheDate != null) {
//                        if(cacheDate < lastModified) {
//                            userDB.get(SERVER)
//                        }
//                    }
//                }
//            }
//        }.await()
    }

    suspend fun getHistoricDb(): MutableList<Order> {
        val query = ordersDB.whereEqualTo("userId", auth?.uid).get().await()
        return query.toObjects(Order::class.java)
    }

    suspend fun getProducers(deliveryArea: String, /*lastDate: String*/ context: Context, id: String): MutableList<Producer> {
        val query = producerDB.whereEqualTo("type", 2).whereArrayContains("deliveryLocation", deliveryArea).get().await()
        return query.toObjects(Producer::class.java)
//        val userLastDate = getUserDateFromRoom(context, id)
//        val query = producerDB.whereEqualTo("type", 2).whereArrayContains("deliveryLocation", deliveryArea)
//        val orderedQuery = producerDB.orderBy(LAST_MODIFIED_FIELD, Query.Direction.DESCENDING).whereGreaterThan(LAST_MODIFIED_FIELD, userLastDate)
//        val result: MutableList<Producer> = mutableListOf()
//        orderedQuery.get(SERVER).addOnCompleteListener { task ->
//            if(task.isSuccessful) {
//                val docsServer= task.result?.documents
//                val isEmpty = task.result?.isEmpty
//                if(docsServer?.isEmpty() == true) {
//                    query.get(CACHE).addOnCompleteListener { taskCache ->
//                        if(taskCache.isSuccessful) {
//                            val docsCache = taskCache.result?.documents
//                            docsCache?.forEach {
//                                val producer = it?.toObject(Producer::class.java)
//                                producer?.let { result.add(producer) }
//                            }
//                        }
//                    }
//                }else {
//                    docsServer?.forEach {
//                        val producer = it?.toObject(Producer::class.java)
//                        if(producer?.type == 2 && producer.deliveryLocation.contains(deliveryArea)) {
//                            result.add(producer)
//                        }
//                    }
//                    query.get(CACHE).addOnCompleteListener { taskServer ->
//                        if(taskServer.isSuccessful) {
//                            val docsCache = taskServer.result?.documents
//                            docsCache?.forEach {
//                                val producer = it?.toObject(Producer::class.java)
//                                if(!result.checkingIfExistProducer(producer?.uid)){
//                                    producer?.let { result.add(producer) }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }.await().apply { return result }
    }

//    private suspend fun getUserDateFromRoom(context: Context, id: String): Date = FeiraDaguaDatabase.getDatabase(context).dataDao().getLastloginDateUser(id)
//
//    suspend fun insertUserDateInRoom(context: Context) {
//        val data = FeiraDaguaDatabase.getDatabase(context).dataDao().insertLastLoginDateUser(Calendar.getInstance().time)
//        data
//    }
}