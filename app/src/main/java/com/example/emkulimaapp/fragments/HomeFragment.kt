package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.Home.ChoicesRetrofit
import com.example.emkulimaapp.RetrofitClasses.Home.FruitsRetrofit
import com.example.emkulimaapp.RetrofitClasses.Home.RecommendedRetrofit
import com.example.emkulimaapp.RetrofitClasses.Home.VegetablesRetrofit
import com.example.emkulimaapp.adapters.CategoryAdapter
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.adapters.TakeAdapter
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.interfaces.Home.ChoiceInterface
import com.example.emkulimaapp.interfaces.Home.FruitInterface
import com.example.emkulimaapp.interfaces.Home.RecommendedProductsInterface
import com.example.emkulimaapp.interfaces.Home.VegetableInterface
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.Product
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    @BindView(R.id.cardFruits)
    lateinit var fruits: CardView
    @BindView(R.id.cardVegetables)
    lateinit var vegetables: CardView
    @BindView(R.id.cardDairy)
    lateinit var dairy: CardView
    @BindView(R.id.cardMeat)
    lateinit var meat: CardView
    @BindView(R.id.cardPoultry)
    lateinit var poultry: CardView
    @BindView(R.id.recyclerTake)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.recyclerVegetables)
    lateinit var recyclerVeg: RecyclerView
    @BindView(R.id.recyclerFruits)
    lateinit var recyclerFruits: RecyclerView
    @BindView(R.id.viewTake)
    lateinit var take: TextView
    @BindView(R.id.viewVegetables)
    lateinit var takeVegetables: TextView
    @BindView(R.id.viewFruits)
    lateinit var takeFruits: TextView
    @BindView(R.id.recommended)
    lateinit var recommend: RecyclerView
    @BindView(R.id.progressHome)
    lateinit var progress: ProgressBar
    @BindView(R.id.scrollHome)
    lateinit var scroll: ScrollView
    @BindView(R.id.swipeHome)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.imgNotificationIcon)
    lateinit var notIcon: ImageView
    @BindView(R.id.txtNotNumber)
    lateinit var notNumb: TextView
    @BindView(R.id.shimmerFrame)
    lateinit var shimmeFrame: ShimmerFrameLayout
    @BindView(R.id.shimmerVegetables)
    lateinit var shimmeVeg: ShimmerFrameLayout
    @BindView(R.id.shimmerFruit)
    lateinit var shimmeFruit: ShimmerFrameLayout

    private lateinit var takeLinearLayoutManager: LinearLayoutManager
    private lateinit var categoryLinearLayoutManager: LinearLayoutManager
    private lateinit var fruitsLinearLayoutManager: LinearLayoutManager
    private lateinit var recommendedLayoutManager: GridLayoutManager
    private lateinit var takeAdapter: TakeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var generalInterface: GeneralInterface
    private lateinit var productAdapter: ProductAdapter
    //
    private lateinit var recommendedProductsInterface: RecommendedProductsInterface
    private lateinit var fruitInterface: FruitInterface
    private lateinit var vegetableInterface: VegetableInterface
    private lateinit var choiceInterface: ChoiceInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = View.inflate(activity, R.layout.fragment_home, null)
        ButterKnife.bind(this, view)
        this.progress.visibility = View.GONE
        //this.scroll.visibility = View.GONE
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.green, Resources.getSystem().newTheme()))
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
        shimmeFrame.startShimmer()
        shimmeFruit.startShimmer()
        shimmeVeg.startShimmer()
        shimmeFrame.visibility = View.VISIBLE
        shimmeVeg.visibility = View.VISIBLE
        shimmeFruit.visibility = View.VISIBLE
        notIcon.setOnClickListener {
            findNavController().navigate(R.id.notificationsFragment)
        }
        notNumb.visibility = View.GONE
        setTypes()
        setSelectedCategory()
        setTakeItems()
        setVegetablesCategory()
        setFruitsCategory()
        setRecommended()
        setNotifications()
        return view
    }

    private fun setNotifications() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)!!
        var total: Int = sharedPreferences.getInt("TOTAL", 0)
        if (total > 0){
            notNumb.visibility = View.VISIBLE
            notNumb.text = total.toString()
        }
        else{
            notNumb.visibility = View.GONE
        }
    }

    private fun setTypes() {
        this.vegetables.setOnClickListener {
            val type = "Vegetable"
            generalInterface.getTypeSelected(type)
        }

        this.fruits.setOnClickListener {
            val type = "Fruit"
            generalInterface.getTypeSelected(type)
        }

        this.meat.setOnClickListener {
            val type = "Meat"
            generalInterface.getTypeSelected(type)
        }

        this.dairy.setOnClickListener {
            val type = "Dairy"
            generalInterface.getTypeSelected(type)
        }
        poultry.setOnClickListener {
            val type = "Poultry"
            generalInterface.getTypeSelected(type)
        }
    }

    private fun setRecommended() {
        val activity = activity as Context
        recommendedLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)

        recommendedProductsInterface = RecommendedRetrofit.getRetrofit().create(RecommendedProductsInterface::class.java)
        val call: Call<AllProducts> = recommendedProductsInterface.getProducts()
        call.enqueue(object: Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    getData(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getData(all: ArrayList<Product>) {
        productAdapter.getData(all)
        this.recommend.adapter = productAdapter
        this.recommend.layoutManager = recommendedLayoutManager
    }

    private fun setSelectedCategory() {
        this.take.setOnClickListener {
            generalInterface.getAllProducts()
        }

        this.takeFruits.setOnClickListener {
            val type = "Fruit"
            generalInterface.getTypeSelected(type)
        }

        this.takeVegetables.setOnClickListener {
            val type = "Vegetable"
            generalInterface.getTypeSelected(type)
        }
    }

    private fun setFruitsCategory() {
        recyclerFruits.visibility = View.GONE
        val activity = activity as Context
        categoryAdapter = CategoryAdapter(activity)
        fruitsLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        fruitInterface = FruitsRetrofit.getRetrofit().create(FruitInterface::class.java)
        val call: Call<AllProducts> = fruitInterface.getProductByType("Fruit")
        call.enqueue(object: Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    recyclerFruits.visibility = View.VISIBLE
                    shimmeFruit.visibility = View.GONE
                    getFruits(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getFruits(all: ArrayList<Product>) {
        categoryAdapter.getCategory(all)
        this.recyclerFruits.adapter = categoryAdapter
        this.recyclerFruits.layoutManager = fruitsLinearLayoutManager

    }

    private fun setVegetablesCategory() {
        recyclerVeg.visibility = View.GONE
        val activity = activity as Context
        categoryAdapter = CategoryAdapter(activity)
        categoryLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        vegetableInterface = VegetablesRetrofit.getRetrofit().create(VegetableInterface::class.java)
        val call: Call<AllProducts> = vegetableInterface.getProductByType("Vegetable")
        call.enqueue(object: Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    recyclerVeg.visibility = View.VISIBLE
                    shimmeVeg.visibility = View.GONE
                    getVegetables(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getVegetables(all: ArrayList<Product>) {
        categoryAdapter.getCategory(all)
        this.recyclerVeg.adapter = categoryAdapter
        this.recyclerVeg.layoutManager = categoryLinearLayoutManager
    }

    private fun setTakeItems() {
        recyclerView.visibility = View.GONE
        val activity = activity as Context
        takeAdapter = TakeAdapter(activity)
        takeLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        choiceInterface = ChoicesRetrofit.getRetrofit().create(ChoiceInterface::class.java)
        val call: Call<AllProducts> = choiceInterface.getProducts()
        call.enqueue(object: Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    shimmeFrame.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    getChoice(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getChoice(all: ArrayList<Product>) {
        takeAdapter.getProducts(all)
        this.recyclerView.layoutManager = takeLinearLayoutManager
        this.recyclerView.adapter = takeAdapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}