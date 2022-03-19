/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.mqtt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hercules.data.remote.response.TotemResponse
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

private const val UNDEFINED = "Undefined State"
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
            MqttEvents.RefreshSubscription -> shouldRefreshSubscriptions()
        }
    }

    private fun shouldRefreshSubscriptions() {
        _mqttState.value = mqttState.value.copy(
            isMqttSubscribed = false
        )
    }

    fun clearSnack() {
        _mqttState.value = mqttState.value.copy(snack = null)
    }

    fun clearError() {
        _mqttState.value = mqttState.value.copy(error = null)
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
                    is Resource.Failure -> updateErrorState("[Error] Publish message in topic $topic")
                    else -> {
                        throw java.lang.IllegalStateException(UNDEFINED)
                    }
                }
            }
        }
    }


    /**
     * connect to mqtt broker
     */
    private fun connect() {
        connectionMqttJob?.cancel()
        viewModelScope.launch {
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
                    else -> throw IllegalStateException(UNDEFINED)
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
                    is Resource.Error -> updateErrorState(response.exception)
                    is Resource.UnknownError -> updateErrorState(response.exception)
                    else -> throw IllegalStateException(UNDEFINED)
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
                    else -> throw IllegalStateException(UNDEFINED)
                }
            }
        }
    }

    private fun addSuccessfulTopic(topic: String) {
        subscriptionsMap[topic] = true
        if (topicList.last() == topic)
            _mqttState.value = mqttState.value.copy(
                subscriptionMap = subscriptionsMap,
                isMqttSubscribed = true
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

    private fun onMessageReceived(messageReceived: Resource.MessageReceived<TotemResponse>) {
        _mqttState.value = mqttState.value.copy(

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
                    is Resource.UnknownError -> updateErrorState("[UnknownError] unsubscribe from topic $topic")
                    else -> throw IllegalStateException(UNDEFINED)
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
                    is Resource.UnknownError -> updateErrorState("[UnknownError] disconnect from MQTT")
                    else -> throw IllegalStateException(UNDEFINED)
                }
            }
        }
    }

    private fun updateErrorState(e: Throwable) {
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
