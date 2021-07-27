package com.feiradagua.feiradagua.model.`class`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Order (
    val id: String = "",
    val products: MutableList<ProductOrder> = mutableListOf(),
    val username: String = "",
    val userPhoto: String = "",
    val totalPrice: Double = 0.0,
    val deliveryDate: String = "",
    val confirmation: Int = 0,
    val producerId: MutableList<String> = mutableListOf(),
    val userId: String = ""
): Parcelable