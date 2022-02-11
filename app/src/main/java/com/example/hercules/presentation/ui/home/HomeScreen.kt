package com.example.hercules.presentation.ui.home

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hercules.R
import com.example.hercules.data.model.DBSensor
import com.example.hercules.domain.model.RegadorInstructionSet
import com.example.hercules.domain.model.Sensor
import com.example.hercules.presentation.ui.home.components.SensorListItem
import com.example.hercules.presentation.ui.home.components.SensorOrderSection
import kotlinx.coroutines.launch

private const val TAG = "SENSOR_LIST"

/**
 * holds list of saved sensors for the user to interact with
 * currently works as home screen
 */
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    sensorViewModel: SensorsViewModel = viewModel(),
    mqttViewModel: MqttViewModel
) {
    val state = sensorViewModel.homeState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val deleteMsg = stringResource(R.string.sensor_deleted)
    val undoLabel = stringResource(R.string.undo)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sensorViewModel.onEvent(
                        HomeEvents.OnAddSensor(
                            DBSensor(
                                topic = "home/terrace/pump",
                                name = "Regador"
                            )
                        )
                    )
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.sensors_in_your_network),
                    style = MaterialTheme.typography.h5
                )
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort),
                        contentDescription = stringResource(
                            R.string.sort_sensors
                        )
                    )
                }
            }

            AnimatedVisibility(
                visible = state.value.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SensorOrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    sensorOrder = state.value.sensorOrder,
                    onOrderChange = {
                        sensorViewModel.onEvent(HomeEvents.OnOrderChange(it))
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.value.totems) { item: Sensor ->
                    SensorListItem(
                        modifier = Modifier.fillMaxWidth(),
                        sensor = item,
                        onButtonClicked = {
                            Log.i(TAG, "HomeScreen: ALARM BUTTON CLICKED")
                            sendWaterPumpPowerPayload(mqttViewModel, item)
                        },
                        onDeleteButtonClicked = {
                            sensorViewModel.onEvent(HomeEvents.OnDeleteSensor(it))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = deleteMsg,
                                    actionLabel = undoLabel
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    sensorViewModel.onEvent(HomeEvents.OnUndoDelete)
                                }
                            }
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun sendWaterPumpPowerPayload(
    mqttViewModel: MqttViewModel,
    item: Sensor
) {
    if (mqttViewModel.mqttState.value.lastMessageSent?.topic != null
        && mqttViewModel.mqttState.value.lastMessageSent?.topic == item.topic) {
        if (mqttViewModel.mqttState.value.lastMessageSent?.message?.lowercase()
            == RegadorInstructionSet.OFF.name.lowercase()
        ) {
            mqttViewModel.onEvent(
                MqttEvents.PublishMessage(
                    item.topic,
                    RegadorInstructionSet.ON.name
                )
            )
        } else {
            mqttViewModel.onEvent(
                MqttEvents.PublishMessage(
                    item.topic,
                    RegadorInstructionSet.OFF.name
                )
            )
        }
    } else {
        mqttViewModel.onEvent(
            MqttEvents.PublishMessage(
                item.topic,
                RegadorInstructionSet.ON.name
            )
        )
    }
}