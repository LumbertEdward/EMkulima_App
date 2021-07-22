package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllCustomer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface UpdateProfileInterface {
    @POST("customer/{user_id}/profile/update")
    @FormUrlEncoded
    fun updateUser(
        @Path("user_id") user_id: String,
        @Field("first_name") firstname: String,
        @Field("last_name") lastname: String,
        @Field("phone_number") phonenumber: String,
        @Field("location") location: String,
    ): Call<AllCustomer>
}