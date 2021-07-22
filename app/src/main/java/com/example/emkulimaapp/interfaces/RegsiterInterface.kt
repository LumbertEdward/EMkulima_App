package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCustomer
import com.example.emkulimaapp.models.Customer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegsiterInterface {
    @POST("customer/register")
    @FormUrlEncoded
    fun registerUser(
        @Field("userId") userId: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("phone_number") phoneNumber: String,
        @Field("location") location: String,
        @Field("password") password: String
    ): Call<AllCustomer>
}