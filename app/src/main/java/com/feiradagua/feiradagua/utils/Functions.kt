package com.feiradagua.feiradagua.utils

import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Products
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
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
}

fun MutableList<Products>.deleteProduct(id: String) {
    this.forEach {products ->
        if(products.id == id) {
            this.remove(products)
        }
    }
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