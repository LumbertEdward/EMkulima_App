package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

open class TakeAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Product> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.take_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.name.text = lst[position].name
        myViewHolder.cal.text = lst[position].calcs.toString()
        myViewHolder.time.text = lst[position].time + " mins"
        myViewHolder.cal.text = lst[position].calcs.toString() + " kcal"
        myViewHolder.time.text = lst[position].time
        val picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(constants.BASE_URL + lst[position].img).into(myViewHolder.img)
        myViewHolder.card.setOnClickListener {
            generalInterface.passDetails(lst[position])
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        generalInterface = context as GeneralInterface
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var card: CardView = view.findViewById(R.id.cardTake)
        var name: TextView = view.findViewById(R.id.nameTake)
        var time: TextView = view.findViewById(R.id.timeTake)
        var cal: TextView = view.findViewById(R.id.calTake)
        var img: ImageView = view.findViewById(R.id.imgTake)
    }

    fun getProducts(prods: ArrayList<Product>){
        for (i in prods){
            lst.add(i)
            notifyDataSetChanged()
        }
    }
}