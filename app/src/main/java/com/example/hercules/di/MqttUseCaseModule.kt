package com.example.hercules.di

import com.example.hercules.domain.use_case.mqtt.MqttInteractor
import com.example.hercules.domain.use_case.mqtt.MqttUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MqttUseCaseModule {
    @Binds
    abstract fun provideMqttUseCase(mqttInteractor: MqttInteractor): MqttUseCase
}