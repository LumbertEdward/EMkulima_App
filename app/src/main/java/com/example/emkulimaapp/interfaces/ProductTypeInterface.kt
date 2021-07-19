package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductTypeInterface {
    @GET("customer/products/type")
    fun getProductByType(
        @Query("type") type: String
    ): Call<AllProducts>
}