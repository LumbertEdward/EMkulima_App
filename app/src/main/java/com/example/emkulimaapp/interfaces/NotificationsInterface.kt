package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllNotifications
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationsInterface {
    @GET("customer/notifications/{user_id}")
    fun getNotification(
        @Path("user_id") userId: String
    ): Call<AllNotifications>
}