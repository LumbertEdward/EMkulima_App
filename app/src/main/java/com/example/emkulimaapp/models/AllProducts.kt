package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllProducts(
    @SerializedName("data")
    @Expose
    var all: ArrayList<Product> = ArrayList()
) {
}