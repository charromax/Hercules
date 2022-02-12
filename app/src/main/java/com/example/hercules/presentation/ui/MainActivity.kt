package com.example.hercules.presentation.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hercules.data.network.mqtt.MqttService
import com.example.hercules.presentation.theme.HerculesTheme
import com.example.hercules.presentation.theme.Shapes
import com.example.hercules.presentation.ui.home.HomeScreen
import com.example.hercules.presentation.ui.mqtt.MqttEvents
import com.example.hercules.presentation.ui.mqtt.MqttViewModel
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MAIN"

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mqttViewModel: MqttViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mqttViewModel.onEvent(MqttEvents.StartConnectionRequest(this, listOf("home/terrace/pump")))
        setContent {
            HerculesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(mqttViewModel = mqttViewModel)
                }
            }
        }
        checkServiceRunning()
    }

    private fun checkServiceRunning() {
        if (isServiceRunningInForeground(this, MqttService::class.java)) {
            Log.i(TAG, "checkServiceRunning: its alive!")
            stopMqttService(applicationContext)
        } else Log.i(TAG, "checkServiceRunning: its dead...")
    }

    private fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }
}

@ExperimentalFoundationApi
@Composable
fun HerculesHomeScreen(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(shape = Shapes.small, onClick = { startMqttService(context) }) {
            Text(text = "START")
        }

        Button(shape = Shapes.small, onClick = { stopMqttService(context) }) {
            Text(text = "STOP")
        }

    }

}

@ExperimentalFoundationApi
private fun startMqttService(context: Context) {
    val serviceIntent = Intent(context, MqttService::class.java)
    context.startService(serviceIntent)
}

@ExperimentalFoundationApi
private fun stopMqttService(context: Context) {
    val serviceIntent = Intent(context, MqttService::class.java)
    context.stopService(serviceIntent)
}

