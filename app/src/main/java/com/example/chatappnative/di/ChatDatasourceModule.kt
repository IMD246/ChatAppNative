package com.example.chatappnative.di

import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.data_source.ChatDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.repository.ChatRepositoryImpl
import com.example.chatappnative.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDatasourceModule {

    @Provides
    @Singleton
    fun provideChatDataSource(): ChatDataSource {
        val interceptorLog = HttpLoggingInterceptor()
        // set your desired log level
        interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = Builder()
            .addInterceptor(interceptorLog)

        return Retrofit.Builder()
            .baseUrl(NetworkUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ChatDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatDataSource: ChatDataSource,
        preferences: Preferences,
    ): ChatRepository {
        return ChatRepositoryImpl(chatDataSource, preferences)
    }
}