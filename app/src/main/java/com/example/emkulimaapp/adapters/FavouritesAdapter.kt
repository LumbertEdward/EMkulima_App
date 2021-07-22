package com.example.emkulimaapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

open class FavouritesAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Product> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.name.text = lst[position].name
        myViewHolder.price.text = "Ksh " + lst[position].price.toString()
        val picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(constants.BASE_URL + lst[position].img).into(myViewHolder.img)
        myViewHolder.card.setOnClickListener {
            generalInterface.passDetails(lst[position])
        }

        myViewHolder.fav.setOnClickListener {
            generalInterface.removeFromFavourites(lst[position].productId!!)
            removeItem(position)
        }
        myViewHolder.imgFav.setImageResource(R.drawable.ic_baseline_favorite_24)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        generalInterface = context as GeneralInterface
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var card: CardView = view.findViewById(R.id.cardProd)
        var name: TextView = view.findViewById(R.id.prodName)
        var price: TextView = view.findViewById(R.id.prodPrice)
        var cart: RelativeLayout = view.findViewById(R.id.relCart)
        var fav: RelativeLayout = view.findViewById(R.id.relFavProd)
        var img: ImageView = view.findViewById(R.id.imgProd)
        var imgFav: ImageView = view.findViewById(R.id.imgFav)
    }

    fun getData(prod: ArrayList<Product>){
        for (i in prod){
            lst.add(i)
            notifyDataSetChanged()
        }
    }

    fun clear(){
        lst.clear()
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int){
        lst.removeAt(pos)
        notifyItemRemoved(pos)
    }
}