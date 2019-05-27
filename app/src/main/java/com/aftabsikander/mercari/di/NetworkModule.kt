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


/**
 * Network layer which contains key component providers.
 */
@Module
class NetworkModule {

    /**
     * Provides [OkHttpClient] instance with pre configuration setup.
     *
     * @return [OkHttpClient] instance
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(AppConstants.WEB_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.WEB_READ_TIMEOUT, TimeUnit.SECONDS)
            //.writeTimeout(AppConstants.WEB_WRITE_FILE_TIMEOUT, TimeUnit.MINUTES)
            .addInterceptor(RequestInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    /**
     * Provides [Cache] instance for our application which would be used by [OkHttpClient] client.
     * @param application [Application] instance which will used to retrieve our cache directory of the app.
     *
     * @return [Cache] Instance.
     */
    @Provides
    @Singleton
    fun providesOkHttpCache(application: Application): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(application.cacheDir, cacheSize)
    }


    /**
     * Provides [Gson] instance with pre configuration values and it would be used by [Retrofit] client.
     *
     * @return [Gson] instance
     */
    @Singleton
    @Provides
    fun provideGsonConfig(): Gson {
        return GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .setPrettyPrinting()
            .create()
    }


    /**
     * Provides [Retrofit] instance with pre configuration values and it would be used across the application.
     *
     * @return [Retrofit] instance
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WebLinks.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(provideGsonConfig()))
            .client(provideOkHttpClient())
            .build()
    }


    /**
     * Provides [MercariService] service End point instance which will be used by [Retrofit]
     * internally for creating service calls.
     * @param retrofit [Retrofit] client object
     *
     * @return [MercariService] instance
     */
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MercariService {
        return retrofit.create(MercariService::class.java)
    }

}