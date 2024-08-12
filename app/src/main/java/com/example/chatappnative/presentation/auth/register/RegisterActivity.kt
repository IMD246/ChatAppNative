package com.example.chatappnative.presentation.auth.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.chatappnative.presentation.auth.composables.LargeTopSection
import com.example.chatappnative.presentation.auth.login.RegisterViewModel
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    private val registeriewModel: RegisterViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        LargeTopSection(
                            title = "Small Top App Bar",
                            subTitle = "Fill up your details to register."
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}