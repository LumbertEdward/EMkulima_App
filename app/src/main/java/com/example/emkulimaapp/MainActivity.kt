package com.example.emkulimaapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.authentication.Login
import com.example.emkulimaapp.fragments.*
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.models.FragmentClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), GeneralInterface {
    @BindView(R.id.navigation)
    lateinit var nav: NavigationView
    @BindView(R.id.bottom)
    lateinit var bot: BottomNavigationView
    @BindView(R.id.relTool)
    lateinit var tool: Toolbar
    @BindView(R.id.drawer)
    lateinit var drawer: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences

    //fragments
    private var homeFragment: HomeFragment? = null
    private var favouritesFragment: FavouritesFragment? = null
    private var productsFragment: ProductsFragment? = null
    private var searchFragment: SearchFragment? = null
    private var cartFragment: CartFragment? = null

    var tags: ArrayList<String> = ArrayList()
    var fragments: ArrayList<FragmentClass> = ArrayList()
    var mCount: Int = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE)

        coordinatorLayout = findViewById(R.id.coordinator)
        coordinatorLayout.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.statusBarColor = resources.getColor(R.color.white, Resources.getSystem().newTheme())
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        initFragment()
        setNavigation()
        setBottomView()
        setDrawer()
    }

    private fun initFragment() {
        if (homeFragment == null){
            homeFragment = HomeFragment()
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frame, homeFragment!!, getString(R.string.HOME))
            fragmentTransaction.commit()
            tags.add(getString(R.string.HOME))
            fragments.add(FragmentClass(getString(R.string.HOME), homeFragment))
        }
        else{
            tags.remove(getString(R.string.HOME))
            tags.add(getString(R.string.HOME))
        }
        setVisibility(getString(R.string.HOME))
    }

    private fun setDrawer() {
        setSupportActionBar(tool)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawer, tool, R.string.open, R.string.close
        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

    }

    private fun setBottomView() {
        this.bot.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    if (homeFragment == null){
                        homeFragment = HomeFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, homeFragment!!, getString(R.string.HOME))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.HOME))
                        fragments.add(FragmentClass(getString(R.string.HOME), homeFragment))
                    }
                    else{
                        tags.remove(getString(R.string.HOME))
                        tags.add(getString(R.string.HOME))
                    }
                    setVisibility(getString(R.string.HOME))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cart -> {
                    if (cartFragment == null){
                        cartFragment = CartFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, cartFragment!!, getString(R.string.CART))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.CART))
                        fragments.add(FragmentClass(getString(R.string.CART), cartFragment))
                    }
                    else{
                        tags.remove(getString(R.string.CART))
                        tags.add(getString(R.string.CART))
                    }
                    setVisibility(getString(R.string.CART))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourites -> {
                    if (favouritesFragment == null){
                        favouritesFragment = FavouritesFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, favouritesFragment!!, getString(R.string.FAVOURITES))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.FAVOURITES))
                        fragments.add(FragmentClass(getString(R.string.FAVOURITES), favouritesFragment))
                    }
                    else{
                        tags.remove(getString(R.string.FAVOURITES))
                        tags.add(getString(R.string.FAVOURITES))
                    }
                    setVisibility(getString(R.string.FAVOURITES))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    if (homeFragment == null){
                        homeFragment = HomeFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, homeFragment!!, getString(R.string.HOME))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.HOME))
                        fragments.add(FragmentClass(getString(R.string.HOME), homeFragment))
                    }
                    else{
                        tags.remove(getString(R.string.HOME))
                        tags.add(getString(R.string.HOME))
                    }
                    setVisibility(getString(R.string.HOME))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun setNavigation() {
        this.nav.setNavigationItemSelectedListener {
            drawer.closeDrawers()
            when(it.itemId){
                R.id.navHome -> {
                    if (homeFragment == null){
                        homeFragment = HomeFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, homeFragment!!, getString(R.string.HOME))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.HOME))
                        fragments.add(FragmentClass(getString(R.string.HOME), homeFragment))
                    }
                    else{
                        tags.remove(getString(R.string.HOME))
                        tags.add(getString(R.string.HOME))
                    }
                    setVisibility(getString(R.string.HOME))
                    return@setNavigationItemSelectedListener true
                }
                R.id.navCart -> {
                    if (cartFragment == null){
                        cartFragment = CartFragment()
                        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frame, cartFragment!!, getString(R.string.CART))
                        fragmentTransaction.commit()
                        tags.add(getString(R.string.CART))
                        fragments.add(FragmentClass(getString(R.string.CART), cartFragment))
                    }
                    else{
                        tags.remove(getString(R.string.CART))
                        tags.add(getString(R.string.CART))
                    }
                    setVisibility(getString(R.string.CART))
                    return@setNavigationItemSelectedListener true
                }
                R.id.navProfile -> {
                    logOut()
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    return@setNavigationItemSelectedListener false
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
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun setVisibility(tag: String){

        if (tag == getString(R.string.HOME)){
            coordinatorLayout.visibility = View.VISIBLE
        }
        if (tag == getString(R.string.CART)){
            coordinatorLayout.visibility = View.GONE
        }
        if (tag == getString(R.string.SEARCH)){
            coordinatorLayout.visibility = View.GONE
        }
        if (tag == getString(R.string.PRODUCTS)){
            coordinatorLayout.visibility = View.GONE
        }


        for (i in tags.indices){
            if (tag == fragments[i].name){
                val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.show(fragments[i].fragment!!)
                fragmentTransaction.commit()
            }
            else{
                val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(fragments[i].fragment!!)
                fragmentTransaction.commit()
            }
        }
    }

    override fun onBackPressed() {
        val total = tags.size
        if (total > 1){
            val top: String = tags[total - 1]
            val bottom: String = tags[total - 2]
            setVisibility(bottom)
            tags.remove(top)
            mCount = 1
        }
        else if (total == 1){
            val top = tags[total - 1]
            if (top == getString(R.string.HOME)){
                Toast.makeText(this, "End", Toast.LENGTH_LONG).show()
                mCount++
            }
            else{
                mCount++
            }
        }
        if (mCount >= 2){
            super.onBackPressed()
        }
    }

    override fun getAllProducts() {
        if (productsFragment != null){
            supportFragmentManager.beginTransaction().remove(productsFragment!!).commitAllowingStateLoss()
        }
        else{
            productsFragment = ProductsFragment()
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frame, productsFragment!!, getString(R.string.PRODUCTS)).commit()
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            tags.add(getString(R.string.PRODUCTS))
            fragments.add(FragmentClass(getString(R.string.PRODUCTS), productsFragment))
            setVisibility(getString(R.string.PRODUCTS))
        }
    }
}