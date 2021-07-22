package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllNotifications {
    @SerializedName("data")
    @Expose
    var data: ArrayList<Notification> = ArrayList()
}