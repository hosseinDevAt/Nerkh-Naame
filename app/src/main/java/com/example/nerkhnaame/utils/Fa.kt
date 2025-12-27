package com.example.nerkhnaame.utils


fun String.fa(): String {
    val english = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val persian = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    var result = this
    english.forEachIndexed { index, ch ->
        result = result.replace(ch, persian[index])
    }
    return result
}

