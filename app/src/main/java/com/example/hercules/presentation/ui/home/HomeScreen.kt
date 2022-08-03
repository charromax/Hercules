/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hercules.R
import com.example.hercules.data.remote.requests.LedStripControlRequest
import com.example.hercules.domain.model.*
import com.example.hercules.presentation.ui.Screen
import com.example.hercules.presentation.ui.home.components.HomeHeader
import com.example.hercules.presentation.ui.home.components.totems.LedStripControlItem
import com.example.hercules.presentation.ui.home.components.totems.MagneticSensor
import com.example.hercules.presentation.ui.home.components.totems.WaterPumpListItem
import com.example.hercules.presentation.ui.mqtt.MqttEvents
import com.example.hercules.presentation.ui.mqtt.MqttState
import com.example.hercules.presentation.ui.mqtt.MqttViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "SENSOR_LIST"

/**
 * holds list of saved sensors for the user to interact with
 * currently works as home screen
 */
@ExperimentalFoundationApi
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column() {
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

                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(homeState.value.totems) { item: Totem ->
                        val deleteMsg: String
                        val undoLabel = stringResource(R.string.undo)

                        when (item.type) {
                            TotemType.WATER_PUMP -> {
                                deleteMsg = stringResource(id = R.string.water_pump_deleted)
                                WaterPumpListItem(
                                    regador = item as Regador,
                                    onButtonClicked = {
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
                                    }
                                )
                            }
                            TotemType.MAG_SENSOR -> {
                                deleteMsg = stringResource(id = R.string.sensor_deleted)
                                MagneticSensor(
                                    magSensor = item as MagSensor,
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
                            TotemType.RGB_STRIP_CONTROL -> {
                                LedStripControlItem(
                                    totem = item as RgbStripControl,
                                    onButtonClicked = { color: Color ->
                                        sendColorPickerPayload(color, item, mqttViewModel)
                                    },
                                    onDeleteButtonClicked = { /*TODO*/ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun sendColorPickerPayload(color: Color, totem: Totem, mqttViewModel: MqttViewModel) {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(LedStripControlRequest::class.java)
    val message = jsonAdapter.toJson(
        LedStripControlRequest(
            RgbMode.MANUAL,
            4,
            RangeChannel.R,
            color.red.toInt(),
            color.green.toInt(),
            color.blue.toInt()
        )
    )
    mqttViewModel.onEvent(MqttEvents.PublishMessage(totem.topic, message))
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

fun addTopicList(
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

fun deleteTotem(
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

fun sendWaterPumpPowerPayload(
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
