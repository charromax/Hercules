package com.example.hercules.data.network.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

const val TAG = "MQTTCLIENT"
const val BROKER_URL = "tcp://br5.maqiatto.com:1883"
const val USERNAME = "charr0max"
const val PASSWORD = "Mg412115"
const val ENCODING = "UTF-8"

class CustomMqttClient(private val context: Context) {

    val client by lazy {
        val clientId = MqttClient.generateClientId()
        MqttAndroidClient(context, BROKER_URL,
            clientId)
    }

    companion object {
        const val TAG = "MqttClient"
    }

    /**
     * create options with username and password for mqtt broker
     */
    private fun createMqttConnectOptions() = MqttConnectOptions().apply {
        userName = USERNAME
        password = PASSWORD.toCharArray()
    }

    fun connect(topics: Array<String>? = null,
                messageCallBack: ((topic: String, message: MqttMessage) -> Unit)? = null) {
        try {
            client.connect(createMqttConnectOptions())
            client.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                    topics?.forEach {
                        subscribeTopic(it)
                    }
                    Log.d(TAG, "Connected to: $serverURI")
                }

                override fun connectionLost(cause: Throwable) {
                    Log.d(TAG, "The Connection was lost.")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    Log.d(TAG, "Incoming message from $topic: " + message.toString())
                    messageCallBack?.invoke(topic, message)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    //TODO: use this callback
                }
            })


        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publishMessage(topic: String, msg: String) {

        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            client.publish(topic, message.payload, 0, true)
            Log.d(TAG, "$msg published to $topic")
        } catch (e: MqttException) {
            Log.d(TAG, "Error Publishing to $topic: " + e.message)
            e.printStackTrace()
        }

    }

    fun subscribeTopic(topic: String, qos: Int = 0) {
        client.subscribe(topic, qos).actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.d(TAG, "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.d(TAG, "Failed to subscribe to $topic")
                exception.printStackTrace()
            }
        }
    }

    fun close() {
        client.apply {
            unregisterResources()
            close()
        }
    }
}