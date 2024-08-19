package com.example.loanapplication.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject


class SharedPreferencesUtils @Inject constructor(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setStingShard(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getStringShard(key: String?, defVal: String?): String? {
        return preferences.getString(key, defVal)
    }

    fun setIntShard(key: String?, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }


    fun getIntShard(key: String?, defVal: Int): Int {
        return preferences.getInt(key, defVal)
    }

    fun setBooleanShard(key: String?, defVal: Boolean) {
        editor.putBoolean(key, defVal)
        editor.commit()
    }

    fun getBooleanShard(key: String?, defVal: Boolean): Boolean {
        return preferences.getBoolean(key, defVal)
    }

}
