package com.example.hercules.data.remote.response

import com.example.hercules.domain.model.RangeChannel
import com.example.hercules.domain.model.RgbMode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonReader

data class RgbStripControlPayload(
    var mode: RgbMode = RgbMode.MANUAL,
    var speed: Int = 0,
    @field:Json(name = "range_channel")
    var rangeChannel: RangeChannel = RangeChannel.R,
    var red: Int = 255,
    var green: Int = 255,
    var blue: Int = 255,
) : BaseTotemPayload() {

    companion object {
        val MODE = "mode"
        val SPEED = "speed"
        val RANGE_CHANNEL = "range_channel"
        val RED = "red"
        val GREEN = "green"
        val BLUE = "blue"

        fun buildPayload(
            json: JsonReader
        ): BaseTotemPayload {
            json.beginObject()
            val payload = RgbStripControlPayload()
            if (json.selectName(JsonReader.Options.of(MODE)) > -1) payload.mode =
                RgbMode.valueOf(json.nextString().uppercase())
            if (json.selectName(JsonReader.Options.of(SPEED)) > -1) payload.speed = json.nextInt()
            if (json.selectName(JsonReader.Options.of(RED)) > -1) payload.speed = json.nextInt()
            if (json.selectName(JsonReader.Options.of(RANGE_CHANNEL)) > -1) payload.rangeChannel = RangeChannel.valueOf(json.nextString().uppercase())
            if (json.selectName(JsonReader.Options.of(GREEN)) > -1) payload.speed = json.nextInt()
            if (json.selectName(JsonReader.Options.of(BLUE)) > -1) payload.speed = json.nextInt()
            json.endObject()
            return payload
        }
    }
}
