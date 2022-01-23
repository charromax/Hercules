package com.example.hercules.domain.use_case.mqtt

import com.example.hercules.domain.models.Message
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.utils.Failure
import com.example.hercules.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    operator fun invoke(): Flow<Message> {
        return flow {
            when (val result = repo.receiveMessages()) {
                is Failure -> throw result.error
                is Success -> emit(result.value)
            }
        }

    }
}