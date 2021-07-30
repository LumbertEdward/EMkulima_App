package com.example.emkulimaapp.farmer.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.R
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.farmer.adapters.LocationAdapter
import com.example.emkulimaapp.farmer.models.farmer
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class SearchByLocationFragment : Fragment() {
    @BindView(R.id.imgBackSearchLocation)
    lateinit var back: ImageView
    @BindView(R.id.editSearchLocation)
    lateinit var search: EditText
    @BindView(R.id.recyclerSearchLocation)
    lateinit var recyclerView: RecyclerView

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_search_by_location, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        setData()
        searchProducts()
        return view
    }

    private fun searchProducts() {
        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                locationAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setData() {
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

                    if (farmerListAdded.size > 0){
                        var lst: ArrayList<String> = ArrayList()
                        for (i in farmerListAdded.indices){
                            lst.add(farmerListAdded[i].location.toString())
                        }

                        if (lst.size > 0){
                            var activity = activity as Context
                            locationAdapter = LocationAdapter(activity)
                            linearLayoutManager = LinearLayoutManager(activity)
                            locationAdapter.getData(lst)
                            recyclerView.adapter = locationAdapter
                            recyclerView.layoutManager = linearLayoutManager
                        }
                        else{
                            //Toast.makeText(activity, "No Data", Toast.LENGTH_LONG).show()
                        }
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

}