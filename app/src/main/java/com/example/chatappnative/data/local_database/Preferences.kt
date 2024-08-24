package com.example.chatappnative.data.local_database

import android.content.Context

class Preferences(context: Context) {
    private var sharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

    private val ONBOARDING = "onboarding"
    private val ACCESS_TOKEN = "access_token"

    fun saveOnboarding() {
        sharedPreferences.edit().putBoolean(ONBOARDING, true).apply()
    }

    fun getOnboarding(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING, false)
    }

    fun saveAccessToken(value: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}