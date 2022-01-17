package com.example.hercules.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hercules.domain.models.Sensor

// Annotates class to be a Room Database with a table (entity) of the Sensor class
@Database(entities = arrayOf(Sensor::class), version = 1, exportSchema = false)
abstract class HerculesDB : RoomDatabase() {

    abstract fun sensorDao(): SensorDao

}