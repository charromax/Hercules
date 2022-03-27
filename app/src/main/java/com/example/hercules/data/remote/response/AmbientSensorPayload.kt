/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

data class AmbientSensorPayload(
    var temperature: Double? = null,
    var humidity: Double? = null
) : BaseTotemPayload() {
    companion object {
        fun buildPayload(
            json: JsonReader
        ): BaseTotemPayload? {
            json.beginObject()
            val payload = AmbientSensorPayload()
            when (json.selectName(JsonReader.Options.of("temp", "hum"))) {
                0 -> payload.temperature = json.nextDouble()
                1 -> payload.humidity = json.nextDouble()
                else -> throw JsonDataException("Invalid data")
            }
            json.endObject()
            return if (payload.humidity != null && payload.temperature != null) payload else null
        }
    }
}