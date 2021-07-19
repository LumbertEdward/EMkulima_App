package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrders
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CompleteOrdersInterface {
    @GET("customer/products/{user_id}/completeorders")
    fun getCompleted(
        @Path("user_id") userId: Int
    ): Call<AllOrders>
}