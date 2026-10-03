package com.example.hometask

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class ChoreNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val choreName =
            intent.getStringExtra("CHORE_NAME") ?: "Chore"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val notification = NotificationCompat.Builder(
            context,
            "CHORE_REMINDERS"
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Chore Reminder")
            .setContentText("Time to do: $choreName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            choreName.hashCode(),
            notification
        )
    }
}