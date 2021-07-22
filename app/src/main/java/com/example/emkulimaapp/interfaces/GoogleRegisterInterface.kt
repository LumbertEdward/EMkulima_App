package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCustomer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleRegisterInterface {
    @POST("customer/googleRegister")
    @FormUrlEncoded
    fun sendUser(
        @Field("userId") userId: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("photo") photo: String
    ): Call<AllCustomer>
}