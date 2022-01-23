package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.models.Message
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.domain.utils.Failure
import com.example.hercules.domain.utils.Success
import javax.inject.Inject

/**
 * publish orders to mqtt broker
 */
class PublishMqttUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(topic: String, data: String): Message {
        if (topic.isEmpty()) throw Exception("Topic vacio")
        if (data.isEmpty()) throw Exception("Mensaje vacio")
        when (val result = repo.publish(topic, data)) {
            is Failure -> throw result.error
            is Success -> return Message(topic = topic, message = data)
        }
    }
}