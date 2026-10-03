package com.example.hometask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale

class ChoreNotificationScheduler(
    private val context: Context
) {

    fun scheduleNotification(
        choreId: Int,
        choreName: String,
        dueDate: String
    ) {

        createNotificationChannel()

        try {

            val formatter = SimpleDateFormat(
                "d/M/yyyy HH:mm",
                Locale.getDefault()
            )

            val date = formatter.parse(dueDate)

            if (date == null) {
                return
            }

            val intent = Intent(
                context,
                ChoreNotificationReceiver::class.java
            )

            intent.putExtra(
                "CHORE_NAME",
                choreName
            )

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                choreId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            if (date.time > System.currentTimeMillis()) {

                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    date.time,
                    pendingIntent
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    fun cancelNotification(choreId: Int) {

        val intent = Intent(
            context,
            ChoreNotificationReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            choreId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

    private fun createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val channel = android.app.NotificationChannel(
                "CHORE_REMINDERS",
                "Chore Reminders",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "Notifications for upcoming chores"

            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as android.app.NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}