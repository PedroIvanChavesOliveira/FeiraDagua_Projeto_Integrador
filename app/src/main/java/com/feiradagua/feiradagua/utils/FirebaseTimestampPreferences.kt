package com.feiradagua.feiradagua.utils

import android.content.Context
import android.content.SharedPreferences
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TIMESTAMP_KEY
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TIMESTAMP_ORDERS
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TIMESTAMP_PRODUCERS_LIST
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TIMESTAMP_PRODUCTS
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TIMESTAMP_USERS

object FirebaseTimestampPreferences {
    fun setLastModifiedPreferences(context: Context, pos: Int, date: String) {
        var preferences: SharedPreferences? = null
        when(pos) {
            1 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_USERS, Context.MODE_PRIVATE) }
            2 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_ORDERS, Context.MODE_PRIVATE) }
            3 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_PRODUCTS, Context.MODE_PRIVATE) }
            4 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_PRODUCERS_LIST, Context.MODE_PRIVATE) }
        }
        val editor = preferences?.edit()
        editor?.putString(SHAREDPREFERENCES_TIMESTAMP_KEY, date)
        editor?.apply()
    }

    fun getLastModifiedPreferences(context: Context, pos: Int): String? {
        var preferences: SharedPreferences? = null
        when(pos) {
            1 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_USERS, Context.MODE_PRIVATE) }
            2 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_ORDERS, Context.MODE_PRIVATE) }
            3 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_PRODUCTS, Context.MODE_PRIVATE) }
            4 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TIMESTAMP_PRODUCERS_LIST, Context.MODE_PRIVATE) }
        }
        return preferences?.getString(SHAREDPREFERENCES_TIMESTAMP_KEY, "")
    }
}