package com.example.chatappnative.presentation.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatappnative.presentation.main.chat.ChatScreen
import com.example.chatappnative.presentation.main.chat.ChatViewModel
import com.example.chatappnative.presentation.main.components.BottomNavItem
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorF9FFFF
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        BottomNavigation(
            modifier = Modifier.padding(bottom = 40.dp),
            backgroundColor = ColorF9FFFF,
            elevation = 10.dp,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val items = listOf(
                BottomNavItem.Chat,
                BottomNavItem.Contact,
                BottomNavItem.Setting
            )

            items.forEach {
                val contentColor =
                    if (currentRoute == it.route) {
                        Color191919.copy(alpha = 0.95F)
                    } else {
                        Color191919.copy(
                            alpha = 0.5F
                        )
                    }
                BottomNavigationItem(
                    selected = currentRoute == it.route,
                    selectedContentColor = Color191919.copy(alpha = 0.95F),
                    unselectedContentColor = Color191919.copy(alpha = 0.5F),
                    onClick = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = it.resourceId),
                            contentDescription = "",
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = it.label, style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = contentColor,
                            )
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            },
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavigationHost(navController)
            }
        }
    }

    @Composable
    fun NavigationHost(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.Chat.route) {
            composable(BottomNavItem.Chat.route) {
                val chatModel: ChatViewModel by viewModels()
                ChatScreen(chatModel = chatModel)
            }
            composable(BottomNavItem.Contact.route) {
                Text(text = "Contacts")
            }
            composable(BottomNavItem.Setting.route) {
                Text(text = "Settings")
            }
        }
    }
}

