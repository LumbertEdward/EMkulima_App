package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrderItems
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheckOutInterface {
    @POST("customer/products/checkout")
    @FormUrlEncoded
    fun checkOut(
        @Field("product_id") productId: Int,
        @Field("farmer_id") farmerId: Int,
        @Field("order_Id") orderId: String,
        @Field("status") status: String
    ): Call<AllOrderItems>
}