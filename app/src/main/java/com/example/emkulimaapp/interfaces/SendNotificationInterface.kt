package com.example.emkulimaapp.interfaces

import com.example.emkulimaapp.models.AllNotifications
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SendNotificationInterface {
    @POST("customer/notifications")
    @FormUrlEncoded
    fun sendNotification(
        @Field("user_id") user_id: String,
        @Field("notification") notification: String,
        @Field("notification_date") notification_date: String
    ): Call<AllNotifications>
}