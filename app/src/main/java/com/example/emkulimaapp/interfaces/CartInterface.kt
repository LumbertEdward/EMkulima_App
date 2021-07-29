package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCart
import com.example.emkulimaapp.models.message
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface CartInterface {
    @GET("customer/products/{product_id}/{farmer_id}/{userId}/{total_items}/shoppingcart/add/")
    fun addToCart(
        @Path("product_id") productId: Int,
        @Path("farmer_id") farmerId: Int,
        @Path("userId") userId: String,
        @Path("total_items") total_items: Int
    ): Call<AllCart>
}