package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.PendingOrdersRetrofit
import com.example.emkulimaapp.adapters.OrdersAdapter
import com.example.emkulimaapp.interfaces.PendingOrdersInterface
import com.example.emkulimaapp.models.AllOrders
import com.example.emkulimaapp.models.Orders
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingOrdersFragment : Fragment() {
    @BindView(R.id.recyclerPendingOrders)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.progressPending)
    lateinit var pending: ProgressBar
    @BindView(R.id.txtNoPendingOrder)
    lateinit var txt: TextView

    private lateinit var pendingOrdersInterface: PendingOrdersInterface
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_pending_orders, null)
        ButterKnife.bind(this, view)
        recyclerView.visibility = View.GONE
        pending.visibility = View.VISIBLE
        txt.visibility = View.GONE
        var activity = activity as Context
        linearLayoutManager = LinearLayoutManager(activity)
        ordersAdapter = OrdersAdapter(activity)
        getData()
        return view
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
            AppCompatActivity.MODE_PRIVATE
        )!!
        var uId = sharedPreferences.getString("USERID", "1")!!

        pendingOrdersInterface = PendingOrdersRetrofit.getRetrofit().create(PendingOrdersInterface::class.java)
        val call: Call<AllOrders> = pendingOrdersInterface.getPending(uId)
        call.enqueue(object : Callback<AllOrders>{
            override fun onResponse(call: Call<AllOrders>, response: Response<AllOrders>) {
                if (response.isSuccessful){
                    pending.visibility = View.GONE
                    setData(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AllOrders>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setData(data: ArrayList<Orders>) {
        if (data.size > 0){
            ordersAdapter.getData(data)
            recyclerView.adapter = ordersAdapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.visibility = View.VISIBLE
        }
        else{
            txt.visibility = View.VISIBLE
        }

    }

}