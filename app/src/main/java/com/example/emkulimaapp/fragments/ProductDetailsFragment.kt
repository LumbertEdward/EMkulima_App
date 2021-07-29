package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CartRetrofit
import com.example.emkulimaapp.RetrofitClasses.FavouritesRetrofit
import com.example.emkulimaapp.RetrofitClasses.RecommendedDetailsRetrofit
import com.example.emkulimaapp.adapters.ProductAdapter
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.CartInterface
import com.example.emkulimaapp.interfaces.FavouritesInterface
import com.example.emkulimaapp.interfaces.RecommendedDetailsInterface
import com.example.emkulimaapp.models.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
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
    @BindView(R.id.imgFavDet)
    lateinit var fav: ImageView
    @BindView(R.id.imgSubtract)
    lateinit var subtract: ImageView
    @BindView(R.id.imgAdd)
    lateinit var add: ImageView
    @BindView(R.id.cartItemQuantity)
    lateinit var quantity: TextView
    @BindView(R.id.scrollDetails)
    lateinit var scroll: ScrollView

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartInterface: CartInterface
    private lateinit var recommendedDetailsInterface: RecommendedDetailsInterface
    private lateinit var favouritesInterface: FavouritesInterface

    private var requestQueue: RequestQueue? = null
    private var jsonObjectRequest: JsonObjectRequest? = null

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
            findNavController().navigate(R.id.productsFragment)
        }

        this.cart.setOnClickListener {
            sendToCart()
        }

        fav.setOnClickListener {
            addToFav()
        }
        setDetails()
        setRecommended()
        return view
    }

    private fun addToFav() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        var productContent = arguments?.getParcelable<Product>("PRODUCT")
        val product_id = productContent!!.productId!!

        favouritesInterface = FavouritesRetrofit.getRetrofit().create(FavouritesInterface::class.java)
        var call: Call<AllFavourites> = favouritesInterface.checkOut(userId, product_id)
        call.enqueue(object : Callback<AllFavourites>{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(call: Call<AllFavourites>, response: Response<AllFavourites>) {
                if (response.isSuccessful){
                    var snackbar: Snackbar = Snackbar.make(scroll, "Added To Favourites", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO", View.OnClickListener {
                        val snackbar1: Snackbar = Snackbar.make(scroll, "REMOVED", Snackbar.LENGTH_LONG)
                        snackbar1.setBackgroundTint(resources.getColor(R.color.green, Resources.getSystem().newTheme()))
                        removeFromFavourites(response.body()!!.all)
                        snackbar1.show()
                    })
                    snackbar.setActionTextColor(resources.getColor(R.color.red, Resources.getSystem().newTheme()))
                    snackbar.setBackgroundTint(resources.getColor(R.color.green, Resources.getSystem().newTheme()))
                    snackbar.show()
                    fav.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }

            override fun onFailure(call: Call<AllFavourites>, t: Throwable) {
                //Toast.makeText(activity, "Not Added", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun removeFromFavourites(all: ArrayList<FavouritesModel>) {
        var favRequest: RequestQueue = Volley.newRequestQueue(activity)
        var obj: JSONObject = JSONObject()
        try {
            obj.put("user_id", all[0].user_id)
            obj.put("product_id", all[0].product_id)
        }
        catch (e: JSONException){

        }

        var favJsonObject: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, constants.BASE_URL, obj,
            {

            },
            {

            })

        favRequest.add(favJsonObject)
    }

    private fun sendToCart() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1")
        var productContent = arguments?.getParcelable<Product>("PRODUCT")

        cartInterface = CartRetrofit.getRetrofit().create(CartInterface::class.java)
        var call: Call<AllCart> = cartInterface.addToCart(productContent!!.productId!!, productContent.farmerId!!, userId!!, quantity.text.toString().toInt())
        call.enqueue(object : Callback<AllCart> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(call: Call<AllCart>, response: Response<AllCart>) {
                if (response.isSuccessful){
                    var snackbar: Snackbar = Snackbar.make(scroll, "Added To Cart", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO", View.OnClickListener {
                        val snackbar1: Snackbar = Snackbar.make(scroll, "REMOVED", Snackbar.LENGTH_LONG)
                        snackbar1.setBackgroundTint(resources.getColor(R.color.green, Resources.getSystem().newTheme()))
                        removeFromCart(response.body()!!.data)
                        snackbar1.show()
                    })
                    snackbar.setActionTextColor(resources.getColor(R.color.red, Resources.getSystem().newTheme()))
                    snackbar.setBackgroundTint(resources.getColor(R.color.green, Resources.getSystem().newTheme()))
                    snackbar.show()
                    //Toast.makeText(activity, "Added", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllCart>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun removeFromCart(data: ArrayList<Cart>) {
        requestQueue = Volley.newRequestQueue(activity)

        jsonObjectRequest = JsonObjectRequest(Request.Method.GET, constants.BASE_URL + "customer/products/${data[0].userId}/shoppingcart/${data[0].cartId}/delete", null,
            {
        },
            {

            })

        requestQueue!!.add(jsonObjectRequest)
    }


    private fun setDetails() {
        val product = arguments?.getParcelable<Product>("PRODUCT")
        var total: Int = 1
        add.setOnClickListener {
            total += 1
            quantity.text = total.toString()
            price.text = (product!!.price!!.toInt() * total).toString()
        }

        subtract.setOnClickListener {
            if (total == 1){
                total = 1
                quantity.text = total.toString()
                price.text = (product!!.price!!.toInt() * total).toString()
            }
            else{
                total -= 1
                quantity.text = total.toString()
                price.text = (product!!.price!!.toInt() * total).toString()
            }
        }


        if (product != null){
            var img = product.img.toString()
            var title = product.name.toString()
            var price = product.price.toString()
            var description = product.desc.toString()
            val productPrice = price.toInt() * quantity.text.toString().toInt()

            this.title.text = title
            this.price.text = (product!!.price!!.toInt() * total).toString()
            this.description.text = description

            val activity = activity as Context
            val picasso: Picasso.Builder = Picasso.Builder(activity)
            picasso.downloader(OkHttp3Downloader(activity))
            picasso.build().load(constants.BASE_URL + img).into(this.img)
        }
    }

    private fun setRecommended() {
        val product = arguments?.getParcelable<Product>("PRODUCT")
        val type = product!!.type.toString()
        val activity = activity as Context
        gridLayoutManager = GridLayoutManager(activity, 2)
        productAdapter = ProductAdapter(activity)
        recommendedDetailsInterface = RecommendedDetailsRetrofit.getRetrofit().create(RecommendedDetailsInterface::class.java)
        val call: Call<AllProducts> = recommendedDetailsInterface.getProductByType(type)
        call.enqueue(object : Callback<AllProducts>{
            override fun onResponse(call: Call<AllProducts>, response: Response<AllProducts>) {
                if (response.isSuccessful){
                    getRecommended(response.body()!!.all)
                }
            }

            override fun onFailure(call: Call<AllProducts>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getRecommended(all: ArrayList<Product>) {
        productAdapter.getData(all)
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = gridLayoutManager

    }
}