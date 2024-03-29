package com.feiradagua.feiradagua.model.`class`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Cart (
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var totalPrice: Double = 0.0,
    var producerId: String = "",
    var weight: String = "",
    var photo: String = ""
): Parcelable