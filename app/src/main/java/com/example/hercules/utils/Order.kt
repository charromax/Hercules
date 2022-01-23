package com.example.hercules.utils

sealed class Order {
    object Ascending: Order()
    object Descending: Order()
}

sealed class SensorOrder(val order:Order) {
    class Date(order: Order): SensorOrder(order)
    class Topic(order: Order): SensorOrder(order)
    class State(order: Order): SensorOrder(order)
}

