/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hercules.domain.model.BasicInstructionSet
import com.example.hercules.domain.use_case.mqtt.MqttUseCase
import com.example.hercules.domain.use_case.sensors.TotemUseCases
import com.example.hercules.presentation.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect

@HiltWorker
class MqttWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val totemUseCases: TotemUseCases,
    private val mqttUseCase: MqttUseCase
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Do the work here
        val totemId = inputData.getInt(TOTEM_ID, -1)
        val instruction = inputData.getString(INSTRUCTION)

        // if there's such a totem and instruction is one of the set then proceed
        if (validateArguments(totemId, instruction)) {
            totemUseCases.getTotemByIdUseCase(totemId)?.let {
                mqttUseCase.connectToMQTT().collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            mqttUseCase.publishMessage(
                                it.topic,
                                instruction ?: ""
                            )
                            Result.success()
                        }
                        else -> Result.failure()
                    }
                }
            }
            return Result.failure()
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun validateArguments(totemId: Int, instruction: String?) =
        totemId > -1 && !instruction.isNullOrEmpty() && instruction in BasicInstructionSet.values()
            .map { it.name }

    companion object {
        const val TOTEM_ID = "TOTEM_ID"
        const val INSTRUCTION = "INSTRUCTION"
    }

}
