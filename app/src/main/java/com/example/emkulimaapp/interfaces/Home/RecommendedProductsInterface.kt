package com.example.emkulimaapp.interfaces.Home

import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET

interface RecommendedProductsInterface {
    @GET("customer/products")
    fun getProducts(): Call<AllProducts>
}