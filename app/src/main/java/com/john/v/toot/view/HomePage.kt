package com.john.v.toot.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase
import com.john.v.toot.data.TaskViewModel
import com.john.v.toot.view.task.TaskActivity


/**
 * https://support.google.com/googleplay/android-developer/answer/9047303
 */

class HomePage : AppCompatActivity() {

    lateinit var createTaskButton: Button
    lateinit var taskViewModel: TaskViewModel
    lateinit var recyclerViewadapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        createTaskButton = findViewById(R.id.create_task_button)

        createTaskButton.setOnClickListener {
            val intent = Intent(this@HomePage, TaskActivity::class.java)
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
                // Update the cached copy of the words in the adapter.
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
}
