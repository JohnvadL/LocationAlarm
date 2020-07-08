package com.john.v.toot.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.john.v.toot.R
import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDatabase
import com.john.v.toot.notifications.NotificationsService
import com.john.v.toot.notifications.NotificationsService.LocalBinder
import com.jtv7.rippleswitchlib.RippleSwitch


/**
 * BUTTON ANIMATION: https://stackoverflow.com/questions/2614545/animate-change-of-view-background-color-on-android
 */
class TaskAdapter(context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var text = "Hi, this is an alert sent from the Buddy System App.  I set this alert " +
            "to be sent at this time. Please click the link to access my current location ."



    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks: List<Task>? = null
    private var context: Context = context
    lateinit var mService: NotificationsService


    lateinit var mConnection: ServiceConnection


    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordItemView: TextView = view.findViewById(R.id.textView)
        val startButton: Switch = view.findViewById(R.id.start_timer_switch)
        val relativeLayout: RelativeLayout = view.findViewById(R.id.relative_layout)
        val notePreview:TextView = view.findViewById(R.id.note_preview)
        val activationTime:TextView = view.findViewById(R.id.activation_time)
        val timeRemaining:TextView = view.findViewById(R.id.time_remaining_textView)
        val contacts:TextView = view.findViewById(R.id.contacts_textView)
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var current = tasks?.get(position)?.copy()
        var name = current?.name
        name = name?.trim()
        holder.wordItemView.text = name ?: return

        holder.notePreview.text = text

        if (!current!!.isActive) {
            holder.wordItemView.setTextColor(context.resources.getColor(R.color.light_grey))
            holder.relativeLayout.setBackgroundResource(R.drawable.rounded_corners)
            holder.startButton.setChecked(false)
            holder.activationTime.visibility = View.GONE
        } else {
            holder.wordItemView.setTextColor(context.resources.getColor(R.color.red))
            holder.relativeLayout.setBackgroundResource(R.drawable.rounded_corners_active)
            holder.startButton.setChecked(true)
            holder.activationTime.visibility = View.VISIBLE
        }


        holder.startButton.setOnClickListener {
            if (!current.isActive) {
                mConnection = object : ServiceConnection {
                    override fun onServiceDisconnected(name: ComponentName?) {
                        // Do nothing
                    }

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        val binder = service as LocalBinder
                        Log.e("TaskAdapter", "Service connected")
                        mService = binder.serviceInstance
                        mService.createNotification(current)
                        context.unbindService(mConnection)
                    }
                }
            } else {
                mConnection = object : ServiceConnection {
                    override fun onServiceDisconnected(name: ComponentName?) {
                        // Do nothing
                    }

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        val binder = service as LocalBinder
                        Log.e("TaskAdapter", "Service connected")
                        mService = binder.serviceInstance
                        mService.cancelNotification(current)
                        context.unbindService(mConnection)
                    }
                }
            }

            val serviceIntent = Intent(context, NotificationsService::class.java)
            Log.e("TaskAdapter", "Trying to bind service")
            context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE)
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