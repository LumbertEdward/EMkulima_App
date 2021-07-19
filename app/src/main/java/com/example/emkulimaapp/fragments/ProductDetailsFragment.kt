package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CartRetrofit
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.CartInterface
import com.example.emkulimaapp.models.Product
import com.example.emkulimaapp.models.message
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsFragment : Fragment() {
    @BindView(R.id.relBackDetails)
    lateinit var back: RelativeLayout
    @BindView(R.id.cardAddToCart)
    lateinit var cart: CardView
    @BindView(R.id.prodDetailImage)
    lateinit var img: ImageView
    @BindView(R.id.prodDetailTitle)
    lateinit var title: TextView
    @BindView(R.id.prodDetailPrice)
    lateinit var price: TextView
    @BindView(R.id.prodDetailDescription)
    lateinit var description: TextView
    @BindView(R.id.recyclerRecommendDetail)
    lateinit var recyclerView: RecyclerView

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartInterface: CartInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_product_details, null)
        ButterKnife.bind(this, view)
        this.back.setOnClickListener {

        }

        this.cart.setOnClickListener {
            sendToCart()
        }
        setDetails()
        setRecommended()
        return view
    }

    private fun sendToCart() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1")
        var productContent = arguments?.getParcelable<Product>("PRODUCT")

        cartInterface = CartRetrofit.getRetrofit().create(CartInterface::class.java)
        var call: Call<message> = cartInterface.addToCart(productContent!!.productId!!, productContent.farmerId!!, userId!!.toInt(), productContent.name!!, productContent.desc!!, productContent.price!!, productContent.img!!, productContent.type!!, productContent.calcs!!, productContent.time!!)
        call.enqueue(object : Callback<message> {
            override fun onResponse(call: Call<message>, response: Response<message>) {
                if (response.isSuccessful){
                    Toast.makeText(activity, "Added", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<message>, t: Throwable) {
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun setDetails() {
        val product = arguments?.getParcelable<Product>("PRODUCT")
        if (product != null){
            var img = product.img.toString()
            var title = product.name.toString()
            var price = product.price.toString()
            var description = product.desc.toString()

            this.title.text = title
            this.price.text = price
            this.description.text = description

            val activity = activity as Context
            val picasso: Picasso.Builder = Picasso.Builder(activity)
            picasso.downloader(OkHttp3Downloader(activity))
            picasso.build().load(constants.BASE_URL + img).into(this.img)
        }
    }

    private fun setRecommended() {
        val activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)
    }
}