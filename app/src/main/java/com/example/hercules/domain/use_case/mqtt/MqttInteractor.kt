/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * interactor class for MQTT communication
 */
class MqttInteractor @Inject constructor(
    private val mqttRepository: MqttRepository
) : MqttUseCase {

    override fun connectToMQTT(): Flow<Resource<Nothing>> = mqttRepository.connectToMQTT()

    override fun disconnectFromMQTT(): Flow<Resource<Nothing>> = mqttRepository.disconnectFromMQTT()

    override fun isMQTTConnected() = mqttRepository.isMQTTConnected()

    override fun subscribeToTopic(topic: String, qosLevel: Int?) =
        mqttRepository.subscribeToTopic(topic, qosLevel)

    override fun retrieveMessageFromPublisher() = mqttRepository.retrieveMessageFromPublisher()
    override fun publishMessage(topic: String, message: String): Flow<Resource<Nothing>> =
        mqttRepository.publishMessage(topic, message)

    override fun unSubscribeFromTopic(topic: String): Flow<Resource<Nothing>> =
        mqttRepository.unSubscribeFromTopic(topic)
}