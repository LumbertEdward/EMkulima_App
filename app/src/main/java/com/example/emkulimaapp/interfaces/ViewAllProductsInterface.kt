package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET

interface ViewAllProductsInterface {
    @GET("customer/products")
    fun getProducts(): Call<AllProducts>
}