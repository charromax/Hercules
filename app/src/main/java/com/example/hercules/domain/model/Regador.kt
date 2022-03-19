package com.example.hercules.domain.model

import com.example.hercules.data.model.DBTotem

class Regador(
    override val id: Int,
    override val topic: String,
    override val powerState: Boolean,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean,
    override val type: TotemType
) : Totem() {

    override fun toDBObject(): DBTotem {
        return DBTotem(
            id = id,
            createdAt = createdAt,
            topic = topic,
            name = name,
            totemType = type
        )
    }

    companion object {
        fun build(totem: DBTotem) =
            Regador(
                id = totem.id,
                createdAt = totem.createdAt,
                topic = totem.topic,
                name = totem.name,
                type = totem.totemType,
                isActive = false,
                powerState = false
            )
    }
}