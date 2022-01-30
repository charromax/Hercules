package com.example.hercules.presentation.utils

sealed class Order {
    object Ascending : Order()
    object Descending : Order()
}

sealed class SensorOrder(val order: Order) {
    class Date(order: Order) : SensorOrder(order)
    class Topic(order: Order) : SensorOrder(order)
    class State(order: Order) : SensorOrder(order)

    fun copy(order: Order): SensorOrder {
        return when (this) {
            is Date -> Date(order = order)
            is State -> State(order = order)
            is Topic -> Topic(order = order)
        }
    }
}

