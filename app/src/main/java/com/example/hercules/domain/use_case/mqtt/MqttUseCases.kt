package com.example.hercules.domain.use_case.mqtt

data class MqttUseCases(
    val connectMqttUseCase: ConnectMqttUseCase,
    val disconnectMqttUseCase: DisconnectMqttUseCase,
    val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    val getMessageUseCase: GetMessageUseCase,
    val publishMqttUseCase: PublishMqttUseCase,
)