package com.example.hercules.domain.model

enum class PowerState {
    ON, OFF
}
abstract class Totem {
    abstract val id: Int
    abstract val topic: String
    abstract val powerState: PowerState
    abstract val name: String
    abstract val createdAt: Long
    abstract val isActive: Boolean

    fun turnOn() {

    }
    fun turnOff() {

    }

    fun register() {

    }
}
