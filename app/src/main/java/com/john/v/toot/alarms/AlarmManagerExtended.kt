package com.john.v.toot.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.john.v.toot.data.Task
import com.john.v.toot.notifications.TimerReceiver
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AlarmManagerExtended {


    companion object {
        fun startTimer(task: Task, context: Context) {

            Log.e("AlarmManagerExtended", "StartTimer:" +task.name)
            // Start the intent for the time given
            val pendingIntent= createIntent(task, context)
            val alarmManager =
                context.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager


            var timeInMs:Long? = null

            if (task.isTimer) {
                Log.e("AlarmManagerExtended","Full time:" + task.time.toLong())


                val time = Calendar.getInstance()
                time.setTimeInMillis(System.currentTimeMillis())
                time.add(Calendar.SECOND, task.time.toInt())


                timeInMs = time.timeInMillis

            } else {

                val arr =
                    (task.time.toString()).split("\\.".toRegex()).dropLastWhile({ it.isEmpty() })
                        .toTypedArray()
                val intArr = IntArray(2)
                intArr[0] = Integer.parseInt(arr[0]) // 1
                intArr[1] = Integer.parseInt(arr[1]) // 9

                val myTime = (intArr[0]).toString() + ":" + (intArr[1]).toString()
                Log.e("AlarmManagerExtended" , myTime)


                val customCalendar = GregorianCalendar()
                // set hours and minutes
                customCalendar.set(Calendar.HOUR_OF_DAY, intArr[0])
                customCalendar.set(Calendar.MINUTE, intArr[1])
                customCalendar.set(Calendar.SECOND, 0)
                customCalendar.set(Calendar.MILLISECOND, 0)

                val currentCalendar = Calendar.getInstance()

                Log.e("AlarmMangerExtended", "CustomCalendar" +
                        customCalendar.get(Calendar.HOUR_OF_DAY) + ": " +  customCalendar.get(Calendar.MINUTE) )
                Log.e("AlarmMangerExtended", "CurrentCalendar" +
                        currentCalendar.get(Calendar.HOUR_OF_DAY) + ": " +  currentCalendar.get(Calendar.MINUTE) )


                if(currentCalendar.get(Calendar.HOUR_OF_DAY ) > customCalendar.get(Calendar.HOUR_OF_DAY))
                {
                    // if the current hour is greater than the hour set
                    Log.e("AlarmManagerExtended", "Moving to Next Day")
                    customCalendar.add(Calendar.DATE , 1)
                }  else if (currentCalendar.get(Calendar.HOUR_OF_DAY ) == customCalendar.get(Calendar.HOUR_OF_DAY)){

                    if(currentCalendar.get(Calendar.MINUTE ) >=customCalendar.get(Calendar.MINUTE))
                    {
                        // if the minutes are higher or the minutes have passed,
                        // we should use the next day
                        Log.e("AlarmManagerExtended", "Moving to Next Day")
                        customCalendar.add(Calendar.DATE , 1)
                    }
                }


                val customDate = customCalendar.time

                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                var date:Date?= null

                try {

                    date = sdf.parse(myTime)

                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                if (date != null) {
                    timeInMs = customDate.time
                }
            }


            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                timeInMs!!,
                pendingIntent
            )
        }


        fun cancelTimer(task: Task, context: Context) {
            val alarmManager =
                context.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val pendingIntent = createIntent(task, context)
            alarmManager?.cancel(pendingIntent )

        }



        fun createIntent (task:Task, context: Context) : PendingIntent {

            val intent = Intent(context, TimerReceiver::class.java)
            intent.putExtra(AlarmConstants.EXTRA_NAME, task.name)
            intent.putExtra(AlarmConstants.EXTRA_CONTACTS, task.jsonContacts)
            intent.putExtra(AlarmConstants.EXTRA_MESSAGE, task.customMessage)

            return PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }
}