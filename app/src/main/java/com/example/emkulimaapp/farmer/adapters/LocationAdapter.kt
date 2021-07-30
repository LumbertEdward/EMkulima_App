package com.example.emkulimaapp.farmer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.farmer.models.farmer
import com.example.emkulimaapp.interfaces.GeneralInterface
import java.util.*
import kotlin.collections.ArrayList

open class LocationAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var lst: ArrayList<String> = ArrayList()
    private var lstFiltered: ArrayList<String> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.locationName.text = lst[position]
        myViewHolder.cardLocation.setOnClickListener {
            generalInterface.sendLocation(lst[position])
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
        var locationName: TextView = view.findViewById(R.id.locationName)
        var cardLocation: CardView = view.findViewById(R.id.cardLocation)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var fil: ArrayList<String> = ArrayList()
                if (constraint.isNullOrEmpty()){
                    fil.addAll(lstFiltered)
                }
                else{
                    var name: String = constraint.toString().lowercase(Locale.getDefault())
                    for (i in lstFiltered){
                        if (i.lowercase().contains(name)){
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
                lst.addAll(results!!.values as ArrayList<String>)
                notifyDataSetChanged()
            }

        }
    }

    fun getData(dat: ArrayList<String>){
        for (i in dat){
            lst.add(i)
            notifyDataSetChanged()
        }

        lstFiltered.addAll(dat)
        notifyDataSetChanged()
    }
}