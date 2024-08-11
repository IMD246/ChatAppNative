package com.example.chatappnative.DI

import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.data_source.AuthDataSource
import com.example.chatappnative.data.repository.AuthRepositoryImpl
import com.example.chatappnative.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDatasourceModule {

    @Provides
    @Singleton
    fun provideExampleDataSource(): AuthDataSource {
        return Retrofit.Builder()
            .baseUrl(NetworkUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideExampleRepository(authDataSource: AuthDataSource): AuthRepository {
        return AuthRepositoryImpl(authDataSource)
    }
}