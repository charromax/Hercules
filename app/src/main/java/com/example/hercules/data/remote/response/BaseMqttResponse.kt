/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.data.remote.response

import com.example.hercules.domain.model.TotemType
import com.squareup.moshi.JsonReader
import okio.Buffer
import org.joda.time.Instant

object MqttManualParser {

    val TYPE = "type"
    val IS_ACTIVE = "is_active"
    val IS_POWER_ON = "is_power_on"
    val PAYLOAD = "payload"
    val FIELDS = JsonReader.Options.of(TYPE, IS_ACTIVE, IS_POWER_ON, PAYLOAD)

    fun parse(data: String, topic: String): TotemResponse {
        val response = TotemResponse().apply { this.topic = topic }
        var prePayload = ""
        val json = JsonReader.of(Buffer().writeUtf8(data))
        json.beginObject()
        if (json.selectName(JsonReader.Options.of(TYPE)) != -1) response.type =
            TotemType.valueOf(json.nextString())
        if (json.selectName(JsonReader.Options.of(IS_ACTIVE)) != -1) response.isActive =
            json.nextBoolean()
        if (json.selectName(JsonReader.Options.of(IS_POWER_ON)) != -1) response.isPowerOn =
            json.nextBoolean()
        if (json.selectName(JsonReader.Options.of(PAYLOAD)) != -1) {
            prePayload = json.nextString()
        }
        json.endObject()
        if (prePayload.isNotEmpty()) {
            response.payload = prePayload.replace("*", "\"")
        }
        return response
    }

    fun buildPayload(currentState: String?, type: TotemType): BaseTotemPayload? {
        if (currentState.isNullOrEmpty() || currentState == "null") return null
        val payload: BaseTotemPayload?
        val json = JsonReader.of(Buffer().writeUtf8(currentState))
        payload = when (type) {
            TotemType.WATER_PUMP -> WaterPumpPayload.buildPayload(json)
            TotemType.MAG_SENSOR -> MagSensorPayload.buildPayload(json)
            TotemType.AMBIENT_SENSOR -> AmbientSensorPayload.buildPayload(json)
        }
        return payload
    }
}


data class TotemResponse(
    val createdAt: Long = Instant.now().millis,
    var topic: String = "",
    var type: TotemType? = null,
    var isActive: Boolean = false,
    var isPowerOn: Boolean = false,
    var payload: String? = null
)

abstract class BaseTotemPayload




