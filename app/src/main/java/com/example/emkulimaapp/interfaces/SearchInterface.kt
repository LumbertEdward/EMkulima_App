package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET

interface SearchInterface {
    @GET("customer/products")
    fun getProducts(): Call<AllProducts>
}