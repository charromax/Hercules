package com.example.hercules.presentation.ui

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
import com.example.hercules.presentation.theme.HerculesTheme
import com.example.hercules.presentation.theme.Shapes
import android.app.ActivityManager
import android.util.Log
import com.example.hercules.presentation.ui.home.HomeScreen

const val TAG= "MAIN"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HerculesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

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

