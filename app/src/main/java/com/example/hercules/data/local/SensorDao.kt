package com.example.hercules.data.local

import androidx.room.*
import com.example.hercules.domain.models.Sensor
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Query("SELECT * FROM sensors")
    fun getAllSensors(): Flow<List<Sensor>>

    @Query("SELECT * FROM sensors WHERE id = :id")
    suspend fun getSensorByID(id: Int): Sensor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewSensor(sensor: Sensor)

    @Delete
    suspend fun deleteSensor(sensor: Sensor)

}