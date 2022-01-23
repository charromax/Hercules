package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.domain.utils.Failure
import com.example.hercules.domain.utils.Success
import javax.inject.Inject

class UnsubscribeTopicUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(topic: String): String {
        when (val result = repo.unSubscribe(topic)) {
            is Failure -> throw result.error
            is Success -> return result.value
        }
    }
}