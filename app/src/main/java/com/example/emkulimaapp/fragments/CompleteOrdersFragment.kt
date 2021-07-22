package com.example.emkulimaapp.fragments

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
import com.example.emkulimaapp.RetrofitClasses.CompleteOrdersRetrofit
import com.example.emkulimaapp.RetrofitClasses.PendingOrdersRetrofit
import com.example.emkulimaapp.adapters.OrdersAdapter
import com.example.emkulimaapp.interfaces.CompleteOrdersInterface
import com.example.emkulimaapp.interfaces.PendingOrdersInterface
import com.example.emkulimaapp.models.AllOrders
import com.example.emkulimaapp.models.Orders
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteOrdersFragment : Fragment() {

    @BindView(R.id.recyclerCompleteOrders)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.txtNoOrder)
    lateinit var txt: TextView
    @BindView(R.id.progressCompleted)
    lateinit var progress: ProgressBar

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var completeOrdersInterface: CompleteOrdersInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = View.inflate(activity, R.layout.fragment_complete_orders, null)
        ButterKnife.bind(this, view)
        txt.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        getData()
        return view
    }

    private fun getData() {
        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
            AppCompatActivity.MODE_PRIVATE
        )!!
        var uId = sharedPreferences.getString("USERID", "1")!!

        completeOrdersInterface = CompleteOrdersRetrofit.getRetrofit().create(CompleteOrdersInterface::class.java)
        val call: Call<AllOrders> = completeOrdersInterface.getCompleted(uId)
        call.enqueue(object : Callback<AllOrders> {
            override fun onResponse(call: Call<AllOrders>, response: Response<AllOrders>) {
                if (response.isSuccessful){
                    progress.visibility = View.GONE
                    setData(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AllOrders>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
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