package com.example.chatappnative.presentation.welcome.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.chatappnative.R
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.auth.login.SplashViewModel
import com.example.chatappnative.presentation.welcome.onboarding.OnboardingActivity
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val splashModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        val isOnboarding = splashModel.getIsOnboarding().collectAsState().value
        val accessToken = splashModel.getAccessToken().collectAsState().value

        LaunchedEffect(key1 = isOnboarding, key2 = accessToken) {
            val intent: Intent = if (isOnboarding) {
                if (accessToken.isEmpty()) {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                } else {
                    // Main Page
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
            } else {
                Intent(this@SplashActivity, OnboardingActivity::class.java)
            }

            startActivity(intent)
        }
        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_launcher_background),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

