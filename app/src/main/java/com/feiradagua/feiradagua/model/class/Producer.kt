package com.feiradagua.feiradagua.model.`class`

class Producer (
    var name: String? = "",
    val email: String? = "",
    var phone: String? = "",
    var photo: String? = "",
    var type: Int? = 0,
    var adress: String = "",
    var description: String = "",
    var deliveryDate: MutableList<String> = mutableListOf(),
    var deliveryLocation: MutableList<String> = mutableListOf(),
    var payment: MutableList<String> = mutableListOf(),
    var products: MutableList<Products>? = mutableListOf()
)