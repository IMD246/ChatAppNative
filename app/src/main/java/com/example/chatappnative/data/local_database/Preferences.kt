package com.example.chatappnative.data.local_database

import android.content.Context
import android.util.Log
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.google.gson.Gson

class Preferences(context: Context) {
    private var sharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

    private val ONBOARDING = "onboarding"
    private val ACCESS_TOKEN = "access_token"
    private val USER_INFO = "user_info"
    private val IS_LOGGED_IN = "is_logged_in"
    private val ACTIVITY_PENDING = "activity_pending"

    fun saveOnboarding() {
        sharedPreferences.edit().putBoolean(ONBOARDING, true).apply()
    }

    fun getOnboarding(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING, false)
    }

    fun saveAccessToken(value: String) {
        Log.d("Preferences", "saveAccessToken: $value")
        sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()
        saveIsLoggedIn(true)
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun saveIsLoggedIn(value: Boolean) {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, value).apply()
    }

    fun getIsLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun saveActivityPending(value: String) {
        sharedPreferences.edit().putString(ACTIVITY_PENDING, value).apply()
    }

    fun getActivityPending(): String {
        return sharedPreferences.getString(ACTIVITY_PENDING, "") ?: ""
    }

    fun saveUserInfo(value: UserInfoAccessModel) {
        val toJsonUserInfo = Gson().toJson(value)

        sharedPreferences.edit().putString(USER_INFO, toJsonUserInfo).apply()
    }

    fun getUserInfo(): UserInfoAccessModel? {
        val toJsonUserInfo = sharedPreferences.getString(USER_INFO, "") ?: ""
        if (toJsonUserInfo.isEmpty()) {
            return null
        }

        return Gson().fromJson(toJsonUserInfo, UserInfoAccessModel::class.java)
    }

    fun logout() {
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
        sharedPreferences.edit().remove(USER_INFO).apply()
        sharedPreferences.edit().remove(IS_LOGGED_IN).apply()
        sharedPreferences.edit().remove(ACTIVITY_PENDING).apply()
    }
}