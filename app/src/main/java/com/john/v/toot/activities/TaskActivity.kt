package com.john.v.toot.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase
import com.john.v.toot.notifications.TimerReceiver
import kotlinx.android.synthetic.main.create_task_activity.*

class TaskActivity : AppCompatActivity() {

    lateinit var timerValueHours: NumberPicker
    lateinit var timerValueMinutes: NumberPicker
    lateinit var alarmTimePicker: TimePicker

    lateinit var clock_mode: ConstraintLayout
    lateinit var timer_mode: ConstraintLayout


    val TAG = "TaskActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.create_task_activity)
        // Switch between timer-based and clock-based
        val clockOrTimer = findViewById<Spinner>(R.id.clock_or_timer)
        val clockTimerList = arrayListOf<String>("Clock", "Timer")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            clockTimerList
        )

        clock_mode = findViewById(R.id.clock_mode)
        timer_mode = findViewById(R.id.timer_mode)


        clockOrTimer.adapter = adapter

        // http://www.google.com/maps/place/49.46800006494457,17.11514008755796

        clockOrTimer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    clock_mode.visibility = View.VISIBLE
                    timer_mode.visibility = View.INVISIBLE
                } else {
                    clock_mode.visibility = View.INVISIBLE
                    timer_mode.visibility = View.VISIBLE
                }
            }
        }


        timerValueHours = findViewById(R.id.timer_value_hours)
        timerValueHours.minValue = 0
        timerValueHours.maxValue = 23


        timerValueMinutes = findViewById(R.id.timer_value_minutes)
        timerValueMinutes.minValue = 0
        timerValueMinutes.maxValue = 59

        // Once timer is done , add location services

        /**
         * Alarm Manager Tests
         *
         */

        val intent = Intent(this, TimerReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 2000,
            pendingIntent
        )


        val database = TaskDatabase.getDatabase(baseContext)
        val taskDao = database!!.taskDao()

        TaskDatabase.databaseWriteExecutor.execute {
            val tasks= taskDao.getAllTasks()
            for (task in tasks ){
                Log.e(TAG , task.name)
            }
        }

        /*

        android:id="@+id/clock_or_timer"
        android:id="@+id/timer_value_hours"
        android:id="@+id/timer_value_minutes"
        android:id="@+id/time_picker"
        android:id="@+id/alert_power_off"
        android:id="@+id/alert_low_battery"
        android:id="@+id/text_content"
        android:id="@+id/submit_button"

        @PrimaryKey val name:String,
        @ColumnInfo val time:Double,
        @ColumnInfo val lowBattery:Boolean,
        @ColumnInfo val powerOff: Boolean,
        @ColumnInfo val customMessage: String


         */


        val time: Double

        if (clockOrTimer.selectedItem.toString() == "Clock") {
            time = -1.0
        } else {


            // get the value in  seconds
            time = timer_value_hours.value.toDouble() * 60 * 60 +
                    timer_value_minutes.value.toDouble() * 60
        }
        val submitButtom = findViewById<Button>(R.id.submit_button)
        submitButtom.setOnClickListener {
            val task = Task(
                findViewById<EditText>(R.id.task_name).text.toString(),
                time,
                findViewById<CheckBox>(R.id.alert_low_battery).isChecked,
                findViewById<CheckBox>(R.id.alert_power_off).isChecked,
                findViewById<EditText>(R.id.custom_message).text.toString()
                )

            // if there are errors in th
            TaskDatabase.databaseWriteExecutor.execute {
                val db = TaskDatabase.getDatabase(baseContext)
                db?.taskDao()?.insertAll(task)
            }
            finish()
        }
    }

}
