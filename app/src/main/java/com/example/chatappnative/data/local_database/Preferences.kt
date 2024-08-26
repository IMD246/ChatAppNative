package com.example.chatappnative.data.local_database

import android.content.Context
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.google.gson.Gson

class Preferences(context: Context) {
    private var sharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

    private val ONBOARDING = "onboarding"
    private val ACCESS_TOKEN = "access_token"
    private val USER_INFO = "user_info"

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
        sharedPreferences.edit().clear().apply()
    }
}