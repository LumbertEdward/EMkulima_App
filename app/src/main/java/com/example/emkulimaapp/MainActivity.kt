package com.example.emkulimaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.RetrofitClasses.*
import com.example.emkulimaapp.authentication.Login
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.fragments.*
import com.example.emkulimaapp.interfaces.DeleteFavouriteInterface
import com.example.emkulimaapp.interfaces.Favourites.CheckingProductInterface
import com.example.emkulimaapp.interfaces.Favourites.ProductFavouritesInterface
import com.example.emkulimaapp.interfaces.FavouritesCheckingProductsInterface
import com.example.emkulimaapp.interfaces.FavouritesInterface
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Method

class MainActivity : AppCompatActivity(), GeneralInterface {
    @BindView(R.id.bottom)
    lateinit var bot: BottomNavigationView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var productFavouritesInterface: ProductFavouritesInterface
    private lateinit var checkingProductInterface: CheckingProductInterface
    private lateinit var deleteFavouriteInterface: DeleteFavouriteInterface

    private lateinit var navController: NavController
    private var totItem: Int = 1
    val navOptions: NavOptions.Builder = NavOptions.Builder()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        navController = findNavController(R.id.navHost)

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.statusBarColor = resources.getColor(R.color.white, Resources.getSystem().newTheme())
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        navOptions.setEnterAnim(android.R.anim.slide_in_left).setExitAnim(android.R.anim.slide_out_right).setPopEnterAnim(android.R.anim.slide_in_left).setPopExitAnim(android.R.anim.slide_out_right)

        initFragment()
        setBottomView()
    }

    private fun initFragment() {
        navController.navigate(R.id.homeFragment)
    }

    private fun setBottomView() {
        bot.setupWithNavController(navController)
        this.bot.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    navController.navigate(R.id.homeFragment, null, navOptions.build())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cart -> {
                    navController.navigate(R.id.cartFragment, null, navOptions.build())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourites -> {
                    navController.navigate(R.id.favouritesFragment, null, navOptions.build())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment, null, navOptions.build())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.orders -> {
                    navController.navigate(R.id.ordersFragment, null, navOptions.build())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }


    private fun showBottom(){
        this.bot.visibility = View.VISIBLE
    }

    private fun hideBottom(){
        this.bot.visibility = View.GONE
    }

    override fun getAllProducts() {
        navController.navigate(R.id.action_homeFragment_to_productsFragment)
    }

    override fun passDetails(product: Product) {
        var bundle: Bundle = Bundle()
        bundle.putParcelable("PRODUCT", product)
        navController.navigate(R.id.productDetailsFragment, bundle, navOptions.build(), null)
    }

    override fun getTypeSelected(type: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PRODUCTTYPE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("TYPE", type)
        editor.apply()

        navController.navigate(R.id.action_homeFragment_to_productTypefragment)
    }

    override fun goToCheckout(lst: ArrayList<Product>) {
        var bundle: Bundle = Bundle()
        bundle.putParcelableArrayList("CART", lst)
        navController.navigate(R.id.action_cartFragment_to_checkOutFragment, bundle)
    }

    override fun goToOrders() {
        navController.navigate(R.id.action_checkOutFragment_to_ordersFragment)
    }

    override fun addToFavourites(product_id: Int, view: ImageView) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        checkingProductInterface = CheckingProductsRetrofit.getRetrofit().create(CheckingProductInterface::class.java)
        val call: Call<AllFavourites> = checkingProductInterface.checking(userId, product_id)
        call.enqueue(object : Callback<AllFavourites>{
            override fun onResponse(call: Call<AllFavourites>, response: Response<AllFavourites>) {
                if (response.isSuccessful){
                    if (response.body()!!.all.size < 1){
                        AddFavourites(product_id, view)
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Already added", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AllFavourites>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Product Already Added", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun AddFavourites(productId: Int, view: ImageView) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        productFavouritesInterface = ProductFavouritesRetrofit.getRetrofit().create(ProductFavouritesInterface::class.java)
        var call: Call<AllFavourites> = productFavouritesInterface.checkOut(userId, productId)
        call.enqueue(object : Callback<AllFavourites>{
            override fun onResponse(call: Call<AllFavourites>, response: Response<AllFavourites>) {
                if (response.isSuccessful){
                    view.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }

            override fun onFailure(call: Call<AllFavourites>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Not Added", Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun removeFromFavourites(product_id: Int) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        deleteFavouriteInterface = DeleteFavouriteRetrofit.getRetrofit().create(DeleteFavouriteInterface::class.java)
        val call: Call<AllFavourites> = deleteFavouriteInterface.getFavourites(userId, product_id)
        call.enqueue(object : Callback<AllFavourites>{
            override fun onResponse(call: Call<AllFavourites>, response: Response<AllFavourites>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MainActivity, "Removed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllFavourites>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun logOut() {
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInClient.signOut().addOnCompleteListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("LOGGEDIN", false)
            editor.apply()

            val sharedPreferencesUserDetails: SharedPreferences = getSharedPreferences("USERDETAILS", MODE_PRIVATE)
            val editorUser: SharedPreferences.Editor = sharedPreferencesUserDetails.edit()
            editorUser.clear()
            editorUser.apply()

            val notificationSharedPreferences: SharedPreferences = getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)!!
            val editorNot: SharedPreferences.Editor = notificationSharedPreferences.edit()
            editorNot.clear()
            editorNot.apply()

            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }
    }

    override fun selectedOrder(orderId: String) {
        val bundle: Bundle = Bundle()
        bundle.putString("ORDERID", orderId)
        navController.navigate(R.id.viewSelectedOrdersFragment, bundle)
    }

    override fun addToCart(product: Product) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        var cartRequest: RequestQueue = Volley.newRequestQueue(this)

        var jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, constants.BASE_URL + "customer/products/${product.productId}/${product.farmerId}/${userId}/${1}/shoppingcart/add/", null,
            {

            },
            {

            })

        cartRequest.add(jsonObjectRequest)
    }


}