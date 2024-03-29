package com.feiradagua.feiradagua.repository

import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Firebase.LAST_MODIFIED_FIELD
import com.feiradagua.feiradagua.utils.Constants.Firebase.PRODUCTS_COLLECTION
import com.feiradagua.feiradagua.utils.checkingIfExist
import com.feiradagua.feiradagua.utils.updateProduct
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

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
        productDB.document(id).set(product, SetOptions.merge()).addOnSuccessListener {
//            productDB.document(id).update(mapOf(LAST_MODIFIED_FIELD to Calendar.getInstance().time.toString()))
        }.await()
        return true
    }
}