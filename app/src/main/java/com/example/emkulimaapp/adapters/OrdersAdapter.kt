package com.example.emkulimaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Orders

open class OrdersAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lst: ArrayList<Orders> = ArrayList()
    private lateinit var generalInterface: GeneralInterface
    private val FADE_DURATION: Long = 1000
    private val LAST_POSITION: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder: MyViewHolder = holder as MyViewHolder
        myViewHolder.id.text = "#" + lst[position].orderId
        myViewHolder.date.text = lst[position].orderDate
        myViewHolder.status.text = lst[position].status
        myViewHolder.card.setOnClickListener {
            generalInterface.selectedOrder(lst[position].orderId!!)
        }
        setScaleAnimation(myViewHolder.itemView)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        generalInterface = context as GeneralInterface
    }

    protected class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val id: TextView = view.findViewById(R.id.txtOrderId)
        val date: TextView = view.findViewById(R.id.txtOrderDate)
        val status: TextView = view.findViewById(R.id.txtOrderStatus)
        val card: CardView = view.findViewById(R.id.cardOrder)
    }

    fun getData(ord: ArrayList<Orders>){
        for (i in ord){
            lst.add(i)
            notifyDataSetChanged()
        }
    }

    private fun setScaleAnimation(v: View){
        var scaleAnimation: ScaleAnimation = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = FADE_DURATION
        v.animation = scaleAnimation
    }
}