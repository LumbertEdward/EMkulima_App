package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllOrderItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ViewOrderProductsInterface {
    @GET("customer/products/{order_id}/orders/vieworderproducts")
    fun getProducts(
        @Path("order_id") order_id: String
    ): Call<AllOrderItems>
}