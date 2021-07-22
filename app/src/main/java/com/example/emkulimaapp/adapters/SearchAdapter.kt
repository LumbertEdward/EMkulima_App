package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

open class SearchAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var lst: ArrayList<Product> = ArrayList()
    private var filList: ArrayList<Product> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.name.text = lst[position].name
        myViewHolder.price.text = lst[position].price.toString()
        val picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(constants.BASE_URL + lst[position].img).into(myViewHolder.img)
        myViewHolder.rel.setOnClickListener {
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
        var img: ImageView = view.findViewById(R.id.imgSearch)
        var name: TextView = view.findViewById(R.id.txtSearch)
        var price: TextView = view.findViewById(R.id.txtSearchPrice)
        var imgOthers: ImageView = view.findViewById(R.id.imgOthers)
        var rel: RelativeLayout = view.findViewById(R.id.relSearch)
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
}