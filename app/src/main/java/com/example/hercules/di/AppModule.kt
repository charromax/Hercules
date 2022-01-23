package com.example.hercules.di

import android.app.Application
import androidx.room.Room
import com.example.hercules.data.local.HerculesDB
import com.example.hercules.data.local.SensorDao
import com.example.hercules.data.network.mqtt.HerculesMqttClient
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.domain.repository.MqttRepositoryImpl
import com.example.hercules.domain.repository.SensorRepositoryImpl
import com.example.hercules.domain.repository.SensorsRepository
import com.example.hercules.domain.use_case.mqtt.*
import com.example.hercules.domain.use_case.sensors.AddSensorUseCase
import com.example.hercules.domain.use_case.sensors.DeleteSensorUseCase
import com.example.hercules.domain.use_case.sensors.GetAllSensorsUseCase
import com.example.hercules.domain.use_case.sensors.SensorUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.eclipse.paho.client.mqttv3.MqttClient
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
    @Singleton
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

    @Provides
    @Singleton
    fun provideMqttClient(): HerculesMqttClient = HerculesMqttClient()

    @Provides
    @Singleton
    fun provideMqttRepository(client: HerculesMqttClient): MqttRepository {
        return MqttRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideMqttUseCases(repository: MqttRepository): MqttUseCases {
        return MqttUseCases(
            connectMqttUseCase = ConnectMqttUseCase(repository),
            disconnectMqttUseCase = DisconnectMqttUseCase(repository),
            getMessageUseCase = GetMessageUseCase(repository),
            publishMqttUseCase = PublishMqttUseCase(repository),
            unsubscribeTopicUseCase = UnsubscribeTopicUseCase(repository)
        )
    }
}