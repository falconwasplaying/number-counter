package com.falcon.splashscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TimesNewRoman = FontFamily(
    Font(R.font.times_new_roman_mt_regular, FontWeight.Normal),
)

@Composable
fun SplashScreenContent() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // diagonal line
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = contentColor,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                strokeWidth = 4f,
            )
        }

        // shared alignment multiplier
        val linePositionZero = 0.42f
        val linePositionOne = 0.58f

        Text(
            text = "0",
            color = contentColor,
            fontFamily = TimesNewRoman,
            fontSize = 90.sp,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .offset(
                    x = (screenWidth * linePositionZero) - 30.dp, // Shifting Left
                    y = (screenHeight * linePositionZero) + 70.dp  // Shifting Down
                )
        )
        
        Text(
            text = "1",
            color = contentColor,
            fontFamily = TimesNewRoman,
            fontSize = 90.sp,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .offset(
                    x = (screenWidth * linePositionOne) + 0.dp, // Shifting Right
                    y = (screenHeight * linePositionOne) - 120.dp  // Shifting Up
                )
        )
    }
}
