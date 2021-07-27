package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class StoreInfosRepository {
    private val productsDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    suspend fun getProducerProducts(id: String): MutableList<Products> {
        val query = productsDB.whereEqualTo("producerId", id).get().await()
        return query.toObjects(Products::class.java)
    }
}