package com.john.v.toot.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.john.v.toot.data.Task
import com.john.v.toot.notifications.TimerReceiver

class AlarmManager {

    fun startTimer(task: Task, context: Context) {

        // Start the intent for the time given

        val intent = Intent(context, TimerReceiver::class.java)
        intent.putExtra(AlarmConstants.EXTRA_NAME, task.name)
        intent.putExtra(AlarmConstants.EXTRA_CONTACTS, task.jsonContacts)
        intent.putExtra(AlarmConstants.EXTRA_MESSAGE, task.customMessage)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager


        if(task.isTimer) {

            val arr = (task.time.toString()).split("\\.".toRegex()).dropLastWhile({ it.isEmpty() })
                .toTypedArray()
            val intArr = IntArray(2)
            intArr[0] = Integer.parseInt(arr[0]) // 1
            intArr[1] = Integer.parseInt(arr[1]) // 9

            alarmManager?.set(
                AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + (intArr[0]*60*60 + intArr[1]*60)*1000,
                pendingIntent
            )

        }

    }



    fun cancelTimer(task: Task, context:Context) {


    }


}