package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.models.Message
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.domain.utils.Failure
import com.example.hercules.domain.utils.Success
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(): Message {
        when (val result = repo.receiveMessages()) {
            is Failure -> throw result.error
            is Success -> return result.value
        }
    }
}