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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.models.Product

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
        getProducts()
        return view
    }

    private fun getProducts() {
        var activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)
        val lst: ArrayList<Product> = ArrayList()
        lst.add(Product("Fresh Orange", 300, "", "10 mins", "240 kcal", "Fruit"))
        lst.add(Product("Fresh Cherry", 400, "", "14 mins", "230 kcal", "Fruit"))
        lst.add(Product("Japan Apple", 600, "", "20 mins", "249 kcal", "Fruit"))

        if (lst.size > 0){
            productAdapter.getData(lst)
            this.recyclerView.adapter = productAdapter
            this.recyclerView.layoutManager = gridLayoutManager
        }
    }
}