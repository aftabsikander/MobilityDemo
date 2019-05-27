package com.aftabsikander.mercari.di

import android.app.Application
import com.aftabsikander.mercari.utilities.constants.DatabaseConstants
import com.zhuinden.monarchy.Monarchy
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

/**
 * Database layer which contains key component providers.
 */
@Module
class DatabaseModule {

    /**
     * Provides [RealmConfiguration] which will be accessible across the app also this will initialize
     * [Realm] database.
     * @param application [Application] instance
     *
     * @return [RealmConfiguration] instance with our pre configuration
     */
    @Singleton
    @Provides
    fun provideRealmConfiguration(application: Application): RealmConfiguration {
        Realm.init(application)
        val builder = RealmConfiguration.Builder()
            .name(DatabaseConstants.NAME)
            .schemaVersion(DatabaseConstants.SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
        return builder.build()

    }

    /**
     * Provides [Realm] which will be accessible across the app also this will set default configuration for our
     * database
     * @param config [RealmConfiguration] configuration
     *
     * @return [Realm] instance with our pre configuration
     */
    @Provides
    @Singleton
    fun provideRealmDatabase(config: RealmConfiguration): Realm {
        Realm.setDefaultConfiguration(config)
        return Realm.getDefaultInstance()
    }


    /**
     * Provides [Monarchy] which will be accessible across the app also this will set default configuration for our
     * [Realm] database.
     * @param config [RealmConfiguration] configuration
     *
     * @return [Monarchy] instance with our pre configuration
     */
    @Provides
    @Singleton
    fun monarchy(config: RealmConfiguration): Monarchy {
        Realm.setDefaultConfiguration(config)
        return Monarchy.Builder()
            .setRealmConfiguration(config)
            .build()
    }

}