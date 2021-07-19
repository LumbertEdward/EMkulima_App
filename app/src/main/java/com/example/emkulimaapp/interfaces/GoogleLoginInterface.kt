package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCustomer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleLoginInterface {
    @POST("customer/googleLogin")
    @FormUrlEncoded
    fun getUser(
        @Field("email") email: String
    ): Call<AllCustomer>
}