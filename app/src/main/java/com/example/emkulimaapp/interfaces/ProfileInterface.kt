package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCustomer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileInterface {
    @GET("customer/{user_id}/profile")
    fun getUserDetails(
        @Path("user_id") user_id: String
    ): Call<AllCustomer>
}