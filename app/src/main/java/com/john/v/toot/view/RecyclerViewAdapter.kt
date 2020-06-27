package com.john.v.toot.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase


/**
 * BUTTON ANIMATION: https://stackoverflow.com/questions/2614545/animate-change-of-view-background-color-on-android
 */
class RecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>() {


    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks: List<Task>? = null
    private var context: Context = context

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordItemView: TextView = view.findViewById(R.id.textView)
        val startButton: Button = view.findViewById(R.id.start_timer)

    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var current = tasks?.get(position)?.copy()
        holder.wordItemView.text = current?.name ?: return


        if (!current.isActive) {
            holder.startButton.text = "Start task"
        } else {
            holder.startButton.text = "Stop task"
        }


        holder.startButton.setOnClickListener {
            TaskDatabase.databaseWriteExecutor.execute {

                TaskDatabase.getDatabase(context)?.taskDao()?.updateTasks(
                    Task(
                        current.name,
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = mInflater.inflate(
            R.layout.recycler_view_task,
            parent, false
        )
        return TaskViewHolder(itemView)
    }

    fun setTask(task: List<Task>) {
        tasks = task

        notifyDataSetChanged()
    }

}