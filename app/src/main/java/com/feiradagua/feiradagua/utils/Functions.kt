package com.feiradagua.feiradagua.utils

import com.google.android.material.chip.Chip

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