package com.feiradagua.feiradagua.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProducerProfileRepository {
    private val auth by lazy {
        Firebase.auth
    }

    fun signOut() {
        auth.signOut()
    }
}