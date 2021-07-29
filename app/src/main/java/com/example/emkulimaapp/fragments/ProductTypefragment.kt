package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.telecom.Call
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
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.ProductTypeRetrofit
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.interfaces.ProductInterface
import com.example.emkulimaapp.interfaces.ProductTypeInterface
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.Product
import retrofit2.Response
import retrofit2.Retrofit

class ProductTypefragment : Fragment() {
    @BindView(R.id.recyclerProductsType)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.prodsTypeBack)
    lateinit var back: ImageView
    @BindView(R.id.titleProdsType)
    lateinit var title: TextView
    @BindView(R.id.searchProdsType)
    lateinit var search: ImageView
    @BindView(R.id.txtNoProductType)
    lateinit var txtProdType: TextView
    @BindView(R.id.progressProdType)
    lateinit var progress: ProgressBar

    private lateinit var productAdapter: ProductAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productTypeInterface: ProductTypeInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = View.inflate(activity, R.layout.fragment_product_typefragment, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        txtProdType.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        getData()
        return view
    }

    private fun getData() {
        var activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)

        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("PRODUCTTYPE", Context.MODE_PRIVATE)
        var type = sharedPreferences.getString("TYPE", "Vegetables")

        title.text = type.toString()

        productTypeInterface = ProductTypeRetrofit.getRetrofit().create(ProductTypeInterface::class.java)
        val call: retrofit2.Call<AllProducts> = productTypeInterface.getProductByType(type.toString())
        call.enqueue(object : retrofit2.Callback<AllProducts>{
            override fun onResponse(
                call: retrofit2.Call<AllProducts>,
                response: Response<AllProducts>
            ) {
                if (response.isSuccessful){
                    progress.visibility = View.GONE
                    showData(response.body()!!.all)
                }
            }

            override fun onFailure(call: retrofit2.Call<AllProducts>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showData(all: ArrayList<Product>) {
        if (all.size > 0){
            productAdapter.getData(all)
            this.recyclerView.adapter = productAdapter
            this.recyclerView.layoutManager = gridLayoutManager
            recyclerView.visibility = View.VISIBLE
        }
        else{
            txtProdType.visibility = View.VISIBLE
        }

    }
}