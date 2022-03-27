/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hercules.R
import com.example.hercules.presentation.ui.add_sensor.components.TotemTypeDropDownButton
import com.example.hercules.presentation.ui.components.TopBarStandard
import com.example.hercules.presentation.ui.home.HomeEvents
import com.example.hercules.presentation.ui.home.TotemsViewModel
import com.example.hercules.presentation.ui.mqtt.MqttEvents
import com.example.hercules.presentation.ui.mqtt.MqttViewModel
import kotlinx.coroutines.launch

@Composable
fun AddTotemScreen(
    navController: NavController,
    totemsViewModel: TotemsViewModel,
    mqttViewModel: MqttViewModel
) {
    val state = totemsViewModel.addTotemScreenState.collectAsState()
    if (state.value.isSaveSuccessful) {
        mqttViewModel.onEvent(MqttEvents.RefreshSubscription)
        totemsViewModel.onEvent(HomeEvents.OnTotemSavedSuccesfully)
        navController.navigateUp()
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    state.value.error?.let {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it
            )
        }
    }

    state.value.snack?.let {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    totemsViewModel.onEvent(AddTotemScreenEvents.OnAddNewTotem)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_save),
                    contentDescription = stringResource(R.string.new_sensor)
                )
            }
        },
        scaffoldState = scaffoldState
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            TopBarStandard(
                isBackButtonVisible = true,
                onBackButtonPressed = { navController.navigateUp() },
                headerTitle = stringResource(R.string.new_totem)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = state.value.newTotemName ?: "",
                    onValueChange = {
                        totemsViewModel.updateTotemName(it)
                    },
                    label = { Text(text = "Nombre") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.value.newTotemTopic ?: "",
                    onValueChange = {
                        totemsViewModel.updateTotemTopic(it.trim().lowercase())
                    },
                    label = { Text(text = "Topic") })
                TotemTypeDropDownButton(
                    onClick = { totemsViewModel.onEvent(AddTotemScreenEvents.OnDropDownOpen) },
                    selectedTotemType = state.value.selectedTotemType,
                    onSelected = {
                        totemsViewModel.onEvent(
                            AddTotemScreenEvents.OnTotemTypeSelected(
                                it
                            )
                        )
                    },
                    isDDExpanded = state.value.isDropDownExpanded,
                    onDismissRequested = { totemsViewModel.onEvent(AddTotemScreenEvents.OnDropDownDismissed) }
                )
            }
        }
    }

}

@Preview
@Composable
fun AddTotemPreview() {
    AddTotemScreen(
        navController = rememberNavController(),
        totemsViewModel = viewModel(),
        mqttViewModel = viewModel()
    )
}

