package com.example.danmech.Sharedprefs

import android.content.Context

import android.content.SharedPreferences

/**
 * Class for Shared Preference
 */
class Moyosharedprefs(var context: Context) {
    fun MakeOld() {
        val sharedPreferences =
            context.getSharedPreferences("NotTheFirstTime", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Old", "true")
        editor.apply()
    }

    val isUserNotOld: Boolean
        get() {
            val sharedPreferences =
                context.getSharedPreferences("NotTheFirstTime", Context.MODE_PRIVATE)
            val isEmailEmpty = sharedPreferences.getString("Old", "")!!.isEmpty()
            return isEmailEmpty
        }


    fun saveLoginDetails(email: String?, password: String?) {
        val sharedPreferences =
            context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Email", email)
        editor.putString("Password", password)
        editor.apply()
    }

    val email: String?
        get() {
            val sharedPreferences =
                context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
            return sharedPreferences.getString("Email", "")
        }

    val isUserLogedOut: Boolean
        get() {
            val sharedPreferences =
                context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
            val isEmailEmpty = sharedPreferences.getString("Email", "")!!.isEmpty()
            val isPasswordEmpty = sharedPreferences.getString("Password", "")!!.isEmpty()
            return isEmailEmpty || isPasswordEmpty
        }

}