package com.example.emkulimaapp.farmer.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class farmer(
    @SerializedName("farmer_id")
    @Expose
    var farmerId: Int? = null,
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null,
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: String? = null,
    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,
    @SerializedName("id_number")
    @Expose
    var idNumber: Int? = null,
    @SerializedName("profile_pic")
    @Expose
    var profilePicture: String? = null,
    @SerializedName("bio")
    @Expose
    var bio: String? = null,
    @SerializedName("location")
    @Expose
    var location: String? = null
) {

}