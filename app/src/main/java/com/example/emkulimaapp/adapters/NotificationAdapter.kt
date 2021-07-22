package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.models.Notification

open class NotificationAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Notification> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.txt.text = lst[position].text
        myViewHolder.date.text = lst[position].date
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txt: TextView = view.findViewById(R.id.txtNotification)
        val date: TextView = view.findViewById(R.id.txtNotificationDate)
    }

    fun getNotifications(notF: ArrayList<Notification>){
        for (i in notF){
            lst.add(i)
            notifyDataSetChanged()
        }
    }
}