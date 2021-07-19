package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrderItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderProductsInterface {
    @GET("customer/products/{order_id}/orders/vieworderproducts")
    fun getOrderItems(
        @Path("order_id") orderId: String
    ): Call<AllOrderItems>
}