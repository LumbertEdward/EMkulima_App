package com.example.emkulimaapp.adapters

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.graphics.Picture
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.FavouritesRetrofit
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.FavouritesInterface
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

open class
ProductAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var lst: ArrayList<Product> = ArrayList()
    private var filList: ArrayList<Product> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
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
            generalInterface.addToFavourites(lst[position].productId!!, myViewHolder.imgFav)
        }

        myViewHolder.cart.setOnClickListener {
            generalInterface.addToCart(lst[position])
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
        filList.addAll(prod)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var fil: ArrayList<Product> = ArrayList()
                if (constraint.isNullOrEmpty()){
                    fil.addAll(filList)
                }
                else{
                    var name: String = constraint.toString().lowercase(Locale.getDefault())
                    for (i in filList){
                        if (i.name.toString().lowercase().contains(name)){
                            fil.add(i)
                        }
                    }
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = fil
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                lst.clear()
                lst.addAll(results!!.values as ArrayList<Product>)
                notifyDataSetChanged()
            }

        }
    }

    fun clear(){
        lst.clear()
    }
}