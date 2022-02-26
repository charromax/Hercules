/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.add_sensor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hercules.R
import com.example.hercules.presentation.ui.home.TotemsViewModel

@Composable
fun AddSensorScreen(
    totemsViewModel: TotemsViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(modifier = Modifier.fillMaxWidth(), value = "Nombre", onValueChange = {
                totemsViewModel.newTotemName = it
            })
            TextField(modifier = Modifier.fillMaxWidth(), value = "Topic", onValueChange = {
                totemsViewModel.newTotemTopic = it
            })


        }
        Button(onClick = {

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
