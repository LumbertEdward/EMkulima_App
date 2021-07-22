package com.example.emkulimaapp.interfaces.Favourites

import com.example.emkulimaapp.models.AllFavourites
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProductFavouritesInterface {
    @POST("customer/favourites")
    @FormUrlEncoded
    fun checkOut(
        @Field("user_id") user_id: String,
        @Field("product_id") productId: Int
    ): Call<AllFavourites>
}