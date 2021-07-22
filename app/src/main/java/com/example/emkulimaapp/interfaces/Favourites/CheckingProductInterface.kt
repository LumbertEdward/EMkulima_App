package com.example.emkulimaapp.interfaces.Favourites

import com.example.emkulimaapp.models.AllFavourites
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CheckingProductInterface {
    @GET("customer/favourites/products/check")
    fun checking(
        @Query("user_id") user_id: String,
        @Query("product_id") product_id: Int
    ): Call<AllFavourites>
}