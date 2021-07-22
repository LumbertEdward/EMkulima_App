package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Notification(
    @SerializedName("notification_id")
    @Expose
    var notificationId: Int? = null,
    @SerializedName("notification")
    @Expose
    var text: String? = null,
    @SerializedName("notification_date")
    @Expose
    var date: String? = null
) {
}