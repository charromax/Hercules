package com.example.hercules.data.repository

import android.content.Context
import com.example.hercules.domain.model.Message
import com.example.hercules.presentation.utils.Result

interface MqttRepository {
    suspend fun connect(context: Context, topics: List<String>): Result<String>
    suspend fun unSubscribe(topic:String): Result<String>
    suspend fun receiveMessages(): Result<Message>
    suspend fun publish(topic: String, data: String): Result<String>
    suspend fun disconnect(): Result<String>
}