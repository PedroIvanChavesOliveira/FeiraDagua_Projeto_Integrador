package com.feiradagua.feiradagua.model.`class`.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.utils.Constants.Notification.CHANNEL_ID
import com.feiradagua.feiradagua.view.activitys.both.SplashActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService: FirebaseMessagingService() {

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
        get(){
            return sharedPref?.getString("token", "")
        }
        set(value) {
            sharedPref?.edit()?.putString("token", value)?.apply()
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, SplashActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"]).setContentText(message.data["message"])
            .setSmallIcon(R.drawable.logo_feira_dagua_remove).setAutoCancel(true)
            .setContentIntent(pendingIntent).build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "ChannelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = R.color.backgroundColor
        }

        notificationManager.createNotificationChannel(channel)
    }
}