/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.local

import androidx.room.*
import com.example.hercules.data.model.DBSensor
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Query("SELECT * FROM sensors")
    fun getAllSensors(): Flow<List<DBSensor>>

    @Query("SELECT * FROM sensors WHERE id = :id")
    suspend fun getSensorByID(id: Int): DBSensor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewSensor(sensor: DBSensor)

    @Delete
    suspend fun deleteSensor(sensor: DBSensor)

}