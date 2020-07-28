package com.example.medicineandgroceryapp.fcm
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.medicineandgroceryapp.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.json.JSONObject
import java.util.*

class Notifications : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }
    fun sendNotification(rm: RemoteMessage) {
        val data = rm.data
        if(data.containsKey("flag")){
            throwVerficationNotification(data["flag"]!!.toInt(),data["cid"].toString(),data["stid"].toString())
        }
        else if(data.containsKey("fstat")){
            throwRqForDpNotification(data["dpid"].toString(),data["stid"].toString())
        }
        else {
            getCustomerName(data["cid"].toString(), data)
        }
    }
    fun getCustomerName(phoneNumber:String,data:Map<String,String> ){
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/Get_Customer_Info.php?phone="+phoneNumber
        val request_status : StringRequest = StringRequest(url_get_status, Response.Listener {
                response ->
            // response
            val jso:JSONObject= JSONObject(response)
            // notify now
            val intent = Intent(application, RequestDetail::class.java)
            intent.putExtra("phone",data["cid"].toString())
            intent.putExtra("store_id",data["store_id"].toString())
            val contentIntent = PendingIntent.getActivity(
                application,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val NOTIFICATION_CHANNEL_ID =
                "com.example.medicineandgroceryapp" //your app package name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.description = "My Channel"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val notificationBuilder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Product request from : " + jso.getString("cname"))
                .setContentIntent(contentIntent)
                .setContentText("Click here to see")
                .setContentInfo("Info")
            notificationManager.notify(Random().nextInt(), notificationBuilder.build())


        }, Response.ErrorListener {
                error ->
            Toast.makeText(applicationContext,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_status))
    }
    fun throwVerficationNotification(flag:Int,customerPhone:String,store_id:String){
        var status:String=""
        if(flag==0){
            status="declined"
        }
        else{
            status="accepted"
        }
        val intent = Intent(application, cart_items::class.java)
        intent.putExtra("phone",customerPhone)
        intent.putExtra("id",store_id)
        val contentIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID =
            "com.example.medicineandgroceryapp" //your app package name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "My Channel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Your product request has been "+status)
            .setContentIntent(contentIntent)
            .setContentText("Click here to see")
            .setContentInfo("Info")
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

    fun throwRqForDpNotification(OnwerPhone:String,store_id:String){
        val intent = Intent(application, DeliveryPersonAcceptReject::class.java)
        intent.putExtra("phone",OnwerPhone)
        intent.putExtra("id",store_id)
        val contentIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID =
            "com.example.medicineandgroceryapp" //your app package name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "My Channel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("You have received a hiring request")
            .setContentIntent(contentIntent)
            .setContentText("Click here to see")
            .setContentInfo("Info")
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
    fun throwNotificationForDpAcceptAndReject(flag:Int,store_owner_phone:String){
        var status:String=""
        if(flag==0){
            status="declined"
        }
        else{
            status="accepted"
        }
        val intent = Intent(application, cart_items::class.java)
        intent.putExtra("phone",store_owner_phone)
        val contentIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID =
            "com.example.medicineandgroceryapp" //your app package name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "My Channel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Your delivery person hiring request has been "+status)
            .setContentIntent(contentIntent)
            .setContentText("Click here to see")
            .setContentInfo("Info")
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
}