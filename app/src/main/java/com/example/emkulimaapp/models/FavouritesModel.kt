package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavouritesModel(
    @SerializedName("fav_id")
    @Expose
    var fav_id: Int? = null,
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null,
    @SerializedName("product_id")
    @Expose
    var product_id: Int? = null
) {
}