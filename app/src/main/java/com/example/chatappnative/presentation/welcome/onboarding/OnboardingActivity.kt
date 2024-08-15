package com.example.chatappnative.presentation.welcome.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.R
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.composables.BaseButton
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorE9FEFE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {

    private val onboardingModel: OnboardingViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppNativeTheme {
                Scaffold {
                    OnboardingScreen()
                }
            }
        }
    }

    @Composable
    fun OnboardingScreen() {
        val pagerState = rememberPagerState(
            pageCount = {
                3
            },
            initialPage = 0,
        )

        val currentPage = onboardingModel.getCurrentPage()

        LaunchedEffect(
            key1 = currentPage,
            block = {
                if (currentPage != pagerState.currentPage) {
                    pagerState.animateScrollToPage(currentPage)
                }
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorE9FEFE)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        )
        {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> {
                        FirstPage()
                    }

                    1 -> {
                        SecondPage()
                    }

                    2 -> {
                        ThirdPage()
                    }

                    else -> Text(
                        text = "Can't reach to this page",
                    )
                }
            }
        }
    }

    @Composable
    fun FirstPage() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            BaseButton(
                modifier = Modifier
                    .align(Alignment.End),
                titleColorEnabled = Color191919.copy(alpha = 0.75F),
                bgColorEnabled = MaterialTheme.colorScheme.primary.copy(alpha = 0.25F),
                shapeRadius = 15.dp,
                titleStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color191919.copy(alpha = 0.75F),
                ),
                title = "Skip",
                onClick = {
                    onboardingModel.onSkipOrGetStarted()
                    startActivity(
                        Intent(
                            this@OnboardingActivity,
                            LoginActivity::class.java
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Welcome to ${applicationContext.getString(R.string.app_name)},\n" +
                        "a great friend to chat \n" +
                        "with you",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color191919.copy(alpha = 0.85F)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                title = "Next",
                onClick = {
                    onboardingModel.onNext()
                },
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    @Composable
    fun SecondPage() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            BaseButton(
                modifier = Modifier
                    .align(Alignment.End),
                titleColorEnabled = Color191919.copy(alpha = 0.75F),
                bgColorEnabled = MaterialTheme.colorScheme.primary.copy(alpha = 0.25F),
                shapeRadius = 15.dp,
                titleStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color191919.copy(alpha = 0.75F),
                ),
                title = "Skip",
                onClick = {
                    onboardingModel.onSkipOrGetStarted()
                    startActivity(
                        Intent(
                            this@OnboardingActivity,
                            LoginActivity::class.java
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "If you are confused about \n" +
                        "what to do just open \n" +
                        "${applicationContext.getString(R.string.app_name)} app",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color191919.copy(alpha = 0.85F)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                title = "Next",
                onClick = {
                    onboardingModel.onNext()
                },
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    @Composable
    fun ThirdPage() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            BaseButton(
                modifier = Modifier
                    .align(Alignment.End),
                titleColorEnabled = Color191919.copy(alpha = 0.75F),
                bgColorEnabled = MaterialTheme.colorScheme.primary.copy(alpha = 0.25F),
                shapeRadius = 15.dp,
                titleStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color191919.copy(alpha = 0.75F),
                ),
                title = "Skip",
                onClick = {
                    onboardingModel.onSkipOrGetStarted()
                    startActivity(
                        Intent(
                            this@OnboardingActivity,
                            LoginActivity::class.java
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "${applicationContext.getString(R.string.app_name)} will be ready\n" +
                        "to chat & make you\n" +
                        "happy",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color191919.copy(alpha = 0.85F)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                title = "Get Started",
                onClick = {
                    onboardingModel.onSkipOrGetStarted()
                    startActivity(
                        Intent(
                            this@OnboardingActivity,
                            LoginActivity::class.java
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

