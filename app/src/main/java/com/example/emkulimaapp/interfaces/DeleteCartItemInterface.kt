package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.message
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeleteCartItemInterface {
    @GET("customer/products/{user_id}/shoppingcart/{cart_item_id}/delete")
    fun deleteItem(
        @Path("user_id") userId: String,
        @Path("cart_item_id") cartItemId: Int
    ): Call<message>
}