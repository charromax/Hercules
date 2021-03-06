/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hercules.R
import com.example.hercules.domain.model.*
import com.example.hercules.presentation.ui.Screen
import com.example.hercules.presentation.ui.home.components.HomeHeader
import com.example.hercules.presentation.ui.home.components.WaterPumpListItem
import com.example.hercules.presentation.ui.home.components.totems.MagneticSensor
import com.example.hercules.presentation.ui.mqtt.MqttEvents
import com.example.hercules.presentation.ui.mqtt.MqttState
import com.example.hercules.presentation.ui.mqtt.MqttViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "SENSOR_LIST"

/**
 * holds list of saved sensors for the user to interact with
 * currently works as home screen
 */
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    navController: NavController,
    totemsViewModel: TotemsViewModel,
    mqttViewModel: MqttViewModel
) {
    val mqttState = mqttViewModel.mqttState.collectAsState()
    val homeState = totemsViewModel.homeState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    addTopicList(mqttState, homeState, mqttViewModel)
    checkSubscriptionErrors(
        context = LocalContext.current,
        scope = scope,
        scaffoldState = scaffoldState,
        map = mqttState.value.subscriptionMap,
        mqttViewModel = mqttViewModel
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddTotemScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_sensor)
                )
            }
        },
        scaffoldState = scaffoldState
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeHeader(
                isOrderSectionVisible = homeState.value.isOrderSectionVisible,
                order = homeState.value.sensorOrder,
                onToggleOrderButton = {
                    //order button clicked
                    totemsViewModel.onEvent(HomeEvents.OnToggleSectionOrder)
                },
                onOrderChange = {
                    totemsViewModel.onEvent(HomeEvents.OnOrderChange(it))
                },
                headerTitle = stringResource(id = R.string.sensors_in_your_network)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(homeState.value.totems) { item: Totem ->
                    val deleteMsg: String
                    val undoLabel = stringResource(R.string.undo)
                    when (item.type) {
                        TotemType.WATER_PUMP -> {
                            deleteMsg = stringResource(id = R.string.water_pump_deleted)
                            WaterPumpListItem(
                                modifier = Modifier.fillMaxWidth(),
                                regador = item as Regador,
                                onButtonClicked = {
                                    Log.i(TAG, "HomeScreen: ALARM BUTTON CLICKED")
                                    sendWaterPumpPowerPayload(mqttViewModel, item)
                                },
                                onDeleteButtonClicked = {
                                    deleteTotem(
                                        totemsViewModel,
                                        it,
                                        scope,
                                        scaffoldState,
                                        deleteMsg,
                                        undoLabel
                                    )
                                },
                                onRefreshButtonClicked = {
                                    //TODO: send refresh totem payload
                                })
                        }
                        TotemType.MAG_SENSOR -> {
                            deleteMsg = stringResource(id = R.string.sensor_deleted)
                            MagneticSensor(
                                modifier = Modifier.fillMaxWidth(),
                                sensor = item as Sensor,
                                lastMessageReceived = mqttState.value.lastMessageReceived,
                                onButtonClicked = {
                                    Log.i(TAG, "HomeScreen: ALARM BUTTON CLICKED")
                                    //TODO: send reset alarm payload
                                },
                                onDeleteButtonClicked = {
                                    deleteTotem(
                                        totemsViewModel,
                                        it,
                                        scope,
                                        scaffoldState,
                                        deleteMsg,
                                        undoLabel
                                    )
                                })
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

fun checkSubscriptionErrors(
    context: Context,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    map: Map<String, Boolean>?,
    mqttViewModel: MqttViewModel
) {
    if (!map.isNullOrEmpty()) {
        if (map.values.contains(false)) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.subscription_error)
                )
            }
        } else {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.system_online)
                )
            }
        }
        mqttViewModel.clearSubscriptionMap()
        mqttViewModel.clearError()
        mqttViewModel.clearSnack()
    }
}

private fun addTopicList(
    mqttState: State<MqttState>,
    totemState: State<HomeState>,
    mqttViewModel: MqttViewModel
) {
    if (mqttState.value.isMqttConnected && !mqttState.value.isMqttSubscribed && totemState.value.topicList.isNotEmpty()) {
        mqttViewModel.topicList.clear()
        mqttViewModel.topicList.addAll(totemState.value.topicList)
        totemState.value.topicList.forEach {
            mqttViewModel.onEvent(MqttEvents.SubscribeToTopic(it))
        }
    }
}

private fun deleteTotem(
    sensorViewModel: TotemsViewModel,
    totem: Totem,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    deleteMsg: String,
    undoLabel: String
) {
    sensorViewModel.onEvent(HomeEvents.OnDeleteTotem(totem))
    scope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = deleteMsg,
            actionLabel = undoLabel
        )

        if (result == SnackbarResult.ActionPerformed) {
            sensorViewModel.onEvent(HomeEvents.OnUndoDelete)
        }
    }
}

private fun sendWaterPumpPowerPayload(
    mqttViewModel: MqttViewModel,
    item: Regador
) {
    if (mqttViewModel.mqttState.value.lastMessageSent?.topic != null
        && mqttViewModel.mqttState.value.lastMessageSent?.topic == item.topic
    ) {
        if (mqttViewModel.mqttState.value.lastMessageSent?.message?.lowercase()
            == BasicInstructionSet.OFF.name.lowercase()
        ) {
            mqttViewModel.onEvent(
                MqttEvents.PublishMessage(
                    item.topic,
                    BasicInstructionSet.ON.name
                )
            )
        } else {
            mqttViewModel.onEvent(
                MqttEvents.PublishMessage(
                    item.topic,
                    BasicInstructionSet.OFF.name
                )
            )
        }
    } else {
        mqttViewModel.onEvent(
            MqttEvents.PublishMessage(
                item.topic,
                BasicInstructionSet.ON.name
            )
        )
    }
}