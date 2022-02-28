package com.example.hercules.presentation.ui.add_sensor.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hercules.R
import com.example.hercules.domain.model.TotemType
import com.example.hercules.presentation.ui.add_sensor.AddTotemScreenEvents


@Composable
fun TotemTypeDropDownButton(
    onClick: () -> Unit,
    selectedTotemType: TotemType,
    onSelected: (TotemType) -> Unit,
    modifier: Modifier = Modifier,
    isDDExpanded: Boolean = false,
    onDismissRequested: () -> Unit
    ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_drop_down),
                contentDescription = null,
                modifier = Modifier.size(20.dp, 20.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Text(text = selectedTotemType.name)

            TotemTypeDropDown(
                isDDExpanded = isDDExpanded,
                onSelected = onSelected,
                onDismissRequested = onDismissRequested
            )
        }

    }
}