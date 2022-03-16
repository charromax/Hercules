/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.example.hercules.domain.model.TotemType
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import org.joda.time.Instant

class MqttManualParser {
    companion object {
        val FIELDS = JsonReader.Options.of("type", "status", "state", "payload")
    }

    fun parse(json: JsonReader, topic: String): TotemResponse {
        json.beginObject()
        val response = TotemResponse()
        while (json.hasNext()) {
            when (json.selectName(FIELDS)) {
                0 -> response.type = TotemType.valueOf(json.nextString())
                1 -> response.status = json.nextBoolean()
                2 -> response.state = json.nextBoolean()
                3 -> buildPayload(json, response)
                else -> {
                    json.skipName()
                    json.skipValue()
                }
            }
        }
        json.endObject()
        return response
    }

    private fun buildPayload(json: JsonReader, response: TotemResponse) {
        json.beginObject()
        when (response.type) {
            TotemType.WATER_PUMP -> response.payload = WaterPumpPayload.buildPayload(json)
            TotemType.MAG_SENSOR -> response.payload = MagSensorPayload.buildPayload(json)
            TotemType.AMBIENT_SENSOR -> response.payload = AmbientSensorPayload.buildPayload(json)
            else -> throw JsonDataException("Invalid or missing totem type")
        }
        json.endObject()
    }
}


data class TotemResponse(
    val createdAt: Long = Instant.now().millis,
    var topic: String = "",
    var type: TotemType? = null,
    var status: Boolean = false,
    var state: Boolean = false,
    var payload: BaseTotemPayload? = null
)

abstract class BaseTotemPayload




