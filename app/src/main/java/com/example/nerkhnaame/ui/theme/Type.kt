package com.example.nerkhnaame.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.example.nerkhnaame.R

val shabnamFont = FontFamily(
    Font(R.font.shabnam_medium, FontWeight.Normal),
    Font(R.font.shabnam_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = shabnamFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        textDirection = TextDirection.Rtl
    )
)