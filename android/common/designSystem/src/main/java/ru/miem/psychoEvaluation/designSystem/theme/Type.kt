package ru.miem.psychoEvaluation.designSystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.miem.psychoEvaluation.common.designSystem.R

private val fonts = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
    Font(R.font.inter_light, weight = FontWeight.Light),
    Font(R.font.inter_thin, weight = FontWeight.Thin),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
)

val Typography = Typography(
    // ==== SMALL SECTION ====
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.3.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.3.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.3.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.3.sp
    ),

    // ==== MEDIUM SECTION ====
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.3.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.25.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp
    ),

    // ==== LARGE SECTION ====
    labelLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.3.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.25.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        letterSpacing = 0.15.sp,
    ),
    displayLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 80.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        letterSpacing = 0.5.sp
    )
)
