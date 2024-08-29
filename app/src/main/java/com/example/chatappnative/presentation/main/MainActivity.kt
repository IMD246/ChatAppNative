package com.example.chatappnative.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatappnative.R
import com.example.chatappnative.event.AddFriendEvent
import com.example.chatappnative.event.UpdateUserPresenceEvent
import com.example.chatappnative.presentation.add_contact.AddContactActivity
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.main.chat.ChatScreen
import com.example.chatappnative.presentation.main.chat.ChatViewModel
import com.example.chatappnative.presentation.main.components.BottomNavItem
import com.example.chatappnative.presentation.main.contact.ContactScreen
import com.example.chatappnative.presentation.main.contact.ContactViewModel
import com.example.chatappnative.presentation.main.setting.SettingScreen
import com.example.chatappnative.presentation.main.setting.SettingViewModel
import com.example.chatappnative.service.EventBusService
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorF9FFFF
import com.example.chatappnative.ui.theme.ColorPrimary
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainModel: MainViewModel by viewModels()

    private val chatModel: ChatViewModel by viewModels()
    private val contactModel: ContactViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    companion object {
        const val TAB_INDEX = "tab-index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tabIndex = intent.getIntExtra(TAB_INDEX, 0)

        requestNotificationPermission()
        setContent {
            ChatAppNativeTheme {
                MainScreen(tabIndex)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddFriendEvent(event: AddFriendEvent) {
        contactModel.updateContactFriend(
            event.status,
            event.friendModel
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateUserPresenceEvent(event: UpdateUserPresenceEvent) {
        chatModel.updateItemPresence(
            event.userPresenceSocketModel
        )
        contactModel.updateItemPresence(
            event.userPresenceSocketModel
        )
    }

    override fun onStart() {
        super.onStart()
        EventBusService.register(this)
    }

    override fun onStop() {
        EventBusService.unregister(this)
        super.onStop()
    }


    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        BottomNavigation(
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
                        if (currentRoute == it.route) return@BottomNavigationItem

                        mainModel.updateIsSettingScreen(it.route == BottomNavItem.Setting.route)

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
    fun HandlePendingActivity(navController: NavHostController) {
        val getActivityPending = mainModel.getActivityPending()

        LaunchedEffect(getActivityPending) {
            when (getActivityPending) {
                AddContactActivity::class.java.name -> {
                    val intent = Intent(this@MainActivity, AddContactActivity::class.java)
                    startActivity(intent)
                }

                MainActivity::class.java.name -> {
                    mainModel.updateIsSettingScreen(false)
                    navController.navigate(BottomNavItem.Contact.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }

                else -> {
                    // do nothing
                }
            }

            mainModel.clearActivityPending()
        }
    }

    @Composable
    fun MainScreen(tabIndex: Int = 0) {
        val navController = rememberNavController()
        val isSettingScreen = mainModel.isSettingScreen.collectAsState().value

        HandlePendingActivity(navController)

        LaunchedEffect(tabIndex) {
            if (tabIndex != 0) {
                mainModel.updateIsSettingScreen(false)
                navController.navigate(BottomNavItem.Contact.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            },
            floatingActionButton = {
                if (!isSettingScreen)
                    FloatingActionButton(
                        containerColor = ColorPrimary,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(60.dp),
                        onClick = {
                            startActivity(Intent(this@MainActivity, AddContactActivity::class.java))
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add_user),
                            contentDescription = "",
                        )
                    }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                    },
            ) {
                NavigationHost(navController)
            }
        }
    }


    @Composable
    fun NavigationHost(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.Chat.route) {
            composable(BottomNavItem.Chat.route) {
                ChatScreen(chatModel = chatModel)
            }
            composable(BottomNavItem.Contact.route) {
                ContactScreen(contactModel = contactModel)
            }
            composable(BottomNavItem.Setting.route) {
                SettingScreen(settingViewModel = settingViewModel) {
                    finish()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}

