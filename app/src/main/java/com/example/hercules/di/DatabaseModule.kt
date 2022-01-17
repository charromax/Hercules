package com.example.hercules.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hercules.data.local.HerculesDB
import com.example.hercules.data.local.SensorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideSensorDao(database: HerculesDB): SensorDao {
        return database.sensorDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): HerculesDB {
        return Room.databaseBuilder(
            appContext,
            HerculesDB::class.java,
            "hercules.db"
        ).build()
    }

}