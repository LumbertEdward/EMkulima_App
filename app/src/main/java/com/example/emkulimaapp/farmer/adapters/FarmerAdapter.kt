package com.example.emkulimaapp.farmer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.farmer.models.farmer
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Product
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

open class FarmerAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var lst: ArrayList<farmer> = ArrayList()
    private var lstFiltered: ArrayList<farmer> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.farmer_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.txtFarmerFirst.text = lst[position].firstName
        myViewHolder.txtLastFarmer.text = lst[position].lastName
        myViewHolder.txtLocationFarmer.text = lst[position].location
        var picasso: Picasso.Builder = Picasso.Builder(context)
        picasso.downloader(OkHttp3Downloader(context))
        picasso.build().load(constants.BASE_URL + lst[position].profilePicture).into(myViewHolder.imgImageFarmer)
        myViewHolder.cardFarmer.setOnClickListener {
            generalInterface.sendFarmer(lst[position])
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
        var cardFarmer: CardView = view.findViewById(R.id.cardFarmer)
        var imgImageFarmer: ImageView = view.findViewById(R.id.imgImageFarmer)
        var txtFarmerFirst: TextView = view.findViewById(R.id.txtFarmerFirst)
        var txtLastFarmer: TextView = view.findViewById(R.id.txtLastFarmer)
        var txtLocationFarmer: TextView = view.findViewById(R.id.txtLocationFarmer)
    }

    fun getData(dat: ArrayList<farmer>){
        for (i in dat){
            lst.add(i)
            notifyDataSetChanged()
        }

        lstFiltered.addAll(dat)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var fil: ArrayList<farmer> = ArrayList()
                if (constraint.isNullOrEmpty()){
                    fil.addAll(lstFiltered)
                }
                else{
                    var name: String = constraint.toString().lowercase(Locale.getDefault())
                    for (i in lstFiltered){
                        if (i.firstName.toString().lowercase().contains(name)){
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
                lst.addAll(results!!.values as ArrayList<farmer>)
                notifyDataSetChanged()
            }
        }
    }
}