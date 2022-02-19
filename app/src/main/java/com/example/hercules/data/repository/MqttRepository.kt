package com.example.hercules.data.repository

import com.example.hercules.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MqttRepository {
    fun connectToMQTT(): Flow<Resource<Nothing>>

    fun disconnectFromMQTT(): Flow<Resource<Nothing>>

    fun isMQTTConnected(): Boolean

    fun subscribeToTopic(topic: String, qosLevel: Int?): Flow<Resource<Nothing>>

    fun unSubscribeFromTopic(topic: String): Flow<Resource<Nothing>>

    fun retrieveMessageFromPublisher(): Flow<Resource<String>>
}