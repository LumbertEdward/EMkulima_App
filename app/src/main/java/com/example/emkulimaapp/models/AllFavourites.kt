package com.example.emkulimaapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Array

class AllFavourites(
    @SerializedName("data")
    @Expose
    var all: ArrayList<FavouritesModel> = ArrayList()
) {
}