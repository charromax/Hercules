package com.example.hercules.presentation.ui.home

data class TotemSubscriptionState(
    val successfulTopics: MutableList<String>,
    val unSuccessfulTopics: MutableList<String>
)