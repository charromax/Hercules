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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hercules.R
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
    navController: NavController,
    sensorViewModel: SensorsViewModel = viewModel(),
    mqttViewModel: MqttViewModel = viewModel()
) {
    val state = sensorViewModel.homeState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val deleteMsg = stringResource(R.string.sensor_deleted)
    val undoLabel = stringResource(R.string.undo)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //TODO: add navigation
                    Log.i(TAG, "HomeScreen: ADD SENSOR")
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
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SensorOrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    sensorOrder = state.sensorOrder,
                    onOrderChange = {
                        sensorViewModel.onEvent(HomeEvents.OnOrderChange(it))
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.sensors) { item: Sensor ->
                    SensorListItem(
                        modifier = Modifier.fillMaxWidth(),
                        sensor = item,
                        onButtonClicked = {
                            Log.i(TAG, "HomeScreen: ALARM BUTTON CLICKED")
                        },
                        onDeleteButtonClicked = {
                            sensorViewModel.onEvent(HomeEvents.OnDeleteSensor(it))
                            scope.launch {
                                val result= scaffoldState.snackbarHostState.showSnackbar(
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