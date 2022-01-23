package com.example.hercules.domain.use_case.mqtt

import android.content.Context
import com.example.hercules.domain.repository.MqttRepository
import com.example.hercules.utils.Failure
import com.example.hercules.utils.Success
import javax.inject.Inject

class ConnectMqttUseCase @Inject constructor(
    private val repo: MqttRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(context: Context, topics:List<String>): String {
        when (val result = repo.connect(context, topics)) {
            is Failure -> throw result.error
            is Success -> return result.value
        }
    }
}