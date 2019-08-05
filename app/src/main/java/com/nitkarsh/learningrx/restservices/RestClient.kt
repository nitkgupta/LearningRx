package com.nitkarsh.learningrx.restservices

import com.nitkarsh.learningrx.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 * class for getting and setting rest client
 */
class RestClient {

    companion object {

        private var apiService: RestServices? = null
        private var httpLoggingInterceptor =
            HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

        private val retrofit by lazy {
            retrofit2.Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }

        fun getClient(): Retrofit = retrofit

        fun getApiService(): RestServices {
            if (apiService == null) {
                apiService = getClient().create(RestServices::class.java)
            }
            return apiService!!
        }

    }
}