package com.example.hercules.data.remote.mqtt

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.sql.Timestamp
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MQTTClient @Inject constructor(private val client: MqttAndroidClient) {

    companion object {
        const val QOS_LEVEL_1 = 1
        val TAG = "MQTTCLIENT"
        val BROKER_URL = "tcp://br5.maqiatto.com:1883"
        val USERNAME = "charr0max"
        val PASSWORD = "Mg412115"
        val ENCODING = "UTF-8"
    }

    fun connectToMQTT(): Flow<MqttResponse<Nothing>> = callbackFlow {
        client.connect(createMqttConnectOptions()).apply {
            actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG,"[SUCCESS] connected to ${client.serverURI}")
                    trySend(MqttResponse.Success("Successfully connected to ${client.serverURI}")).isSuccess
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "[FAILURE] connected to ${client.serverURI} caused by $exception")
                    trySend(MqttResponse.Failure(exception, client.serverURI))
                }
            }
        }
        awaitClose { close() }
    }

    /**
     * create options with username and password for mqtt broker
     */
    private fun createMqttConnectOptions() = MqttConnectOptions().apply {
        userName = USERNAME
        password = PASSWORD.toCharArray()
    }

    fun retrieveMessageFromPublisher(): Flow<MqttResponse<String>> = callbackFlow {
        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG,"[LOST CONNECTION] caused by $cause")
                offer(MqttResponse.ConnectionLost(cause))
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val currentTimestamp = Timestamp(System.currentTimeMillis())
                Log.d(TAG,"[MESSAGE RECEIVED] from topic : $topic with message : $message at $currentTimestamp")
                offer(MqttResponse.MessageReceived(message.toString(), topic.toString()))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG,"[DELIVERY COMPLETE] from ${client.serverURI}")
            }
        })

        awaitClose { close() }

    }

    fun subscribeToTopic(
        topic: String,
        qosLevel: Int?
    ): Flow<MqttResponse<Nothing>> = callbackFlow {
        client.subscribe(
            topic,
            qosLevel ?: QOS_LEVEL_1,
            null,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG,"[SUCCESS] subscribe on topic : $topic")
                    offer(MqttResponse.Success("Successfully subscribe to topic $topic"))
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG,"[FAILURE] subscribe on topic : $topic caused by $exception")
                    offer(MqttResponse.Failure(exception, topic))
                }
            }
        )

        awaitClose { close() }
    }

    fun unSubscribeTopic(topic: String): Flow<MqttResponse<Nothing>> = callbackFlow {
        client.unsubscribe(topic, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG,"[SUCCESS] unsubscribe on topic : $topic")
                offer(MqttResponse.Success(topic))
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(TAG,"[FAILURE] unsubscribe on topic : $topic caused by $exception")
                offer(MqttResponse.Failure(exception, topic))
            }
        })

        awaitClose { close() }
    }

    fun disconnectMQTT(): Flow<MqttResponse<Nothing>> = callbackFlow {
        client.disconnect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG,"[SUCCESS] disconnected from ${client.serverURI}")
                offer(MqttResponse.Success("Successfully disconnected from ${client.serverURI}"))
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(TAG,"[FAILURE] disconnected to ${client.serverURI} caused by $exception")
                offer(MqttResponse.Failure(exception, client.serverURI))
            }
        })

        awaitClose { close() }
    }

    fun isMQTTConnected(): Boolean = client.isConnected

}