package com.dicoding.storyapp1.data

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.storyapp1.data.Constants.SESSION
import com.dicoding.storyapp1.data.Constants.TOKEN

object Preference {

    fun init(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun preferenceEditor(context: Context): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveSessionToken(token: String, context: Context) {
        val tokenSaver = preferenceEditor(context)
        tokenSaver.putString(TOKEN, token)
        tokenSaver.apply()
    }

    fun logout(context: Context) {
        val logoutEdit = preferenceEditor(context)
        logoutEdit.remove(TOKEN)
        logoutEdit.remove("status")
        logoutEdit.apply()
    }

}

object Constants {
    const val TOKEN = "token"
    const val SESSION = "session"
}