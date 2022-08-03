/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui.home.components.totems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hercules.domain.model.RgbStripControl
import com.example.hercules.domain.model.Totem
import com.example.hercules.presentation.ui.components.BaseTotemCard
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun LedStripControlItem(
    totem: RgbStripControl,
    onButtonClicked: (color: Color) -> Unit,
    onDeleteButtonClicked: (totem: Totem) -> Unit
) {
    val controller = rememberColorPickerController()
    var selectedColor = remember { Color.Black }
    BaseTotemCard {
        Column {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    selectedColor = colorEnvelope.color
                }
            )
            Button(onClick = { onButtonClicked(selectedColor) }) {
                Text(text = "Enviar color")
            }
        }

    }

}