package com.example.chatappnative.presentation.welcome.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.chatappnative.R
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.composables.ObserverAsEvent
import com.example.chatappnative.presentation.main.MainActivity
import com.example.chatappnative.presentation.welcome.onboarding.OnboardingActivity
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val splashModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        val channelFlow = splashModel.navigateChannelFlow

        ObserverAsEvent(channelFlow) {
            val intent: Intent = when (it) {
                NavigateSplashScreen.Login -> Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )

                NavigateSplashScreen.Main -> Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                )

                NavigateSplashScreen.Onboarding -> Intent(
                    this@SplashActivity,
                    OnboardingActivity::class.java
                )
            }

            startActivity(intent)
            finish()
        }

        Image(
            painter = painterResource(id = R.drawable.img_launcher_background),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

