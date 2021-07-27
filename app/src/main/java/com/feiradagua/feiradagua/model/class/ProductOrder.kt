package com.feiradagua.feiradagua.model.`class`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductOrder (
    val id: String = "",
    val totalPrice: Double = 0.0
): Parcelable