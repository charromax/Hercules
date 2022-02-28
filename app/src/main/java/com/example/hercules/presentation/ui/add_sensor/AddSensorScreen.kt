/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hercules.R
import com.example.hercules.presentation.ui.add_sensor.components.TotemTypeDropDown
import com.example.hercules.presentation.ui.add_sensor.components.TotemTypeDropDownButton
import com.example.hercules.presentation.ui.home.TotemsViewModel

@Composable
fun AddSensorScreen(
    totemsViewModel: TotemsViewModel = viewModel()
) {
    val state = totemsViewModel.addTotemScreenState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(modifier = Modifier.fillMaxWidth(), value = "Nombre", onValueChange = {
                totemsViewModel.newTotemName = it.trim()
            })
            TextField(modifier = Modifier.fillMaxWidth(), value = "Topic", onValueChange = {
                totemsViewModel.newTotemTopic = it.trim().lowercase()
            })
            TotemTypeDropDownButton(
                onClick = { totemsViewModel.onEvent(AddTotemScreenEvents.OnDropDownOpen) },
                selectedTotemType = state.value.selectedTotemType,
                onSelected = { totemsViewModel.onEvent(AddTotemScreenEvents.OnTotemTypeSelected(it)) },
                isDDExpanded = state.value.isDropDownExpanded,
                onDismissRequested = { totemsViewModel.onEvent(AddTotemScreenEvents.OnDropDownDismissed) }
            )
        }
        Button(onClick = {
            totemsViewModel.onEvent(AddTotemScreenEvents.OnAddNewTotem)
        }) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
@Preview
fun AddSensorPreview() {
    AddSensorScreen()
}
