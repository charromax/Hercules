package com.example.hercules.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.hercules.ui.theme.HerculesTheme
import com.example.hercules.ui.theme.Shapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HerculesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HerculesHomeScreen(this)
                }
            }
        }
    }
}

@Composable
fun HerculesHomeScreen(context: Context) {
    Column(
        modifier= Modifier.fillMaxSize(),
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

private fun startMqttService(context: Context) {
    val serviceIntent = Intent(context, MqttService::class.java)
    context.startService(serviceIntent)
}

private fun stopMqttService(context: Context) {
    val serviceIntent = Intent(context, MqttService::class.java)
    context.stopService(serviceIntent)
}

