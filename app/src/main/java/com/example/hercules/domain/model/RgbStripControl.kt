package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem
import com.example.hercules.data.remote.requests.LedStripControlRequest
import com.example.hercules.data.remote.response.BaseTotemPayload
import com.example.hercules.data.remote.response.MqttManualParser
import com.example.hercules.data.remote.response.RgbStripControlPayload
import com.example.hercules.data.remote.response.WaterPumpPayload

enum class RgbMode { MANUAL, FADE, RANGE }
enum class RangeChannel { R, G, B }

class RgbStripControl(
    override val id: Int,
    override val topic: String,
    override val powerState: Boolean,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean,
    override val type: TotemType,
    override val currentState: BaseTotemPayload?
) : Totem() {
    override fun toDBObject(): DBTotem {
        return DBTotem(
            topic = this.topic,
            name = this.name,
            totemType = this.type,
        )
    }

    companion object {
        fun build(totem: DBTotem) =
            RgbStripControl(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                name = totem.name,
                type = totem.totemType,
                isActive = totem.isActive,
                powerState = totem.isPowerOn,
                currentState = MqttManualParser.buildPayload(
                    totem.rawJsonPayload,
                    totem.totemType
                ) as? RgbStripControlPayload
            )
    }
}