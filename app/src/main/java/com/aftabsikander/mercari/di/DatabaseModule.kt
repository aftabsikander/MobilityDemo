package com.aftabsikander.mercari.di

import android.app.Application
import com.aftabsikander.mercari.utilities.constants.DatabaseConstants
import com.zhuinden.monarchy.Monarchy
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class DatabaseModule {

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

    @Provides
    @Singleton
    internal fun provideRealmDatabase(config: RealmConfiguration): Realm {
        Realm.setDefaultConfiguration(config)
        return Realm.getDefaultInstance()
    }

    @Provides
    @Singleton
    fun monarchy(config: RealmConfiguration): Monarchy {
        Realm.setDefaultConfiguration(config)
        return Monarchy.Builder()
            .setRealmConfiguration(config)
            .build()
    }

}