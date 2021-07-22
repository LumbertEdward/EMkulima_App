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
    var userId: String? = null,
    @SerializedName("total_items")
    @Expose
    var total_items: Int? = null,
    @SerializedName("farmer_id")
    @Expose
    var farmerId: Int? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(cartId)
        parcel.writeValue(productId)
        parcel.writeString(userId)
        parcel.writeValue(total_items)
        parcel.writeValue(farmerId)
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