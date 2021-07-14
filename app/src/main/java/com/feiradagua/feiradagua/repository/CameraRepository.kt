package com.feiradagua.feiradagua.repository

import android.net.Uri
import com.feiradagua.feiradagua.utils.Constants
import com.google.firebase.auth.ktx.auth
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

    suspend fun putFileToStorage(uri: Uri): Uri {
        firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/profilePhoto.jpg").putFile(uri).await()
        val uriPath = firebaseStorageRef.child("${(firebaseAuth.currentUser?.uid ?: "")}/profilePhoto.jpg").downloadUrl.await()
        saveUriOnProfile(uriPath)
        return uriPath
    }

    private suspend fun saveUriOnProfile(uri: Uri) {
        userDB.update("photo", uri.toString()).await()
    }
}