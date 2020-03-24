package com.iremember.storage

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.util.*


class SharedPrefManager private constructor(private val mCtx: Context) {


    fun getSettings(permission: String): Boolean {
        val sharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(permission, false)
    }
    fun getLooks(permission: String): Boolean {
        val sharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(permission, true)
    }


    fun saveSettings(state: Boolean, permission: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(permission, state)
        editor.apply()
    }


    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    companion object {
        val SHARED_PREF_NAME = "my_shared_preff"
        private val SHARED_PREF_ADVER = "my_shared_adver"

        @SuppressLint("StaticFieldLeak")
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}