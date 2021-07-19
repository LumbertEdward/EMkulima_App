package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.Product

interface GeneralInterface {
    fun getAllProducts()
    fun passDetails(product: Product)
    fun getTypeSelected(type: String)
    fun goToCheckout(lst: ArrayList<Cart>)
    fun goToOrders()
    fun onBackPressed()
}