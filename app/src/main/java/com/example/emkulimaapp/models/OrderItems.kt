package com.example.emkulimaapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderItems(
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null,
    @SerializedName("order_id")
    @Expose
    var orderId: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("farmer_id")
    @Expose
    var farmerId: Int? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(productId)
        parcel.writeString(orderId)
        parcel.writeString(status)
        parcel.writeValue(farmerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItems> {
        override fun createFromParcel(parcel: Parcel): OrderItems {
            return OrderItems(parcel)
        }

        override fun newArray(size: Int): Array<OrderItems?> {
            return arrayOfNulls(size)
        }
    }

}