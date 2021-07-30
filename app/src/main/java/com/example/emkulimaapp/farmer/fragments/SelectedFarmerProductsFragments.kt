package com.example.emkulimaapp.farmer.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.R
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.farmer.models.farmer
import com.example.emkulimaapp.models.Product
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SelectedFarmerProductsFragments : Fragment() {
    @BindView(R.id.farmerProdsBack)
    lateinit var back: ImageView
    @BindView(R.id.titleFarmerProds)
    lateinit var title: TextView
    @BindView(R.id.searchProdsFarmer)
    lateinit var search: ImageView
    @BindView(R.id.recyclerFarmerProducts)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.progressSelectedFarmer)
    lateinit var progress: ProgressBar

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = View.inflate(activity, R.layout.fragment_selected_farmer_products_fragments, null)
        ButterKnife.bind(this, view)
        progress.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        back.setOnClickListener {
            findNavController().navigate(R.id.productsFragment)
        }
        search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        back.setOnClickListener {
            findNavController().navigate(R.id.searchByFarmerFragment)
        }
        getProducts()
        return view
    }

    private fun getProducts() {

        var myRequest: RequestQueue = Volley.newRequestQueue(activity)

        var jsonArray: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, constants.BASE_URL + "customer/products", null,
            {
                try {
                    var jsonArray: JSONArray = it.getJSONArray("data")
                    var farmerList: ArrayList<Product> = ArrayList()
                    var farmerListAdded: ArrayList<Product> = ArrayList()
                    for (i in 0..(jsonArray.length() - 1)){
                        var jsonObj: JSONObject = jsonArray.getJSONObject(i)
                        var fmr: Product = Product()
                        fmr.farmerId = jsonObj.getInt("farmer_id")
                        fmr.productId = jsonObj.getInt("product_id")
                        fmr.img = jsonObj.getString("product_image")
                        fmr.calcs = jsonObj.getInt("product_calcs")
                        fmr.desc = jsonObj.getString("product_description")
                        fmr.name = jsonObj.getString("product_name")
                        fmr.price = jsonObj.getInt("product_price")
                        fmr.time = jsonObj.getString("product_delivery_time")
                        fmr.type = jsonObj.getString("product_type")

                        farmerListAdded.add(fmr)
                    }

                    var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("FARMERID", Context.MODE_PRIVATE)!!
                    var id: Int = sharedPreferences.getInt("ID", 1)
                    var name: String = sharedPreferences.getString("NAME", "")!!

                    title.text = name

                    for (i in farmerListAdded.indices){
                        if (farmerListAdded[i].farmerId == id){
                            farmerList.add(farmerListAdded[i])
                        }
                    }
                    var activity = activity as Context
                    gridLayoutManager = GridLayoutManager(activity, 2)
                    productsAdapter = ProductAdapter(activity)

                    if (farmerList.size > 0){
                        progress.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        productsAdapter.getData(farmerList)
                        recyclerView.adapter = productsAdapter
                        recyclerView.layoutManager = gridLayoutManager
                    }

                }
                catch (e: JSONException){

                }
            },
            {

            })

        myRequest.add(jsonArray)

    }
}