package com.joe.dialdelivery.di

import android.content.Context
import com.joe.network.Api
import com.joe.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideApi(@ApplicationContext context: Context): Api {
        return ApiClient.provideApiClient(context)
    }
}