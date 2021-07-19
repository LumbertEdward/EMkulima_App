package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrders
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PendingOrdersInterface {
    @GET("customer/products/{user_id}/pendingorders")
    fun getPending(
        @Query("user_id") userId: Int
    ): Call<AllOrders>
}