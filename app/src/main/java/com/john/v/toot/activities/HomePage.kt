package com.john.v.toot.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase
import com.john.v.toot.data.TaskViewModel


class HomePage : AppCompatActivity() {

    lateinit var createTaskButton: Button
    lateinit var taskViewModel: TaskViewModel
    lateinit var recyclerViewadapter: RecyclerViewAdapter

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
        recyclerViewadapter = RecyclerViewAdapter(baseContext)
        recyclerViewMain.adapter = recyclerViewadapter
        recyclerViewMain.layoutManager = LinearLayoutManager(baseContext)


        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)


        taskViewModel.allTasks.observe(this,
            Observer<List<Task>> { tasks ->
                // Update the cached copy of the words in the adapter.
                (recyclerViewadapter.setTask(tasks))
            })

    }

}
