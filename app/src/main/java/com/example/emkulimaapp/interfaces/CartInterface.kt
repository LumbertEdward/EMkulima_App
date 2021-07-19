package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.message
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface CartInterface {
    @POST("customer/products/{product_id}/{farmer_id}/{userId}/shoppingcart/add/")
    @FormUrlEncoded
    fun addToCart(
        @Path("product_id") productId: Int,
        @Path("farmer_id") farmerId: Int,
        @Path("userId") userId: Int,
        @Field("product_name") product_name: String,
        @Field("product_description") product_description: String,
        @Field("product_price") product_price: Int,
        @Field("product_image") product_image: String,
        @Field("product_type") product_type: String,
        @Field("product_calcs") product_calcs: Int,
        @Field("product_delivery_time") product_delivery_time: String
    ): Call<message>
}