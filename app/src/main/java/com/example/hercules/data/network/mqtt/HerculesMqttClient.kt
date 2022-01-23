package com.example.hercules.data.network.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class HerculesMqttClient {
    val TAG = "MQTTCLIENT"
    val BROKER_URL = "tcp://br5.maqiatto.com:1883"
    val USERNAME = "charr0max"
    val PASSWORD = "Mg412115"
    val ENCODING = "UTF-8"

    var mqttAndroidClient: MqttAndroidClient? = null
    private var clientID = ""
    var connectionStatus = false
    var retries = 0
    var listener: MqttClientActions? = null

    /**
     * attempt connection to broker
     */
    fun connect(context: Context, topics: List<String>, connectionListener: MqttClientActions.Connection?, subListener: MqttClientActions.Subscription) {
        try {
            clientID = MqttClient.generateClientId()
            mqttAndroidClient = MqttAndroidClient(context.applicationContext, BROKER_URL, clientID)
            val token = mqttAndroidClient?.connect(createMqttConnectOptions())
            token?.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i("Connection", "success ")
                    connectionStatus = true
                    // Give your callback on connection established here
                    connectionListener?.onConnected()
                    topics.forEach {
                        if (it.isNotEmpty()) subscribe(it, subListener)
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    //connectionStatus = false
                    Log.i("Connection", "failure")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                    connectionListener?.onConnectionRefused(retries)
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            e.printStackTrace()
            connectionListener?.onConnectionRefused(retries)
        }
    }

    /**
     * create options with username and password for mqtt broker
     */
    private fun createMqttConnectOptions() = MqttConnectOptions().apply {
        userName = USERNAME
        password = PASSWORD.toCharArray()
    }

    /**
     * subscribe to topic
     */
    fun subscribe(topic: String, listener: MqttClientActions.Subscription?) {
        val qos = 2 // Mention your qos value
        try {
            mqttAndroidClient?.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i(TAG, "onSuccess: SUBSCRIBED")
                    listener?.onSubcriptionSuccess(topic)
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Give your subscription failure callback here
                    listener?.onError(exception.message)
                }
            })
        } catch (e: MqttException) {
            // Give your subscription failure callback here
            listener?.onError(e.message)
        }
    }

    /**
     * end subscription for topic
     */
    fun unSubscribe(topic: String, listener: MqttClientActions.Unsubscription?) {
        try {
            val unsubToken = mqttAndroidClient?.unsubscribe(topic)
            unsubToken?.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Give your callback on unsubscribing here
                    listener?.onUnsubscribed(topic)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Give your callback on failure here
                    listener?.onError(exception.message)
                }
            }
        } catch (e: MqttException) {
            // Give your callback on failure here
            listener?.onError(e.message)
        }
    }

    /**
     * get messages on topics we are subscribed to
     */
    fun receiveMessages(listener: MqttClientActions.Message?) {
        mqttAndroidClient?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                connectionStatus = false
                // Give your callback on failure here
                listener?.onConnectionLost(retries)
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                try {
                    val data = String(message.payload, charset(ENCODING))
                    // data is the desired received message
                    // Give your callback on message received here
                    listener?.onMessageReceived(data, topic)
                } catch (e: Exception) {
                    // Give your callback on error here
                    listener?.onError(e.message)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                // Acknowledgement on delivery complete
                return
            }
        })
    }

    /**
     * publish message on [topic], pass [onError] for user feedback
     */
    fun publish(topic: String, data: String, onError: (String) -> Unit) {
        val encodedPayload: ByteArray
        try {
            encodedPayload = data.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            message.qos = 2
            message.isRetained = false
            mqttAndroidClient?.publish(topic, message)
        } catch (e: Exception) {
            Log.e(TAG, "publish: ERROR", e)
            onError?.invoke(e.message.toString())
        } catch (e: MqttException) {
            // Give Callback on error here
            Log.e(TAG, "publish: ERROR", e)
            onError?.invoke(e.message.toString())
        }
    }

    /**
     * disconnect from broker
     */
    fun disconnect(listener: MqttClientActions.Disconnect?) {
        try {
            val disconToken = mqttAndroidClient?.disconnect()
            disconToken?.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    connectionStatus = false
                    // Give Callback on disconnection here
                    Log.i(TAG, "onSuccess: DISCONNECTED")
                    resetRetries()
                    listener?.onDisconnect()
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Give Callback on error here
                    listener?.onError(exception.message)
                }
            }
        } catch (e: MqttException) {
            // Give Callback on error here
            listener?.onError(e.message)
        }

    }

    /**
     * increment retries counter
     */
    fun incrementRetries() = retries++

    /**
     * reset retries counter after successful connection
     */
    fun resetRetries() {
        retries = 0
    }
}

interface MqttClientActions {
    interface Connection {
        fun onConnected()
        fun onConnectionRefused(retries: Int)
    }

    interface Subscription {
        fun onError(error: String?)
        fun onSubcriptionSuccess(topic: String)
    }

    interface Unsubscription {
        fun onUnsubscribed(topic: String)
        fun onError(error: String?)
    }

    interface Message {
        fun onError(error: String?)
        fun onConnectionLost(retries: Int)
        fun onMessageReceived(message: String, topic: String)
    }

    interface Disconnect {
        fun onDisconnect()
        fun onError(error: String?)
    }
}

