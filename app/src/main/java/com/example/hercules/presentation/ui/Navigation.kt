/*
 * Copyright (c) 2022. charr0max -> manuelrg88@gmail.com
 */

package com.example.hercules.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hercules.presentation.ui.add_sensor.AddTotemScreen
import com.example.hercules.presentation.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object AddTotemScreen : Screen("add_totem_screen")
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun Navigation() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            HomeScreen(
                navController = navController,
                mqttViewModel = viewModel(viewModelStoreOwner = viewModelStoreOwner),
                totemsViewModel = viewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(route = Screen.AddTotemScreen.route) {
            AddTotemScreen(
                navController = navController,
                totemsViewModel = viewModel(viewModelStoreOwner = viewModelStoreOwner),
                mqttViewModel = viewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
    }
}