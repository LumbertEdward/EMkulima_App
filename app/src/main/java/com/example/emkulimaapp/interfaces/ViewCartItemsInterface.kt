package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCart
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViewCartItemsInterface {
    @GET("customer/products/{user_id}/shoppingcart")
    fun viewItems(
        @Path("user_id") userId: String
    ): Call<AllCart>
}