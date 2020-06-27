package com.john.v.toot.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.john.v.toot.R
import com.john.v.toot.alarms.AlarmConstants
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase

class TimerReceiver : BroadcastReceiver() {

    val TAG = "TimerReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e(TAG, "Notifications activity started ")


        createNotificationChannel(context!!)

        var builder =
            NotificationCompat.Builder(context, "TEST_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("TEST TITLE")
                .setContentText("TEST STRING")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }




        TaskDatabase.databaseWriteExecutor.execute {
            val current = TaskDatabase.getDatabase(context)?.taskDao()?.getTask(
                intent?.extras?.get(AlarmConstants.EXTRA_NAME) as String
            )


            TaskDatabase.getDatabase(context)?.taskDao()?.updateTasks(
                Task(
                    current!!.name,
                    current.time,
                    current.lowBattery,
                    current.powerOff,
                    current.customMessage,
                    current.jsonContacts,
                    !current.isActive,
                    current.isTimer
                )
            )
        }

    }


    private fun createNotificationChannel(context: Context) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BuddySystem"
            val descriptionText = "BuddySystemNotifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TEST_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}