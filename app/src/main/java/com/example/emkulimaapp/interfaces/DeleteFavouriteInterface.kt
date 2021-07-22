package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllFavourites
import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeleteFavouriteInterface {
    @GET("customer/favourites/products/delete")
    fun getFavourites(
        @Query("user_id") user_id: String,
        @Query("product_id") product_id: Int
    ): Call<AllFavourites>
}