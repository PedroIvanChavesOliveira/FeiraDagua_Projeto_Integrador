package com.feiradagua.feiradagua.utils

import android.content.Context
import android.content.SharedPreferences
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_KEY
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_FIFTH
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_FIRST
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_FORTH
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_SECOND
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_SIXTH
import com.feiradagua.feiradagua.utils.Constants.SharedPreferences.SHAREDPREFERENCES_TUTORIAL_NAME_THIRD

object TutorialPreferences {

    fun tutorialPreferences(context: Context, show: Boolean, pos: Int) {
        var preferences: SharedPreferences? = null
        when(pos) {
            1 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FIRST, Context.MODE_PRIVATE) }
            2 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_SECOND, Context.MODE_PRIVATE) }
            3 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_THIRD, Context.MODE_PRIVATE) }
            4 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FORTH, Context.MODE_PRIVATE) }
            5 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FIFTH, Context.MODE_PRIVATE) }
            6 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_SIXTH, Context.MODE_PRIVATE) }
        }
        val editor = preferences?.edit()
        editor?.putBoolean(SHAREDPREFERENCES_TUTORIAL_KEY, show)
        editor?.apply()
    }

    fun getTutorialStatus(context: Context, pos: Int): Boolean? {
        var preferences: SharedPreferences? = null
        when(pos) {
            1 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FIRST, Context.MODE_PRIVATE) }
            2 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_SECOND, Context.MODE_PRIVATE) }
            3 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_THIRD, Context.MODE_PRIVATE) }
            4 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FORTH, Context.MODE_PRIVATE) }
            5 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_FIFTH, Context.MODE_PRIVATE) }
            6 -> { preferences = context.getSharedPreferences(SHAREDPREFERENCES_TUTORIAL_NAME_SIXTH, Context.MODE_PRIVATE) }
        }
        return preferences?.getBoolean(SHAREDPREFERENCES_TUTORIAL_KEY, true)
    }
}