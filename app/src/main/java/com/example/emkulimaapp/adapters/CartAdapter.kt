package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

open class CartAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var cart: ArrayList<Cart> = ArrayList()
    private var product: ArrayList<Product> = ArrayList()
    lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.title.text = product[position].name
        myViewHolder.price.text = "Ksh " + ((product[position].price!!.toInt()) * (cart[position].total_items!!)).toString()
        myViewHolder.quantity.text = cart[position].total_items.toString() + " Items"
        val picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(constants.BASE_URL + product[position].img).into(myViewHolder.img)

    }

    override fun getItemCount(): Int {
        return cart.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        generalInterface = context as GeneralInterface
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val img: ImageView = view.findViewById(R.id.imgCartItem)
        val title: TextView = view.findViewById(R.id.cartItemTitle)
        var price: TextView = view.findViewById(R.id.cartItemPrice)
        var quantity: TextView = view.findViewById(R.id.txtTotalItems)
    }

    fun getData(dat: ArrayList<Product>, lst: ArrayList<Cart>){
        for (j in dat){
            product.add(j)
            notifyDataSetChanged()
        }

        for (i in lst){
            cart.add(i)
            notifyDataSetChanged()
        }
    }

    fun getItem(pos: Int): Cart{
        return cart.get(pos)
    }

    fun deleteItem(pos: Int){
        cart.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun redoItem(pos: Int, item: Cart){
        cart.add(pos, item)
        notifyItemInserted(pos)
    }

    fun clear(){
        product.clear()
        cart.clear()
        notifyDataSetChanged()
    }
}