package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.utils.deleteProduct
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class NewProductRepository {
    private val firebaseStorageRef by lazy {
        Firebase.storage.reference
    }

    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val productDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    suspend fun deleteById(id: String): Boolean {
        firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/product-${id}.jpg").delete().await()
        productDB.document(id).delete().await()
        ProducerMenuActivity.PRODUCTS?.deleteProduct(id)
        return true
    }
}