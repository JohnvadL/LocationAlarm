package com.john.v.toot.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.john.v.toot.R
import com.john.v.toot.alarms.AlarmConstants
import com.john.v.toot.alarms.AlarmManagerExtended
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase


class NotificationsReceiver : BroadcastReceiver() {

    val TAG = "NotificationsReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e(TAG, "Notifications activity started ")


        createNotificationChannel(context!!)

        TaskDatabase.databaseWriteExecutor.execute {
            val current = TaskDatabase.getDatabase(context)?.taskDao()?.getTask(
                intent?.extras?.get(AlarmConstants.EXTRA_NAME) as String
            )


            var builder =
                NotificationCompat.Builder(context, "TEST_CHANNEL_ID")
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("TEST TITLE" )
                    .setContentText("Execution number " + NotificationsService.numExecutions[current!!.name] )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(AlarmManagerExtended.getRequestCode(intent?.extras?.get(AlarmConstants.EXTRA_NAME) as String ),
                    builder.build())
            }


            val serviceIntent = Intent(context, NotificationsService::class.java)
            Log.e("NotificationsReceiver", "Trying to bind service")
            val binder:IBinder = peekService(context ,serviceIntent )


            if(binder!= null ){
                val mService:NotificationsService = (binder as NotificationsService.LocalBinder).serviceInstance
                // Notification has been cancelled
                if(! mService.onReceiveNotification(current)){
                    TaskDatabase.getDatabase(context)?.taskDao()?.updateTasks(
                        Task(
                            current!!.name,
                            current.time,
                            current.lowBattery,
                            current.powerOff,
                            current.customMessage,
                            current.jsonContacts,
                            !current.isActive,
                            current.isTimer,
                            null
                        )
                    )
                }
            }
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