package com.example.hercules.domain.repository

import android.content.Context
import com.example.hercules.domain.models.Message
import com.example.hercules.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface MqttRepository {
    suspend fun connect(context: Context, topics: List<String>): Result<String>
    suspend fun unSubscribe(topic:String): Result<String>
    suspend fun receiveMessages(): Result<Message>
    suspend fun publish(topic: String, data: String): Result<String>
    suspend fun disconnect(): Result<String>
}