package com.joe.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.google.com/"
const val CACHE_SIZE = 100 * 1024L

object ApiClient {
    fun provideApiClient( context: Context): Api {
        return buildRetrofit(context)
            .create(Api::class.java)
    }

    private fun buildRetrofit(context: Context): Retrofit {
        val cache = Cache(context.cacheDir, CACHE_SIZE)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(MockInterceptor(context))
            .build()
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}
