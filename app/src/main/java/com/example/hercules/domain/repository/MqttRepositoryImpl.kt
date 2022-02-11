/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.repository

import android.content.Context
import com.example.hercules.R
import com.example.hercules.data.network.mqtt.HerculesMqttClient
import com.example.hercules.data.network.mqtt.MqttClientActions
import com.example.hercules.data.repository.MqttRepository
import com.example.hercules.domain.model.HerculesExceptions.ConnectionLostException
import com.example.hercules.domain.model.HerculesExceptions.UnableToConnectException
import com.example.hercules.domain.model.Message
import com.example.hercules.presentation.utils.Failure
import com.example.hercules.presentation.utils.Result
import com.example.hercules.presentation.utils.Success
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MqttRepositoryImpl @Inject constructor(private val client: HerculesMqttClient) :
    MqttRepository {

    override suspend fun connect(
        context: Context,
        topics: List<String>
    ): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            client.connect(context, topics, object : MqttClientActions.Connection {
                override fun onConnected() {
                    client.resetRetries()
                    if (!continuation.isActive) continuation.resume(Success(context.getString(R.string.connection_success)))
                }

                override fun onConnectionRefused(retries: Int) {
                    if (!continuation.isActive) continuation.resume(
                        Failure(
                            Exception(
                                context.getString(
                                    R.string.rejected_connection
                                )
                            )
                        )
                    )
                }

            }, object : MqttClientActions.Subscription {
                override fun onError(error: String?) {
                    if (!continuation.isActive) continuation.resume(Failure(Exception(error)))
                }

                override fun onSubcriptionSuccess(topic: String) {
                    if (!continuation.isActive) continuation.resume(Success(topic))
                }

            })
        }
    }

    override suspend fun unSubscribe(topic: String): Result<String> {
        return suspendCoroutine { cont ->
            client.unSubscribe(topic, object : MqttClientActions.Unsubscription {
                override fun onUnsubscribed(topic: String) {
                    cont.resume(Success(topic))
                }

                override fun onError(error: String?) {
                    cont.resume(Failure(Exception(error)))
                }
            })
        }
    }

    override suspend fun receiveMessages(): Result<Message> {
        return suspendCoroutine { cont ->
            client.receiveMessages(object : MqttClientActions.Message {
                override fun onError(error: String?) {
                    cont.resume(Failure(Exception(error)))
                }

                override fun onConnectionLost(retries: Int) {
                    if (retries < 5) {
                        client.incrementRetries()
                        cont.resume(Failure(ConnectionLostException("Reconectando...")))
                    } else {
                        cont.resume(Failure(UnableToConnectException("Error desconocido, conexión perdida")))
                    }
                }

                override fun onMessageReceived(message: String, topic: String) {
                    cont.resume(Success(Message(topic = topic, message = message)))
                }

            })
        }
    }

    override suspend fun publish(topic: String, data: String): Result<String> {
        return suspendCoroutine { cont ->
            client.publish(topic, data) { error ->
                cont.resume(Failure(Exception(error)))
            }
            cont.resume(Success("Mensaje publicado"))
        }
    }

    override suspend fun disconnect(): Result<String> {
        return suspendCoroutine { cont ->
            client.disconnect(object : MqttClientActions.Disconnect {
                override fun onDisconnect() {
                    cont.resume(Success("Hercules desconectado"))
                }

                override fun onError(error: String?) {
                    cont.resume(Failure(Exception(error)))
                }

            })
        }
    }
}