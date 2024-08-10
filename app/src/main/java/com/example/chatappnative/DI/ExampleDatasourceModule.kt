package com.example.chatappnative.DI

import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.data_source.ExampleDataSource
import com.example.chatappnative.data.repository.RepositoryImp
import com.example.chatappnative.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExampleDatasourceModule {

    @Provides
    @Singleton
    fun provideExampleDataSource(): ExampleDataSource {
        return Retrofit.Builder()
            .baseUrl(NetworkUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExampleDataSource::class.java);
    }

    @Provides
    @Singleton
    fun provideExampleRepository(dataSource: ExampleDataSource): Repository {
        return RepositoryImp(dataSource)
    }
}