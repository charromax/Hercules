/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote

import com.example.hercules.data.remote.mqtt.MQTTClient
import com.example.hercules.data.remote.mqtt.MqttResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.eclipse.paho.client.mqttv3.MqttException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MqttDataSource @Inject constructor(private val client: MQTTClient) {

    fun connectToMQTT(): Flow<MqttResponse<Nothing>> = client
        .connectToMQTT()
        .catch { exception ->
            handleException<Nothing>(exception = exception).also { emit(it) }
        }

    fun disconnectFromMQTT(): Flow<MqttResponse<Nothing>> = client
        .disconnectMQTT()
        .catch { exception ->
            handleException<Nothing>(exception = exception).also { emit(it) }
        }

    fun subscribeToTopic(topic: String, qosLevel: Int?): Flow<MqttResponse<Nothing>> = client
        .subscribeToTopic(topic, qosLevel)
        .catch { exception ->
            handleException<Nothing>(exception = exception).also { emit(it) }
        }

    fun unSubscribeFromTopic(topic: String): Flow<MqttResponse<Nothing>> = client
        .unSubscribeTopic(topic)
        .catch { exception ->
            handleException<Nothing>(exception = exception).also { emit(it) }
        }

    fun publishMessageInTopic(topic: String, message: String): Flow<MqttResponse<Nothing>> = client
        .publish(topic, message)
        .catch { exception ->
            handleException<Nothing>(exception = exception).also { emit(it) }
        }

    fun isMQTTConnected(): Boolean = client.isMQTTConnected()

    fun retrieveMessageFromPublisher(): Flow<MqttResponse<String>> = client
        .retrieveMessageFromPublisher()
        .catch { exception ->
            handleException<String>(exception = exception).also { emit(it) }
        }

    private fun <T> handleException(
        exception: Throwable
    ): MqttResponse<T> = when (exception) {
        is MqttException -> MqttResponse.Error(exception, exception.message)
        else -> MqttResponse.UnknownError(exception, exception.message)
    }
}