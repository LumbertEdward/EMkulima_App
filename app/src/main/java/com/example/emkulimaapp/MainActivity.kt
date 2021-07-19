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
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.authentication.Login
import com.example.emkulimaapp.fragments.*
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.FragmentClass
import com.example.emkulimaapp.models.Product
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), GeneralInterface {
    @BindView(R.id.bottom)
    lateinit var bot: BottomNavigationView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var navController: NavController

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
                    navController.navigate(R.id.homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cart -> {
                    navController.navigate(R.id.cartFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourites -> {
                    navController.navigate(R.id.favouritesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun logOut() {
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
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
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

        navController.navigate(R.id.productDetailsFragment, bundle)
    }

    override fun getTypeSelected(type: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PRODUCTTYPE", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("TYPE", type)
        editor.apply()

        navController.navigate(R.id.action_homeFragment_to_productTypefragment)
    }

    override fun goToCheckout(lst: ArrayList<Cart>) {
        var bundle: Bundle = Bundle()
        bundle.putParcelableArrayList("CART", lst)
        navController.navigate(R.id.action_cartFragment_to_checkOutFragment, bundle)
    }

    override fun goToOrders() {
        navController.navigate(R.id.action_checkOutFragment_to_ordersFragment)
    }
}