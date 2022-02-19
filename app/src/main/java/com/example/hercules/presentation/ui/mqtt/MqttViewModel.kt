/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.domain.model.Message
import com.example.hercules.domain.use_case.mqtt.MqttUseCase
import com.example.hercules.presentation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val mqttUseCase: MqttUseCase
) : ViewModel() {
    private var connectionMqttJob: Job? = null
    private val _mqttState = MutableStateFlow(MqttState())
    val mqttState: StateFlow<MqttState> = _mqttState
    val topicList = mutableListOf<String>()
    private val subscriptionsMap = mutableMapOf<String, Boolean>()

    fun onEvent(event: MqttEvents) {
        when (event) {
            MqttEvents.StartConnectionRequest -> connect()
            is MqttEvents.PublishMessage -> publish(event.topic, event.message)
            is MqttEvents.SubscribeToTopic -> subscribeToTopic(event.topic, 1)
        }
    }

    private fun publish(topic: String, message: String) {
        viewModelScope.launch {
            mqttUseCase.publishMessage(topic, message).collect { response ->
                when (response) {
                    is Resource.Success -> _mqttState.value = mqttState.value.copy(
                        lastMessageSent = Message(
                            topic = topic,
                            message = message
                        )
                    )
                    is Resource.UnknownError,
                    is Resource.Error,
                    is Resource.Failure -> _mqttState.value = mqttState.value.copy(
                        error = "[Error] Publish message in topic $topic"
                    )
                    else -> throw java.lang.IllegalStateException("Undefined State")
                }
            }
        }
    }


    /**
     * connect to mqtt broker
     */
    private fun connect() {
        connectionMqttJob?.cancel()
        connectionMqttJob = viewModelScope.launch {
            mqttUseCase.connectToMQTT().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _mqttState.value = mqttState.value.copy(
                            isMqttConnected = true
                        )
                        getMessageFromPublisher()
                    }
                    is Resource.Failure -> updateErrorState(
                        "[Failed] Connect to MQTT caused by ${response.exception?.message}"
                    )
                    is Resource.Error -> updateErrorState(
                        "[Error] Connect to MQTT caused by ${response.exception.message}"
                    )
                    is Resource.UnknownError -> updateErrorState(
                        "[UnknownError] Connect to MQTT caused by ${response.exception.message}"
                    )
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    private fun getMessageFromPublisher() {
        viewModelScope.launch {
            mqttUseCase.retrieveMessageFromPublisher().collectLatest { response ->
                when (response) {
                    is Resource.MessageReceived -> onMessageReceived(response)
                    is Resource.ConnectionLost -> response.exception?.let { updateErrorState(it) }
                    is Resource.Error -> response.exception?.let { updateErrorState(it) }
                    is Resource.UnknownError -> response.exception?.let { updateErrorState(it) }
                    else -> throw IllegalStateException("Undefined state")
                }

            }
        }
    }

    private fun subscribeToTopic(topic: String, qosLevel: Int?) {
        viewModelScope.launch {
            mqttUseCase.subscribeToTopic(topic, qosLevel).collect { response ->
                when (response) {
                    is Resource.Success -> addSuccessfulTopic(topic)
                    is Resource.Failure -> addFailedTopic(topic)
                    is Resource.Error -> addFailedTopic(topic)
                    is Resource.UnknownError -> addFailedTopic(topic)
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    private fun addSuccessfulTopic(topic: String) {
        subscriptionsMap[topic] = true
        if (topicList.last() == topic)
            _mqttState.value = mqttState.value.copy(
                subscriptionMap = subscriptionsMap
            )
    }

    private fun addFailedTopic(topic: String) {
        subscriptionsMap[topic] = false
        if (topicList.last() == topic)
            _mqttState.value = mqttState.value.copy(
                subscriptionMap = subscriptionsMap
            )
    }

    fun clearSubscriptionMap() {
        subscriptionsMap.clear()
        _mqttState.value = mqttState.value.copy(
            subscriptionMap = null
        )
    }

    private fun onMessageReceived(messageReceived: Resource.MessageReceived<String>) {
        _mqttState.value = mqttState.value.copy(
            lastMessageReceived = Message(
                topic = messageReceived.topic,
                message = messageReceived.data
            )
        )
    }

    fun unsubscribe(topic: String) {
        viewModelScope.launch {
            mqttUseCase.unSubscribeFromTopic(topic).collect { response ->
                when (response) {
                    is Resource.Success -> _mqttState.value =
                        mqttState.value.copy(snack = response.message)
                    is Resource.Failure,
                    is Resource.Error,
                    is Resource.UnknownError -> _mqttState.value = mqttState.value.copy(
                        error =
                        "[UnknownError] unsubscribe from topic $topic"
                    )
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            mqttUseCase.disconnectFromMQTT().collect { response ->
                when (response) {
                    is Resource.Success -> _mqttState.value =
                        mqttState.value.copy(snack = response.message)
                    is Resource.Failure,
                    is Resource.Error,
                    is Resource.UnknownError -> _mqttState.value = mqttState.value.copy(
                        error =
                        "[UnknownError] disconnect from MQTT"
                    )
                    else -> throw IllegalStateException("Undefined State")
                }
            }
        }
    }

    private fun updateErrorState(e: Exception) {
        _mqttState.value = mqttState.value.copy(
            error = e.message
        )
    }

    private fun updateErrorState(error: String) {
        _mqttState.value = mqttState.value.copy(
            error = error
        )
    }
}
