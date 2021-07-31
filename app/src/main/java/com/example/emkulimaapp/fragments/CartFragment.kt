package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CartRetrofit
import com.example.emkulimaapp.RetrofitClasses.RecommendedCartRetrofit
import com.example.emkulimaapp.RetrofitClasses.ViewCartItemsRetrofit
import com.example.emkulimaapp.adapters.CartAdapter
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.adapters.SwipeToDeleteCallBack
import com.example.emkulimaapp.interfaces.CartInterface
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.interfaces.RecommendedCartInterface
import com.example.emkulimaapp.interfaces.ViewCartItemsInterface
import com.example.emkulimaapp.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {
    @BindView(R.id.recyclerCart)
    lateinit var cart: RecyclerView
    @BindView(R.id.cartRecommended)
    lateinit var recommend: RecyclerView
    @BindView(R.id.linearCheckOut)
    lateinit var checkOut: LinearLayout
    @BindView(R.id.txtNoItems)
    lateinit var no: TextView
    @BindView(R.id.totalPrice)
    lateinit var price: TextView
    @BindView(R.id.linCart)
    lateinit var linCart: LinearLayout
    @BindView(R.id.progressCart)
    lateinit var progress: ProgressBar
    @BindView(R.id.relSnack)
    lateinit var snack: RelativeLayout
    @BindView(R.id.swipeCart)
    lateinit var swipe: SwipeRefreshLayout

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var viewCartItemsInterface: ViewCartItemsInterface
    private lateinit var generalInterface: GeneralInterface
    private lateinit var recommendedCartInterface: RecommendedCartInterface

    private var lstAll: ArrayList<Product> = ArrayList()
    private var lstFiltered: ArrayList<Product> = ArrayList()
    private var lstData: ArrayList<Cart> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_cart, null)
        ButterKnife.bind(this, view)
        checkOut.setOnClickListener {
            goToCheckOut()
        }
        no.visibility = View.VISIBLE
        cart.visibility = View.GONE

        linCart.visibility = View.GONE
        progress.visibility = View.VISIBLE

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            if (lstFiltered.size > 0 && lstData.size > 0){
                cartAdapter.clear()
                cartAdapter.getData(lstFiltered, lstData)
            }
            else{
                showCart()
            }

        }

        showCart()
        showRecommendation()
        enableSwipeToDelete()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun enableSwipeToDelete() {
        val activity = activity as Context
        val swipeToDeleteCallBack: SwipeToDeleteCallBack = SwipeToDeleteCallBack(activity, cartAdapter, snack)
        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(cart)
    }

    private fun goToCheckOut() {
        generalInterface.goToCheckout(lstFiltered)
    }

    private fun showCart() {
        val activity = activity as Context
        linearLayoutManager = LinearLayoutManager(activity)
        cartAdapter = CartAdapter(activity)

        var sharedPreferences: SharedPreferences = activity.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        if (userId == ""){

        }
        else{
            viewCartItemsInterface = ViewCartItemsRetrofit.getRetrofit().create(ViewCartItemsInterface::class.java)
            val call: Call<AllCart> = viewCartItemsInterface.viewItems(userId)
            call.enqueue(object : Callback<AllCart>{
                override fun onResponse(call: Call<AllCart>, response: Response<AllCart>) {
                    if (response.isSuccessful){
                        no.visibility = View.GONE
                        cart.visibility = View.VISIBLE
                        linCart.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                        getData(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<AllCart>, t: Throwable) {
                    Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    private fun getData(data: ArrayList<Cart>) {
        lstData.addAll(data)
        if (data.size > 0){
            for (j in lstAll.indices){
                for (l in data.indices){
                    if (lstAll[j].productId == data[l].productId){
                        lstFiltered.add(lstAll[j])
                    }
                }
            }
        }
        else{

        }


        if (lstFiltered.size > 0){
            no.visibility = View.GONE
            cart.visibility = View.VISIBLE
            cartAdapter.getData(lstFiltered, data)
            this.cart.adapter = cartAdapter
            this.cart.layoutManager = linearLayoutManager

            var y: Int = 0
            for(i in lstFiltered.indices){
                if (lstFiltered[i].price == null){
                    y = 0
                }
                else{
                    y += lstFiltered[i].price!!
                }

            }
            price.text = getString(R.string.MONEY) + y.toString()
        }
        else{
            no.visibility = View.VISIBLE
            cart.visibility = View.GONE
        }
    }

    private fun showRecommendation() {
        val activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)

        recommendedCartInterface = RecommendedCartRetrofit.getRetrofit().create(RecommendedCartInterface::class.java)
        val call: Call<AllProducts> = recommendedCartInterface.getProducts()
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    setRecommended(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun setRecommended(all: ArrayList<Product>) {
        lstAll.addAll(all)
        productAdapter.getData(all)
        recommend.adapter = productAdapter
        recommend.layoutManager = gridLayoutManager
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }

    override fun onResume() {
        super.onResume()
    }
}