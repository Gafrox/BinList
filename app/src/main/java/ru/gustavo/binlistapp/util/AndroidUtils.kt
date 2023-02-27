package ru.gustavo.binlistapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun String.countryCodeToUnicodeFlag(): String {
    return this.filter { it in 'A'..'Z' }.map { it.code.toByte() }.flatMap { char ->
        listOf(
            0xD8.toByte(),
            0x3C.toByte(),
            0xDD.toByte(),
            (0xE6.toByte() + (char - 'A'.code.toByte())).toByte()
        )
    }.toByteArray().let { bytes ->
        String(bytes, Charsets.UTF_16)
    }
}