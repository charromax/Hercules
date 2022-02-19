/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.hercules.data.local.HerculesDB
import com.example.hercules.data.local.TotemDao
import com.example.hercules.data.remote.MqttDataSource
import com.example.hercules.data.remote.mqtt.MQTTClient
import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.data.repository.SensorsRepository
import com.example.hercules.domain.repository.MqttRepositoryImpl
import com.example.hercules.domain.repository.SensorRepositoryImpl
import com.example.hercules.domain.use_case.sensors.AddSensorUseCase
import com.example.hercules.domain.use_case.sensors.DeleteSensorUseCase
import com.example.hercules.domain.use_case.sensors.GetAllSensorsUseCase
import com.example.hercules.domain.use_case.sensors.SensorUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import javax.inject.Named
import javax.inject.Singleton

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val MQTT_SERVER_URI = "MQTT_SERVER_URI"

    @Provides
    @Named(MQTT_SERVER_URI)
    fun provideMQTTServerUri(): String = MQTTClient.BROKER_URL

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
    fun provideSensorDao(database: HerculesDB): TotemDao {
        return database.sensorDao()
    }

    @Provides
    @Singleton
    fun provideSensorRepository(dao: TotemDao): SensorsRepository {
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

    @Singleton
    @Provides
    fun provideMQTTAndroidClient(
        @ApplicationContext appContext: Context,
        @Named(MQTT_SERVER_URI) serverUri: String
    ): MqttAndroidClient {
        val clientId = MqttClient.generateClientId()
        return MqttAndroidClient(appContext, serverUri, clientId)
    }

    @Provides
    fun provideMQTTClient(mqttAndroidClient: MqttAndroidClient): MQTTClient =
        MQTTClient(mqttAndroidClient)

    @Provides
    fun provideMQTTDataSource(mqttClient: MQTTClient): MqttDataSource =
        MqttDataSource(mqttClient)

    @Provides
    @Singleton
    fun provideMqttRepository(
        mqttDataSource: MqttDataSource,
        scope: CoroutineScope
    ): MqttRepository {
        return MqttRepositoryImpl(mqttDataSource, scope)
    }
}