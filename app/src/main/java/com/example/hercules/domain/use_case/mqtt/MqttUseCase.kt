package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MqttUseCase {

    fun connectToMQTT(): Flow<Resource<Nothing>>

    fun disconnectFromMQTT(): Flow<Resource<Nothing>>

    fun isMQTTConnected(): Boolean

    fun subscribeToTopic(topic: String, qosLevel: Int?): Flow<Resource<Nothing>>

    fun retrieveMessageFromPublisher(): Flow<Resource<String>>

    fun unSubscribeFromTopic(topic: String): Flow<Resource<Nothing>>

}