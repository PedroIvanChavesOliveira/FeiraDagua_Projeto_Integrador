package com.feiradagua.feiradagua.model.`class`

class Producer (
    var name: String? = "",
    val email: String? = "",
    var phone: String? = "",
    var photo: String? = "",
    var type: Int? = 0,
    var adress: String = "",
    var description: String = "",
    var deliveryDate: String = "",
    var deliveryLocation: String = "",
    var payment: String = "",
    var products: MutableList<Products> = mutableListOf()
)