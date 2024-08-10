package com.example.chatappnative.data.repository

import com.example.chatappnative.data.data_source.ExampleDataSource
import com.example.chatappnative.domain.repository.Repository
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val exampleDataSource: ExampleDataSource
) : Repository {
    override fun getExample(): String {
        return "a";
//        return exampleDataSource.getAllCoins("1")
    }
}