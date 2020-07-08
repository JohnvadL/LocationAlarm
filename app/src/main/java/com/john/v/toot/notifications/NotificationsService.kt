package com.john.v.toot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.john.v.toot.R
import com.john.v.toot.alarms.AlarmManagerExtended
import com.john.v.toot.data.Task


/**
 * Service to handle all alarm related items
 *
 *
 */
class NotificationsService : Service() {

    val TAG = "NotificationsService"


    val NUM_REPEATING = 5

    private val mBinder = LocalBinder()

    companion object {
        lateinit var numExecutions: HashMap<String, Int>
    }


    /**
     * https://stackoverflow.com/questions/20594936/communication-between-activity-and-service
     * to Communicate with the activity
     */
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= 26) {

            val CHANNEL_ID = "my_channel_01";
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_LOW
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )

            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle("Toot timer is active")
                .setContentText("You have safeguards active").build()
            startForeground(2, notification)

        }

        numExecutions = HashMap()
    }

    override fun onStart(intent: Intent, startid: Int) {
        Log.e(TAG, "onStart")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        //TODO: cancel the notification
        Log.e(TAG, "onDestroy")
    }


    inner class LocalBinder : Binder() {
        val serviceInstance: NotificationsService
            get() = this@NotificationsService
    }

    fun createNotification(task: Task) {
        numExecutions[task.name] = NUM_REPEATING
        AlarmManagerExtended.startTimer(task, baseContext)
    }

    fun onReceiveNotification (task:Task):Boolean {
        if (numExecutions[task.name] == null){
            return false
        } else  if (numExecutions[task.name] == 0){
            cancelNotification(task)
            return false
        } else {
            var value = numExecutions[task.name]
            numExecutions[task.name] = value!! - 1
            return true
        }
    }

    fun cancelNotification(task: Task) {
        AlarmManagerExtended.cancelTimer(task, baseContext)
    }
}