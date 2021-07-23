package com.feiradagua.feiradagua.repository

import android.net.Uri
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class CameraRepository {
    private val firebaseStorageRef by lazy {
        Firebase.storage.reference
    }
    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val userDB by lazy {
        Firebase.firestore.collection(Constants.Firebase.USER_COLLECTION).document("${firebaseAuth.uid}" ?: "")
    }
    private val productDB by lazy {
        Firebase.firestore.collection(PRODUCTS_COLLECTION)
    }

    suspend fun putFileToStorage(uri: Uri): Uri {
        firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/profilePhoto.jpg").putFile(uri).await()
        val uriPath = firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/profilePhoto.jpg").downloadUrl.await()
        saveUriOnProfile(uriPath)
        return uriPath
    }

    suspend fun putFileToStorageProducts(uri: Uri, id: String): Uri {
        firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/product-${id}.jpg").putFile(uri).await()
        val uriPath = firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/product-${id}.jpg").downloadUrl.await()
        saveUriOnProducts(uriPath, id)
        return uriPath
    }

    private suspend fun saveUriOnProfile(uri: Uri) {
        userDB.update("photo", uri.toString()).await()
    }

    private suspend fun saveUriOnProducts(uri: Uri, id: String) {
        ProducerMenuActivity.PRODUCTS?.let {
            val filter = it.filter { it.id == id }
            if (filter.isNotEmpty()) {
                productDB.document(id).update("photo", uri.toString()).await()
            } else {
                val product = Products(id = id, photo = uri.toString())
                productDB.document(id).set(product, SetOptions.merge()).await()
            }
        }
//        }?: run {
//            val product = Products(id = id, photo = uri.toString())
//            productDB.document(id).set(product, SetOptions.merge()).await()
//        }
    }
}