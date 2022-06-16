package uwu.narumi.lolibunker.ui.component.helper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

val darkerGray = Color(0.20f, 0.20f, 0.20f, 1.0f, ColorSpaces.Srgb)

val lightPink = Color(255, 51, 127, 255)
val whitePink = Color(255, 157, 189, 255)
val pink = Color(255, 51, 153, 255)
val selectPink = Color(255, 102, 178, 255)
val darkPink = Color(165, 36, 102, 255)

fun getLongestLine(string: String?): Int {
    if (string.isNullOrBlank())
        return 0

    return string.lines().stream()
        .mapToInt(String::length)
        .max()
        .orElseThrow()
}