package com.example.emkulimaapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.models.Product

open class ProductAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Product> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.name.text = lst[position].name
        myViewHolder.price.text = lst[position].price.toString()
        myViewHolder.prev.paintFlags = myViewHolder.prev.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        myViewHolder.card.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var card: CardView = view.findViewById(R.id.cardProd)
        var name: TextView = view.findViewById(R.id.prodName)
        var price: TextView = view.findViewById(R.id.prodPrice)
        var cart: RelativeLayout = view.findViewById(R.id.relCart)
        var fav: RelativeLayout = view.findViewById(R.id.relFavProd)
        var prev: TextView = view.findViewById(R.id.prodPrevPrice)
    }

    fun getData(prod: ArrayList<Product>){
        for (i in prod){
            lst.add(i)
            notifyDataSetChanged()
        }
    }
}