/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.domain.repository

import com.example.hercules.data.local.TotemDao
import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.repository.SensorsRepository
import com.example.hercules.domain.model.Sensor
import com.example.hercules.domain.model.Totem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(private val dao: TotemDao): SensorsRepository {
    override fun getAllTotems(): Flow<List<Totem>> {
        return dao.getAllTotems().map { list ->
            list.map { it.toTotem() }
        }
    }

    override suspend fun getTotemByID(id: Int): Totem? {
        return dao.getTotemByID(id)?.toTotem()
    }

    override suspend fun addNewTotem(totem: DBTotem) {
        dao.addNewTotem(totem)
    }

    override suspend fun deleteSensor(totem: DBTotem) {
        dao.deleteTotem(totem)
    }

}