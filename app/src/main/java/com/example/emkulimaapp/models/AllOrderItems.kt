package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllOrderItems(
    @SerializedName("message")
    @Expose
    var message: String? = null,
    @SerializedName("data")
    @Expose
    var data: ArrayList<OrderItems> = ArrayList()
) {
}