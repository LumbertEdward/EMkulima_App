package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrders
import retrofit2.Call
import retrofit2.http.*

interface OrderInterface {
    @FormUrlEncoded
    @POST("customer/products/order/{user_id}/")
    fun orderProduct(
        @Field("order_id") orderId: String,
        @Field("order_price") orderPrice: Int,
        @Field("delivery_date") deliveryDate: String,
        @Path("user_id") userId: Int
    ): Call<AllOrders>
}