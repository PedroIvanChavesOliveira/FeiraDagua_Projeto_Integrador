package com.feiradagua.feiradagua.model.`class`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Products (
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var producerId: String = "",
    var photo: String = ""
): Parcelable