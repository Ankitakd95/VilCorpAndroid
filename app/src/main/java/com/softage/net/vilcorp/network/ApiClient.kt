package com.softage.net.vilcorp.network


import com.softage.net.vilcorp.BuildConfig
import com.softage.net.vilcorp.util.Utility
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiClient {
    private const val BASE_URL = Utility.BASE_URL
    private var mRetrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (mRetrofit == null) {
            val okHttpClient = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(interceptor)
                }
            }.build()

            mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return mRetrofit!!
    }
}