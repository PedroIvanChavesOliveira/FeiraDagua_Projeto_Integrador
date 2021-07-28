package com.feiradagua.feiradagua.utils

import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.model.`class`.*
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.InputStream
import java.util.*

fun generateRandomUUID(): String {
    return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT)
}

fun MutableList<String>.removeItem(element: String) {
    if(this.contains(element)) {
        this.remove(element)
    }
}

fun MutableList<String>.addItem(element: String) {
    if(!this.contains(element)) {
        this.add(element)
    }
}

fun Chip.checkByTag(tag: String) {
    if(this.tag == tag) {
        this.isChecked = true
    }
    this.isEnabled = false
}

fun Chip.checkByTagUpdate(tag: String, array: MutableList<String>) {
    if(this.tag == tag) {
        this.isChecked = true
        array.addItem(tag)
    }
}

fun MutableList<Products>.checkingIfExist(id: String): Boolean {
    this.forEach {
        if(id == it.id) {
            return true
        }
    }
    return false
}

fun MutableList<Products>.updateProduct(product: Products) {
    this.forEachIndexed { index, products ->
        if(products.id == product.id) {
            this[index] = product
        }
    }
}

fun Producer.updateProducer(producer: Producer) {
    this.uid = producer.uid
    this.description = producer.description
    this.adress = producer.adress
    this.payment = producer.payment
    this.deliveryDate = producer.deliveryDate
    this.deliveryLocation = producer.deliveryLocation
    this.name = producer.name
    this.photo = producer.photo
    this.phone = producer.phone
    this.email = producer.email
    this.type = producer.type
    this.token = producer.token
}

fun User.updateUser(user: User) {
    this.uid = user.uid
    this.name = user.name
    this.photo = user.photo
    this.adress = user.adress
    this.email = user.email
    this.phone = user.phone
    this.token = user.token
    this.type = user.type
}

fun MutableList<Cart>.updateCartList(item: Cart) {
    this.forEachIndexed { index, cart ->
        if(item.id == cart.id) {
            cart.totalPrice = item.totalPrice
        }
    }
}

fun MutableList<Cart>.deleteItemFromCartList(item: Cart) {
    this.removeAll {it.id == item.id}
}

fun MutableList<Cart>.getTotalPrice(): String {
    var totalValue = 0.0
    this.forEach { item ->
        totalValue += item.totalPrice
    }
    return "R$ $totalValue"
}

fun MutableList<Cart>.getTotalPriceValue(): Double {
    var totalValue = 0.0
    this.forEach { item ->
        totalValue += item.totalPrice
    }
    return totalValue
}

fun MutableList<Cart>.getProducersIdsList(): MutableList<String> {
    val list = mutableListOf<String>()
    this.forEach {
        if(!list.contains(it.producerId)) {
            list.add(it.producerId)
        }
    }
    return list
}

fun MutableList<Producer>.getProducersToken(producersIds: MutableList<String>): MutableList<String?> {
    val list = mutableListOf<String?>()
    this.forEach {
        if(producersIds.contains(it.uid)) {
            list.add(it.token)
        }
    }
    return list
}

fun MutableList<Producer>.getProducer(id: String?): Producer? {
    this.forEach {
        if(it.uid == id) {
            return it
        }
    }
    return null
}

fun MutableList<Cart>.getProductsInfosList(): MutableList<ProductOrder> {
    val list = mutableListOf<ProductOrder>()
    this.forEach {
        val productOrder = ProductOrder(it.id, it.totalPrice)
        list.add(productOrder)
    }
    return list
}

fun MutableList<Products>.deleteProduct(id: String) {
    this.removeAll { it.id == id }
}

fun TextInputEditText.splitAdress(text: String) {
    val splited = text.split(",","-")
    this.filterEditTextByTag(splited)
}

private fun TextInputEditText.filterEditTextByTag(splited: List<String>) {
    if(splited.size == 7) {
        when(this.tag) {
            "Informe o seu endereço" -> {this.setText(splited[0])}
            "Informe o seu bairro" -> {this.setText(splited[3])}
            "Informe seu número" -> {this.setText(splited[1])}
            "Informe seu complemento" -> {this.setText(splited[2])}
            "Informe a sua cidade" -> {this.setText(splited[4])}
            "Informe a UF do seu Estado" -> {this.setText(splited[5])}
            "Informe o seu CEP" -> {this.setText(splited[6])}
        }
    }else {
        when(this.tag) {
            "Informe o seu endereço" -> {this.setText(splited[0])}
            "Informe o seu bairro" -> {this.setText(splited[2])}
            "Informe seu número" -> {this.setText(splited[1])}
            "Informe a sua cidade" -> {this.setText(splited[3])}
            "Informe a UF do seu Estado" -> {this.setText(splited[4])}
            "Informe o seu CEP" -> {this.setText(splited[5])}
        }
    }
}