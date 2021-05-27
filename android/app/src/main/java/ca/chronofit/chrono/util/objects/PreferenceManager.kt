package ca.chronofit.chrono.util.objects

import android.content.Context
import android.content.SharedPreferences
import ca.chronofit.chrono.util.BaseActivity
import com.google.gson.GsonBuilder

object PreferenceManager {
    lateinit var preferences: SharedPreferences
    private const val PREFERENCES_FILE_NAME = "saved_circuits"

    fun with(application: BaseActivity) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
    }

    fun <T> put(`object`: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        preferences.edit().putString(key, jsonString).apply()
    }

    fun put(value: String, key: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun get(key: String): String {
        return preferences.getString(key, "")!!.toString()
    }

    inline fun <reified T> get(key: String): T? {
        val value = preferences.getString(key, null)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}