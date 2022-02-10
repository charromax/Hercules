package com.example.hercules.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.hercules.data.local.HerculesDB
import com.example.hercules.data.local.SensorDao
import com.example.hercules.domain.repository.SensorRepositoryImpl
import com.example.hercules.domain.repository.SensorsRepository
import com.example.hercules.domain.use_case.AddSensorUseCase
import com.example.hercules.domain.use_case.DeleteSensorUseCase
import com.example.hercules.domain.use_case.GetAllSensorsUseCase
import com.example.hercules.domain.use_case.SensorUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): HerculesDB {
        return Room.databaseBuilder(
            app.applicationContext,
            HerculesDB::class.java,
            "hercules.db"
        ).build()
    }

    @Provides
    fun provideSensorDao(database: HerculesDB): SensorDao {
        return database.sensorDao()
    }

    @Provides
    @Singleton
    fun provideSensorRepository(dao: SensorDao): SensorsRepository {
        return SensorRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSensorUseCases(repository: SensorsRepository): SensorUseCases {
        return SensorUseCases(
            getAllSensorsUseCase = GetAllSensorsUseCase(repository),
            deleteSensorUseCase = DeleteSensorUseCase(repository),
            saveNewSensor = AddSensorUseCase(repository)
        )
    }
}