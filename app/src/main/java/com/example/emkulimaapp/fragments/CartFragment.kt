package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CartRetrofit
import com.example.emkulimaapp.RetrofitClasses.ViewCartItemsRetrofit
import com.example.emkulimaapp.adapters.CartAdapter
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.interfaces.CartInterface
import com.example.emkulimaapp.interfaces.GeneralInterface
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

    private var lst: ArrayList<Cart> = ArrayList()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var viewCartItemsInterface: ViewCartItemsInterface
    private lateinit var generalInterface: GeneralInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        showCart()
        showRecommendation()
        return view
    }

    private fun goToCheckOut() {
        generalInterface.goToCheckout(lst)
    }

    private fun showCart() {
        val activity = activity as Context
        linearLayoutManager = LinearLayoutManager(activity)
        cartAdapter = CartAdapter(activity)

        var sharedPreferences: SharedPreferences = activity.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        if (userId == ""){
            Toast.makeText(activity, "No Data", Toast.LENGTH_LONG).show()
        }
        else{
            viewCartItemsInterface = ViewCartItemsRetrofit.getRetrofit().create(ViewCartItemsInterface::class.java)
            val call: Call<AllCart> = viewCartItemsInterface.viewItems(userId.toInt())
            call.enqueue(object : Callback<AllCart>{
                override fun onResponse(call: Call<AllCart>, response: Response<AllCart>) {
                    if (response.isSuccessful){
                        no.visibility = View.GONE
                        cart.visibility = View.VISIBLE
                        getData(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<AllCart>, t: Throwable) {
                    Toast.makeText(activity, t.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    private fun getData(data: ArrayList<Cart>) {
        cartAdapter.getData(data)
        this.cart.adapter = cartAdapter
        this.cart.layoutManager = linearLayoutManager

        var y: Int = 0
        for(i in data.indices){
            if (data[i].price == null){
                y = 0
            }
            else{
                y += data[i].price!!
            }

        }
        price.text = getString(R.string.MONEY) + y.toString()

        lst = data
    }

    private fun showRecommendation() {
        val activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}