package com.aftabsikander.mercari.di

import android.app.Application
import com.aftabsikander.mercari.network.RequestInterceptor
import com.aftabsikander.mercari.network.WebLinks
import com.aftabsikander.mercari.network.services.MercariService
import com.aftabsikander.mercari.utilities.constants.AppConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(AppConstants.WEB_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.WEB_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.WEB_WRITE_FILE_TIMEOUT, TimeUnit.MINUTES)
            .addInterceptor(RequestInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpCache(application: Application): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(application.cacheDir, cacheSize)
    }


    @Singleton
    @Provides
    fun provideGsonConfig(): Gson {
        return GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .setPrettyPrinting()
            .create()
    }


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WebLinks.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(provideGsonConfig()))
            .client(provideOkHttpClient())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MercariService {
        return retrofit.create(MercariService::class.java)
    }

}