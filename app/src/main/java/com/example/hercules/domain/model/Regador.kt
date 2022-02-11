package com.example.hercules.domain.model

enum class RegadorInstructionSet {
    ON,
    OFF,
    REPORT
}
class Regador(
    override val id: Int,
    override val topic: String,
    override val powerState: PowerState,
    override val name: String,
    override val createdAt: Long,
    override val isActive: Boolean
): Totem() {
}