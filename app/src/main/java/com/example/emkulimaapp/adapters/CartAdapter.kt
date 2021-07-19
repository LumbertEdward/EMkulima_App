package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

open class CartAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var product: ArrayList<Cart> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.title.text = product[position].name
        myViewHolder.price.text = product[position].price.toString()
        val picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(product[position].img).into(myViewHolder.img)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val img: ImageView = view.findViewById(R.id.imgCartItem)
        val title: TextView = view.findViewById(R.id.cartItemTitle)
        var price: TextView = view.findViewById(R.id.cartItemPrice)
        var quantity: TextView = view.findViewById(R.id.cartItemQuantity)
    }

    fun getData(lst: ArrayList<Cart>){
        for (i in lst){
            product.add(i)
            notifyDataSetChanged()
        }
    }
}