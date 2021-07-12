package com.example.emkulimaapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.adapters.CategoryAdapter
import com.example.emkulimaapp.adapters.TakeAdapter
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Product

class HomeFragment : Fragment() {
    @BindView(R.id.cardFruits)
    lateinit var fruits: CardView
    @BindView(R.id.cardVegetables)
    lateinit var vegetables: CardView
    @BindView(R.id.cardDairy)
    lateinit var dairy: CardView
    @BindView(R.id.cardMeat)
    lateinit var meat: CardView
    @BindView(R.id.recyclerTake)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.recyclerVegetables)
    lateinit var recyclerVeg: RecyclerView
    @BindView(R.id.recyclerFruits)
    lateinit var recyclerFruits: RecyclerView
    @BindView(R.id.viewTake)
    lateinit var take: TextView

    private lateinit var takeLinearLayoutManager: LinearLayoutManager
    private lateinit var categoryLinearLayoutManager: LinearLayoutManager
    private lateinit var fruitsLinearLayoutManager: LinearLayoutManager
    private lateinit var takeAdapter: TakeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var generalInterface: GeneralInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = View.inflate(activity, R.layout.fragment_home, null)
        ButterKnife.bind(this, view)
        setSelectedCategory()
        setTakeItems()
        setVegetablesCategory()
        setFruitsCategory()
        return view
    }

    private fun setSelectedCategory() {
        take.setOnClickListener {
            generalInterface.getAllProducts()
        }
    }

    private fun setFruitsCategory() {
        val activity = activity as Context
        categoryAdapter = CategoryAdapter(activity)
        fruitsLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val lst: ArrayList<Product> = ArrayList()
        lst.add(Product("Fresh Orange", 300, "", "10 mins", "240 kcal", "Fruit"))
        lst.add(Product("Fresh Cherry", 400, "", "14 mins", "230 kcal", "Fruit"))
        lst.add(Product("Japan Apple", 600, "", "20 mins", "249 kcal", "Fruit"))

        if (lst.size > 0){
            categoryAdapter.getCategory(lst)
            this.recyclerFruits.adapter = categoryAdapter
            this.recyclerFruits.layoutManager = fruitsLinearLayoutManager
        }
    }

    private fun setVegetablesCategory() {
        val activity = activity as Context
        categoryAdapter = CategoryAdapter(activity)
        categoryLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val lst: ArrayList<Product> = ArrayList()
        lst.add(Product("Fresh Orange", 300, "", "10 mins", "240 kcal", "Fruit"))
        lst.add(Product("Fresh Cherry", 400, "", "14 mins", "230 kcal", "Fruit"))
        lst.add(Product("Japan Apple", 600, "", "20 mins", "249 kcal", "Fruit"))

        if (lst.size > 0){
            categoryAdapter.getCategory(lst)
            this.recyclerVeg.adapter = categoryAdapter
            this.recyclerVeg.layoutManager = categoryLinearLayoutManager
        }
    }

    private fun setTakeItems() {
        val activity = activity as Context
        takeAdapter = TakeAdapter(activity)
        takeLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val lst: ArrayList<Product> = ArrayList()
        lst.add(Product("Fresh Orange", 300, "", "10 mins", "240 kcal", "Fruit"))
        lst.add(Product("Fresh Cherry", 400, "", "14 mins", "230 kcal", "Fruit"))
        lst.add(Product("Japan Apple", 600, "", "20 mins", "249 kcal", "Fruit"))

        if (lst.size > 0){
            takeAdapter.getProducts(lst)
            this.recyclerView.layoutManager = takeLinearLayoutManager
            this.recyclerView.adapter = takeAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}