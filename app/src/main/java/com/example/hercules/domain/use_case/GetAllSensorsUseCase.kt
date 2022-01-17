package com.example.hercules.domain.use_case


import com.example.hercules.domain.models.Sensor
import com.example.hercules.domain.repository.SensorsRepository
import com.example.hercules.domain.utils.Order
import com.example.hercules.domain.utils.SensorOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllSensorsUseCase @Inject constructor(
    private val repo: SensorsRepository
) {

    operator fun invoke(sensorOrder: SensorOrder = SensorOrder.Topic(Order.Ascending)): Flow<List<Sensor>> {
            return repo.getAllSensors().map { sensors ->
                when(sensorOrder.order) {
                    Order.Ascending -> {
                        when(sensorOrder) {
                            is SensorOrder.Date -> sensors.sortedBy { it.createdAt }
                            is SensorOrder.Topic -> sensors.sortedBy { it.topic }
                            is SensorOrder.State -> sensors.sortedBy { it.state }
                        }
                    }
                    Order.Descending -> {
                        when(sensorOrder) {
                            is SensorOrder.Date -> sensors.sortedByDescending { it.createdAt }
                            is SensorOrder.Topic -> sensors.sortedByDescending { it.topic }
                            is SensorOrder.State -> sensors.sortedByDescending { it.state }
                        }
                    }
                }
            }
    }
}