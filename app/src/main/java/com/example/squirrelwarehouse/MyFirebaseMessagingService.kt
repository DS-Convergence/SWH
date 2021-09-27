package com.example.squirrelwarehouse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"
class MyFirebaseMessagingService: FirebaseMessagingService()
{

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    // 사용자 디바이스 토큰 새로 생성
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    //알람이 온 경우 --> 어떻게 보이게 할 것인지, 누르면 어디로 이동하게 할 것인지 정하는 메소드
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        //알림 팝업 누를 시 MainActivity로 이동
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}
/*
{
    private val TAG: String = this.javaClass.simpleName

    override fun onNewToken(token: String)
    {
        Log.d(TAG, "Refreshed token : $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null)
        {
            //val title = remoteMessage.notification?.title.toString()
            //val body = remoteMessage.notification?.body.toString()
            Log.e("alarm","remoteMessage.notification?.title.toString()" + remoteMessage.notification?.title.toString())
            Log.e("alarm","remoteMessage.notification?.body.toString()" +remoteMessage.notification?.body.toString())

            val title = remoteMessage.data["title"].toString()
            val body = remoteMessage.data["content"].toString()

            createNotificationChannel()
            sendNotification(title, body)
        }
    }





    // 받은 알림을 기기에 표시하는 메서드
    private fun sendNotification(title: String?, body: String)
    {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)


        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    private fun createNotificationChannel() {
        // create the NotificationChannel,but only o nAPI 26 + b/c the notification class is new and not support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel",name, importance).apply{
                description = descriptionText
            }
            //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //notificationManager.createNotificationChannel(channel)
        }
    }

}*/