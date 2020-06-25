package com.john.v.toot.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.john.v.toot.R
import com.john.v.toot.data.Task

class RecyclerViewAdapter(context : Context) : RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>() {


    private  var mInflater:LayoutInflater = LayoutInflater.from(context)
    private  var tasks:List<Task>? = null

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordItemView: TextView

        init{
            wordItemView = view.findViewById(R.id.textView)
        }
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val current = tasks?.get(position)
        holder.wordItemView.text = current?.name ?: return

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        var itemView = mInflater.inflate(R.layout.recyclerview_item,
            parent , false)
        return TaskViewHolder(itemView)
    }

    fun setTask (task: List<Task>){
        tasks = task
        notifyDataSetChanged()
    }

}