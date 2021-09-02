package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class StoreInfosRepository {
    private val productsDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    suspend fun getProducerProducts(id: String, /*lastDate: String*/): MutableList<Products> {
        val query = productsDB.whereEqualTo("producerId", id).get().await()
        return query.toObjects(Products::class.java)
//        val query = productsDB.whereEqualTo("producerId", id)
//        val orderedQuery = productsDB.orderBy(LAST_MODIFIED_FIELD, Query.Direction.DESCENDING).whereGreaterThan(LAST_MODIFIED_FIELD, lastDate)
//        val result = mutableListOf<Products>()
//        orderedQuery.get(SERVER).addOnCompleteListener { task ->
//            if(task.isSuccessful) {
//                val docsServer = task.result?.documents
//                if(docsServer?.isEmpty() == true) {
//                    query.get(CACHE).addOnCompleteListener { taskCache ->
//                        taskCache.result?.documents?.forEach {
//                            val product = it.toObject(Products::class.java)
//                            product?.let { result.add(product) }
//                        }
//                    }
//                    return@addOnCompleteListener
//                }else {
//                    docsServer?.forEach { doc ->
//                        val product = doc.toObject(Products::class.java)
//                        if(product?.producerId == id) {
//                            result.add(product)
//                        }
//                    }
//                    query.get(CACHE).addOnCompleteListener { taskCache ->
//                        if(taskCache.isSuccessful) {
//                            val docsCache = taskCache.result?.documents
//                            docsCache?.forEach {
//                                val product = it?.toObject(Products::class.java)
//                                if(!result.checkingIfExist(product?.id)){
//                                    product?.let { result.add(product) }
//                                }
//                            }
//                        }
//                    }
//                    return@addOnCompleteListener
//                }
//            }
//        }.await()
//        return result
    }
}