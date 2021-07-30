package com.example.emkulimaapp.farmer.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllFarmers(
    @SerializedName("data")
    @Expose
    var data: ArrayList<farmer> = ArrayList()
) {
}