package com.feiradagua.feiradagua.utils

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