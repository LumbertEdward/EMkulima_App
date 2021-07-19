package com.example.emkulimaapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cart(
    @SerializedName("cart_id")
    @Expose
    var cartId: Int? = null,
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null,
    @SerializedName("userId")
    @Expose
    var userId: Int? = null,
    @SerializedName("farmer_id")
    @Expose
    var farmerId: Int? = null,
    @SerializedName("product_name")
    @Expose
    var name: String? = null,
    @SerializedName("product_price")
    @Expose
    var price: Int? = null,
    @SerializedName("product_image")
    @Expose
    var img: String? = null,
    @SerializedName("product_description")
    @Expose
    var desc: String? = null,
    @SerializedName("product_delivery_time")
    @Expose
    var time: String? = null,
    @SerializedName("product_calcs")
    @Expose
    var calcs: Int? = null,
    @SerializedName("product_type")
    @Expose
    var type: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(cartId)
        parcel.writeValue(productId)
        parcel.writeValue(userId)
        parcel.writeValue(farmerId)
        parcel.writeString(name)
        parcel.writeValue(price)
        parcel.writeString(img)
        parcel.writeString(desc)
        parcel.writeString(time)
        parcel.writeValue(calcs)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }

}