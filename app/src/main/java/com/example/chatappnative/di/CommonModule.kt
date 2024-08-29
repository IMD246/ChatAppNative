package com.example.chatappnative.di

import android.content.Context
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.service.ConnectivityInternetObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): Preferences {
        return Preferences(context)
    }

    @Provides
    @Singleton
    fun provideSocketManager(preferences: Preferences): SocketManager {
        return SocketManager(preferences)
    }

    @Provides
    @Singleton
    fun provideConnectivityInternetObserver(
        @ApplicationContext context: Context,
        socketManager: SocketManager
    ): ConnectivityInternetObserver {
        return ConnectivityInternetObserver(context)
    }
}