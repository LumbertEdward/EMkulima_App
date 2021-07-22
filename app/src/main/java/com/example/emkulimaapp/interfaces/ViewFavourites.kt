package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllFavourites
import com.example.emkulimaapp.models.AllProducts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ViewFavourites {
    @GET("customer/favourites/products")
    fun getFavourites(
        @Query("user_id") user_id: String,
    ): Call<AllFavourites>
}