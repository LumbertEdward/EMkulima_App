package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.models.Product

open class TakeAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Product> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.take_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.name.text = lst[position].name
        myViewHolder.cal.text = lst[position].calcs
        myViewHolder.time.text = lst[position].time
        myViewHolder.card.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var card: CardView = view.findViewById(R.id.cardTake)
        var name: TextView = view.findViewById(R.id.nameTake)
        var time: TextView = view.findViewById(R.id.timeTake)
        var cal: TextView = view.findViewById(R.id.calTake)
    }

    fun getProducts(prods: ArrayList<Product>){
        for (i in prods){
            lst.add(i)
            notifyDataSetChanged()
        }
    }
}