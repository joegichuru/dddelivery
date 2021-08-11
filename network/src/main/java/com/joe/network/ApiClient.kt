package com.joe.network

import android.content.Context
import co.infinum.retromock.BodyFactory
import co.infinum.retromock.Retromock
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileInputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.google.com/"
const val CACHE_SIZE = 100 * 1024L

object ApiClient {
    fun provideApiClient(context: Context): Api {
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

    /**
     * provide mock api client
     */
    fun mockApiClient(context: Context): Api {
        return Retromock.Builder()
            .apply {
                retrofit(buildRetrofit(context))
                defaultBehavior {
                    500
                }
                defaultBodyFactory(FileResourceFactory(context))
            }.build().create(Api::class.java)
    }

    /**
     * Factory to allow us to get input stream from a file
     */
    class FileResourceFactory(val context: Context) : BodyFactory {
        override fun create(input: String): InputStream {
            return context.assets.open(input)
        }

    }
}
