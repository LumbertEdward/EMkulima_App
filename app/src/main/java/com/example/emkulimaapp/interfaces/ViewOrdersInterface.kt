package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrders
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViewOrdersInterface {
    @GET("customer/products/{user_id}/orders")
    fun getOrders(
        @Path("user_id") userId: String
    ): Call<AllOrders>
}