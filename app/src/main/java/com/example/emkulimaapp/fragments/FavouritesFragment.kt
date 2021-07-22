package com.example.emkulimaapp.fragments

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.FavouritesCheckRetrofit
import com.example.emkulimaapp.RetrofitClasses.ViewFavouritesRetrofit
import com.example.emkulimaapp.adapters.CartAdapter
import com.example.emkulimaapp.adapters.FavouritesAdapter
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.interfaces.FavouritesCheckingProductsInterface
import com.example.emkulimaapp.interfaces.FavouritesInterface
import com.example.emkulimaapp.interfaces.ViewFavourites
import com.example.emkulimaapp.models.AllFavourites
import com.example.emkulimaapp.models.AllProducts
import com.example.emkulimaapp.models.FavouritesModel
import com.example.emkulimaapp.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouritesFragment : Fragment() {
    @BindView(R.id.recyclerViewFavourite)
    lateinit var favourites: RecyclerView
    @BindView(R.id.progressFavourites)
    lateinit var progress: ProgressBar
    @BindView(R.id.txtNoFavourites)
    lateinit var txt: TextView
    @BindView(R.id.swipeFavourites)
    lateinit var swipe: SwipeRefreshLayout
    @BindView(R.id.imgBackFav)
    lateinit var back: ImageView

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var viewFavouritesInterface: ViewFavourites
    private lateinit var productsInterface: FavouritesCheckingProductsInterface
    private var lst: ArrayList<Product> = ArrayList()
    private var favList: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_favourites, null)
        ButterKnife.bind(this, view)
        progress.visibility = View.VISIBLE
        favourites.visibility = View.GONE
        txt.visibility = View.GONE
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            favouritesAdapter.clear()
            favouritesAdapter.getData(favList)
        }
        back.setOnClickListener {
            findNavController().navigate(R.id.productsFragment)
        }
        getFavourites()
        getProducts()
        return view
    }

    private fun getProducts() {
        productsInterface = FavouritesCheckRetrofit.getRetrofit().create(FavouritesCheckingProductsInterface::class.java)
        val call: Call<AllProducts> = productsInterface.getProducts()
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    setList(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {

            }

        })
    }

    private fun setList(all: ArrayList<Product>) {
        lst.addAll(all)
    }

    private fun getFavourites() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        val activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        favouritesAdapter = FavouritesAdapter(activity)

        viewFavouritesInterface = ViewFavouritesRetrofit.getRetrofit().create(ViewFavourites::class.java)
        val call: Call<AllFavourites> = viewFavouritesInterface.getFavourites(userId)
        call.enqueue(object : Callback<AllFavourites>{
            override fun onResponse(call: Call<AllFavourites>, response: Response<AllFavourites>) {
                if (response.isSuccessful){
                    progress.visibility = View.GONE
                    getData(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllFavourites>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getData(all: ArrayList<FavouritesModel>) {
        for (i in lst.indices){
            for (j in all.indices){
                if (lst[i].productId == all[j].product_id){
                    favList.add(lst[i])
                }
            }
        }
        if (favList.size > 0){
            favourites.visibility = View.VISIBLE
            favouritesAdapter.getData(favList)
            favourites.adapter = favouritesAdapter
            favourites.layoutManager = gridLayoutManager
        }
        else{
            txt.visibility = View.VISIBLE
        }

    }

}