package com.example.emkulimaapp.farmer.fragments

import android.content.Context
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.R
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.farmer.adapters.FarmerAdapter
import com.example.emkulimaapp.farmer.models.farmer
import com.example.emkulimaapp.models.Product
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SelectedLocationFragment : Fragment() {
    @BindView(R.id.locationProdsBack)
    lateinit var back: ImageView
    @BindView(R.id.titleLocationProds)
    lateinit var title: TextView
    @BindView(R.id.searchProdsLocation)
    lateinit var search: ImageView
    @BindView(R.id.recyclerLocationProducts)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.progressSelectedLocation)
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
        val view: View = View.inflate(activity, R.layout.fragment_selected_location, null)
        ButterKnife.bind(this, view)
        progress.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        back.setOnClickListener {
            findNavController().navigate(R.id.productsFragment)
        }
        search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        setData()
        return view
    }
    private fun setData() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("LOCATION", Context.MODE_PRIVATE)!!
        var name: String = sharedPreferences.getString("NAME", "")!!
        title.text = name
        var myReq: RequestQueue = Volley.newRequestQueue(activity)

        var jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, constants.BASE_URL + "farmer/allfarmers", null,
            {
                try {
                    var jsonArray: JSONArray = it.getJSONArray("data")
                    var farmerListAdded: ArrayList<farmer> = ArrayList()
                    for (i in 0..(jsonArray.length() - 1)){
                        var jsonObj: JSONObject = jsonArray.getJSONObject(i)
                        var fmr: farmer = farmer()
                        fmr.farmerId = jsonObj.getInt("farmer_id")
                        fmr.firstName = jsonObj.getString("first_name")
                        fmr.lastName = jsonObj.getString("last_name")
                        fmr.email = jsonObj.getString("email")
                        fmr.bio = jsonObj.getString("bio")
                        fmr.gender = jsonObj.getString("gender")
                        fmr.idNumber = jsonObj.getInt("id_number")
                        fmr.phoneNumber = jsonObj.getString("phone_number")
                        fmr.profilePicture = jsonObj.getString("profile_pic")
                        fmr.location = jsonObj.getString("location")

                        farmerListAdded.add(fmr)
                    }

                    var newLst: ArrayList<farmer> = ArrayList()

                    if (farmerListAdded.size > 0){
                        for (i in farmerListAdded.indices){
                            if (farmerListAdded[i].location.toString().lowercase() == name.toString().lowercase()){
                                newLst.add(farmerListAdded[i])
                            }
                        }

                        getProducts(newLst)
                    }
                }
                catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            {

            })

        myReq.add(jsonObjectRequest)
    }

    private fun getProducts(newLst: ArrayList<farmer>) {
        var myRequest: RequestQueue = Volley.newRequestQueue(activity)

        var jsonArray: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, constants.BASE_URL + "customer/products", null,
            {
                try {
                    var jsonArray: JSONArray = it.getJSONArray("data")
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


                    var outList: ArrayList<Product> = ArrayList()

                    for (i in farmerListAdded.indices){
                        for (j in newLst.indices){
                            if (farmerListAdded[i].farmerId == newLst[j].farmerId){
                                outList.add(farmerListAdded[i])
                            }
                            else{
                                //Toast.makeText(activity, outList.size.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    if (outList.size > 0){
                        var activity = activity as Context
                        gridLayoutManager = GridLayoutManager(activity, 2)
                        productsAdapter = ProductAdapter(activity)
                        progress.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        productsAdapter.getData(outList)
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