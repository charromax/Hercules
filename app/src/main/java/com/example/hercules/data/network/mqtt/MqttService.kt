package com.example.hercules.data.network.mqtt

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.GROUP_ALERT_CHILDREN
import androidx.core.app.NotificationManagerCompat
import com.example.hercules.R
import com.example.hercules.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MqttService : Service(), MqttClientActions {
    @Inject
    lateinit var client: HerculesMqttClient

    val TAG = "MQTT_SERVICE"
    val CHANNEL_ID = "HERCULES_CHANNEL"
    val NOTIFICATION_ID = 213497586
    val GROUP_HERCULES_SENSORS = "com.android.example.hercules.SENSOR_GROUP"
    private lateinit var notification: Notification
    val testTopics = listOf("home/office/door", "home/office/window")


    override fun onCreate() {
        super.onCreate()
        client.listener = this
        notification = buildNotification("Hercules Vigila", "Sistema Online", true)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun buildNotification(
        title: String,
        message: String,
        isGroupSummary: Boolean = false
    ): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(GROUP_HERCULES_SENSORS)
            .setGroupAlertBehavior(GROUP_ALERT_CHILDREN)
            .setGroupSummary(isGroupSummary)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onMessageReceived(message: String) {
        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_ID, buildNotification("ALerta recibida", message))
    }
}