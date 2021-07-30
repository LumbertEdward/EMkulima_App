package com.example.emkulimaapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.ViewAllProductsRetrofit
import com.example.emkulimaapp.RetrofitClasses.ViewOrderProductsRetrofit
import com.example.emkulimaapp.adapters.OrderProductsAdapter
import com.example.emkulimaapp.interfaces.ViewAllProductsInterface
import com.example.emkulimaapp.interfaces.ViewOrderProductsInterface
import com.example.emkulimaapp.models.AllOrderItems
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.OrderItems
import com.example.emkulimaapp.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewSelectedOrdersFragment : Fragment() {
    @BindView(R.id.imgOrderBack)
    lateinit var back: ImageView
    @BindView(R.id.orderIdNumber)
    lateinit var txtId: TextView
    @BindView(R.id.recyclerOrderProducts)
    lateinit var prodsRecycler: RecyclerView
    @BindView(R.id.progressViewOrder)
    lateinit var progress: ProgressBar

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var orderProductsAdapter: OrderProductsAdapter
    private lateinit var viewOrderProductsInterface: ViewOrderProductsInterface
    private lateinit var viewAllProductsInterface: ViewAllProductsInterface

    private var prods: ArrayList<Product> = ArrayList()
    private var items: ArrayList<Product> = ArrayList()
    private var status: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_view_selected_orders, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.ordersFragment)
        }

        progress.visibility = View.VISIBLE
        prodsRecycler.visibility = View.GONE
        getProducts()
        getData()
        return view
    }

    private fun getData() {
        viewAllProductsInterface = ViewAllProductsRetrofit.getRetrofit().create(ViewAllProductsInterface::class.java)
        val call: Call<AllProducts> = viewAllProductsInterface.getProducts()
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    prods.addAll(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getProducts() {
        var orderId: String = arguments?.getString("ORDERID", "1")!!
        txtId.text = "Order #" + orderId
        viewOrderProductsInterface = ViewOrderProductsRetrofit.getRetrofit().create(ViewOrderProductsInterface::class.java)
        val call: Call<AllOrderItems> = viewOrderProductsInterface.getProducts(orderId)
        call.enqueue(object : Callback<AllOrderItems>{
            override fun onResponse(call: Call<AllOrderItems>, response: Response<AllOrderItems>) {
                if (response.isSuccessful){
                    setProducts(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AllOrderItems>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setProducts(data: ArrayList<OrderItems>) {
        if (data.size > 0){
            for (i in prods.indices){
                for (j in data.indices){
                    if (prods[i].productId == data[j].productId){
                        items.add(prods[i])
                        status.add(data[j].status!!)
                    }
                }
            }

            if (items.size > 0){
                progress.visibility = View.GONE
                prodsRecycler.visibility = View.VISIBLE

                var activity = activity as Context
                linearLayoutManager = LinearLayoutManager(activity)
                orderProductsAdapter = OrderProductsAdapter(activity)

                orderProductsAdapter.getData(items, status)
                prodsRecycler.adapter = orderProductsAdapter
                prodsRecycler.layoutManager = linearLayoutManager
            }
            else{
                //Toast.makeText(activity, "No Data is there", Toast.LENGTH_LONG).show()
            }
        }
    }

}