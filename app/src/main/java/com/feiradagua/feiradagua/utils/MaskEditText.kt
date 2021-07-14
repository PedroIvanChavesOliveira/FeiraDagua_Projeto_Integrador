package com.feiradagua.feiradagua.model.`class`

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object Mask {
    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
            .replace("[)]".toRegex(), "").replace(" ".toRegex(), "")
            .replace(",".toRegex(), "")
    }

    fun isASign(c: Char): Boolean {
        return c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' '
    }

    fun mask(mask: String, text: String): String {
        var i = 0
        var mascara = ""
        for (m in mask.toCharArray()) {
            if (m != '#') {
                mascara += m
                continue
            }
            mascara += try {
                text[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        return mascara
    }

    fun insert (mask: String, ediTxt: EditText): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
                var index = 0
                for (element in mask) {
                    val m = element
                    if (m != '#') {
                        if (index == str.length && str.length < old.length) {
                            continue
                        }
                        mascara += m
                        continue
                    }
                    mascara += try {
                        str[index]
                    } catch (e: Exception) {
                        break
                    }
                    index++
                }
                if (mascara.isNotEmpty()) {
                    var lastChar = mascara[mascara.length - 1]
                    var hadSign = false
                    while (isASign(lastChar) && str.length == old.length) {
                        mascara = mascara.substring(0, mascara.length - 1)
                        lastChar = mascara[mascara.length - 1]
                        hadSign = true
                    }
                    if (mascara.isNotEmpty() && hadSign) {
                        mascara = mascara.substring(0, mascara.length - 1)
                    }
                }
                isUpdating = true
                ediTxt.setText(mascara)
                ediTxt.setSelection(mascara.length)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }
    }
}