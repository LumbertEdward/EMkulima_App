package com.example.emkulimaapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Orders(
    @SerializedName("order_id")
    @Expose
    var orderId: String? = null,
    @SerializedName("userId")
    @Expose
    var userId: Int? = null,
    @SerializedName("order_price")
    @Expose
    var orderPrice: Int? = null,
    @SerializedName("order_date")
    @Expose
    var orderDate: String? = null,
    @SerializedName("delivery_date")
    @Expose
    var deliveryDate: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeValue(userId)
        parcel.writeValue(orderPrice)
        parcel.writeString(orderDate)
        parcel.writeString(deliveryDate)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Orders> {
        override fun createFromParcel(parcel: Parcel): Orders {
            return Orders(parcel)
        }

        override fun newArray(size: Int): Array<Orders?> {
            return arrayOfNulls(size)
        }
    }

}