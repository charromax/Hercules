/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Green900,
    primaryVariant = Green700,
    secondary = Accent200,
    secondaryVariant = Accent500,
    background = DarkGray,
    onBackground = LightText,
    surface = MediumGray,
    onSurface = LightText,
    error = ErrorColor,
    onPrimary = LightestGray,
    onSecondary = DarkText
)

private val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green900,
    secondaryVariant = Green500,
    background = OffWhite,
    onBackground = DarkText,
    surface = LightestGray,
    onSurface = DarkText,
    error = ErrorColor,
    onSecondary = LightText,
    onPrimary = LightText

)

@Composable
fun HerculesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}