package com.john.v.toot.view.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.john.v.toot.R

class ContactsAdapter:
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    var contacts: ArrayList<Pair<String, String>> = ArrayList()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: Button = view.findViewById(R.id.remove_contact)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_contact, parent, false)
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.contactName.text = contacts.get(position).first

        holder.contactName.setOnClickListener {
            contacts.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = contacts.size


    fun addContact(name: String, number: String) {
        contacts.add(Pair(name, number))
        notifyDataSetChanged()
    }
}