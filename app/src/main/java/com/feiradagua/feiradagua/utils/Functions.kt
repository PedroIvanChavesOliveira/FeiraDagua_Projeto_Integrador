package com.feiradagua.feiradagua.utils

import com.feiradagua.feiradagua.model.`class`.Products
import com.google.android.material.chip.Chip
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

fun MutableList<Products>.deleteProduct(id: String) {
    this.forEach {products ->
        if(products.id == id) {
            this.remove(products)
        }
    }
}