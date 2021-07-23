package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.utils.checkingIfExist
import com.feiradagua.feiradagua.utils.updateProduct
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UpdateAndAddProductRepository {
    private val productDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    suspend fun addNewProduct(id: String, product: Products): Boolean {
        ProducerMenuActivity.PRODUCTS?.let{
            if(!it.checkingIfExist(id)) {
                it.add(product)
            } else {
                it.updateProduct(product)
            }
        }
        productDB.document(id).set(product, SetOptions.merge()).await()
        return true
    }

    suspend fun getPhoto(id: String): String? {
        val getProduct = productDB.document(id).get().await()
        return getProduct.toObject(Products::class.java)?.photo
    }
}