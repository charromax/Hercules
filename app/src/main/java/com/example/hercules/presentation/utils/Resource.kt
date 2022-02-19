package com.example.hercules.presentation.utils

import org.eclipse.paho.client.mqttv3.MqttException

sealed class Resource<out R> {
    data class Success(val message: String) : Resource<Nothing>()
    data class Failure(val exception: Exception?, val message: String?) : Resource<Nothing>()
    data class Error(val exception: MqttException, val message: String?) : Resource<Nothing>()
    data class UnknownError(val exception: Exception, val message: String?) : Resource<Nothing>()
    data class ConnectionLost(val exception: Exception?) : Resource<Nothing>()
    data class MessageReceived<out T>(val data: T, val topic: String) : Resource<T>()
}