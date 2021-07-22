package com.example.emkulimaapp.fragments

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
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.SearchRetrofit
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.adapters.SearchAdapter
import com.example.emkulimaapp.interfaces.SearchInterface
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    @BindView(R.id.imgBackSearch)
    lateinit var back: ImageView
    @BindView(R.id.recyclerSearch)
    lateinit var search: RecyclerView
    @BindView(R.id.editSearch)
    lateinit var edit: EditText

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchInterface: SearchInterface
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_search, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.productsFragment)
        }

        var activity = activity as Context
        searchAdapter = SearchAdapter(activity)
        linearLayoutManager = LinearLayoutManager(activity)
        getData()

        edit.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return view
    }

    private fun getData() {
        searchInterface = SearchRetrofit.getRetrofit().create(SearchInterface::class.java)
        val call: Call<AllProducts> = searchInterface.getProducts()
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    setData(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setData(all: ArrayList<Product>) {
        searchAdapter.getData(all)
        search.adapter = searchAdapter
        search.layoutManager = linearLayoutManager
    }
}