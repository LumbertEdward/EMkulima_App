package com.example.emkulimaapp.interfaces

import android.widget.ImageView
import android.widget.TextView
import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.Product

interface GeneralInterface {
    fun getAllProducts()
    fun passDetails(product: Product)
    fun getTypeSelected(type: String)
    fun goToCheckout(lst: ArrayList<Product>)
    fun goToOrders()
    fun addToFavourites(product_id: Int, view: ImageView)
    fun removeFromFavourites(product_id: Int)
    fun logOut()
    fun selectedOrder(orderId: String)
    fun addToCart(product: Product)
}