package com.john.v.toot.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.john.v.toot.BuddySystemApplication
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase
import com.john.v.toot.data.TaskViewModel
import com.john.v.toot.view.task.CreateTaskActivity
import com.scwang.wave.MultiWaveHeader


/**
 * https://support.google.com/googleplay/android-developer/answer/9047303
 */

class HomePage : AppCompatActivity() {

    lateinit var createTaskButton: Button
    lateinit var taskViewModel: TaskViewModel
    lateinit var recyclerViewadapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.SplashTheme)



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)


        var waveHeader: MultiWaveHeader = findViewById(R.id.wave_header)

/*
        waveHeader.velocity = 0.009.toFloat()
        waveHeader.progress = 1.toFloat()
        waveHeader.gradientAngle = 45
        waveHeader.startColor = Color.parseColor("#1799b5")
        waveHeader.closeColor = Color.parseColor("#1799b5")

*/

        createTaskButton = findViewById(R.id.create_task_button)

        createTaskButton.setOnClickListener {
            val intent = Intent(this@HomePage, CreateTaskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up_info, R.anim.no_change)
        }


        val recyclerViewMain = findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerViewadapter = TaskAdapter(baseContext)
        recyclerViewMain.adapter = recyclerViewadapter
        recyclerViewMain.layoutManager = LinearLayoutManager(baseContext)


        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)


        taskViewModel.allTasks.observe(this,
            Observer<List<Task>> { tasks ->
                var flag = true
                // Update the cached copy of the words in the adapter.
                for (task in tasks) {

                    if(task.isActive){
                        waveHeader.velocity = 5.toFloat()

                        waveHeader.startColor = Color.parseColor("#FC3158")
                        waveHeader.closeColor = Color.parseColor("#FC3158")
                        waveHeader.isRunning()
                        flag = false
                        findViewById<TextView>(R.id.active_or_inactive).text = "ACTIVE"
                    }
                }

                if (flag){

                    waveHeader.velocity = 0.009.toFloat()
                    waveHeader.startColor = Color.parseColor("#1799b5")
                    waveHeader.closeColor = Color.parseColor("#1799b5")
                    findViewById<TextView>(R.id.active_or_inactive).text = "INACTIVE"


                }
                (recyclerViewadapter.setTask(tasks))
            })


        val intent = Intent("android.provider.Telephony.SMS_RECEIVED")
        val infos = packageManager.queryBroadcastReceivers(intent, 0)
        for (info in infos) {
            println("Receiver name:" + info.activityInfo.name + "; priority=" + info.priority)
        }


        /**
         * TODO: Implement permission check
         */

        val PERMISSION_REQUEST_CODE = 1

        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            Log.d("permission", "DENIED")
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )

            requestPermissions(permissions, PERMISSION_REQUEST_CODE)

            BuddySystemApplication.updateLocation(baseContext)


        } else {
            Log.e("permission", "GRANTED")
        }

        TaskDatabase.databaseWriteExecutor.execute {
            val tasks = TaskDatabase.getDatabase(baseContext)!!.taskDao().getAllTasks()

            for (task in tasks) {
                val itemType = object : TypeToken<List<String>>() {}.type

                val contacts = Gson().fromJson<ArrayList<String>>(task.jsonContacts, itemType)

                if (contacts != null) {
                    for (contact in contacts) {
                        Log.e("HomePage ", "NAME:" + contact.split(":")[0])
                        Log.e("HomePage", "Phone Number:" + contact.split(":")[1])
                    }
                }
            }
        }

    }


    class TestListener : LocationListener {
        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.e("NotificationsReceiver", "status changed")
        }

        override fun onLocationChanged(p0: Location?) {
            Log.e(
                "TimerReceiever",
                "location changed" + p0?.latitude.toString() + ":" + p0?.longitude.toString()
            )

        }

    }

}
