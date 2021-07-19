package com.example.emkulimaapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.ProductRetrofit
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.interfaces.ProductInterface
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsFragment : Fragment() {
    @BindView(R.id.recyclerProducts)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.prodsBack)
    lateinit var back: ImageView
    @BindView(R.id.titleProds)
    lateinit var title: TextView
    @BindView(R.id.searchProds)
    lateinit var search: ImageView

    private lateinit var productAdapter: ProductAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productInterface: ProductInterface
    private lateinit var generalInterface: GeneralInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_products, null)
        ButterKnife.bind(this, view)
        this.back.setOnClickListener {
            generalInterface.onBackPressed()
        }
        getProducts()
        return view
    }

    private fun getProducts() {
        var activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)

        productInterface = ProductRetrofit.getRetrofit().create(ProductInterface::class.java)
        val call: Call<AllProducts> = productInterface.getProducts()
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    showData(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showData(all: ArrayList<Product>) {
        productAdapter.getData(all)
        this.recyclerView.adapter = productAdapter
        this.recyclerView.layoutManager = gridLayoutManager
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}