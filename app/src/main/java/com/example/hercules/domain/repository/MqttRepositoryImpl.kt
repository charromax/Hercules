/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.repository

import com.example.hercules.data.remote.MqttDataSource
import com.example.hercules.data.remote.mqtt.MqttResponse
import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.presentation.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MqttRepositoryImpl @Inject constructor(
    private val mqttDataSource: MqttDataSource
) : MqttRepository {

    override fun connectToMQTT(): Flow<Resource<Nothing>> = flow {
        mqttDataSource.connectToMQTT().collect { response ->
            when (response) {
                is MqttResponse.Success -> emit(Resource.Success(response.message))
                is MqttResponse.Failure -> emit(
                    Resource.Failure(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun disconnectFromMQTT(): Flow<Resource<Nothing>> = flow {

        mqttDataSource.disconnectFromMQTT().collect { response ->
            when (response) {
                is MqttResponse.Success -> emit(Resource.Success(response.message))
                is MqttResponse.Failure -> emit(
                    Resource.Failure(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun isMQTTConnected(): Boolean = mqttDataSource.isMQTTConnected()

    override fun subscribeToTopic(
        topic: String,
        qosLevel: Int?
    ): Flow<Resource<Nothing>> = flow {

        mqttDataSource.subscribeToTopic(topic, qosLevel).collect { response ->
            when (response) {
                is MqttResponse.Success -> emit(Resource.Success(response.message))
                is MqttResponse.Failure -> emit(
                    Resource.Failure(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun unSubscribeFromTopic(topic: String): Flow<Resource<Nothing>> = flow {
        mqttDataSource.unSubscribeFromTopic(topic).collect { response ->
            when (response) {
                is MqttResponse.Success -> emit(Resource.Success(response.message))
                is MqttResponse.Failure -> emit(
                    Resource.Failure(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun retrieveMessageFromPublisher(): Flow<Resource<String>> = flow {

        mqttDataSource.retrieveMessageFromPublisher().collect { response ->
            when (response) {
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.ConnectionLost -> emit(Resource.ConnectionLost(response.exception))
                is MqttResponse.MessageReceived -> emit(
                    Resource.MessageReceived(
                        response.data,
                        response.topic
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun publishMessage(topic: String, message: String): Flow<Resource<Nothing>> = flow {
        mqttDataSource.publishMessageInTopic(topic, message).collect { response ->
            when (response) {
                is MqttResponse.Success -> emit(Resource.Success(response.message))
                is MqttResponse.Failure -> emit(
                    Resource.Failure(
                        response.exception,
                        response.message
                    )
                )
                is MqttResponse.Error -> emit(Resource.Error(response.exception, response.message))
                is MqttResponse.UnknownError -> emit(
                    Resource.UnknownError(
                        response.exception,
                        response.message
                    )
                )
                else -> throw IllegalStateException("Undefined State")
            }
        }

    }.flowOn(Dispatchers.IO)
}