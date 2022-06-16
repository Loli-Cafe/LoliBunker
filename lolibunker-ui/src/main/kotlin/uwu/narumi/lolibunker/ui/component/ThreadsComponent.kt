package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.LoliBunker
import uwu.narumi.lolibunker.ui.component.helper.darkerGray
import uwu.narumi.lolibunker.ui.component.helper.lightPink
import uwu.narumi.lolibunker.ui.component.helper.pink


@Composable
fun composeThreads(value: MutableState<Int>) {
    var jebanyJetpackComposeIKurwaIntSlider by remember { mutableStateOf(value.value.toFloat()) }
    var fieldValue by remember { mutableStateOf(value.value.toString()) }

    Row {
        TextField(
            value = fieldValue,
            onValueChange = {
                val number = it.toIntOrNull()
                val isEmpty = it.isEmpty() || it.isBlank()
                val threads = number ?: 1

                if (number != null || isEmpty) {
                    value.value =
                        if (threads > LoliBunker.INSTANCE.maxThreads) LoliBunker.INSTANCE.maxThreads else if (threads <= 0) 1 else threads
                    fieldValue = if (isEmpty) it else value.value.toString()
                    jebanyJetpackComposeIKurwaIntSlider = value.value.toFloat()
                }
            },
            label = { Text("Threads") },
            modifier = Modifier.width(140.dp).padding(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.LightGray,
                unfocusedBorderColor = pink,
                focusedBorderColor = lightPink,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White
            )
        )

        Slider(
            value = jebanyJetpackComposeIKurwaIntSlider,
            onValueChange = {
                jebanyJetpackComposeIKurwaIntSlider = it; value.value = it.toInt(); fieldValue = it.toInt().toString()
            },
            steps = LoliBunker.INSTANCE.maxThreads,
            valueRange = 1f..LoliBunker.INSTANCE.maxThreads.toFloat(),
            modifier = Modifier.width(640.dp).padding(30.dp),
            colors = SliderDefaults.colors(
                thumbColor = lightPink,
                activeTrackColor = pink,
                inactiveTrackColor = darkerGray,
                activeTickColor = pink,
                inactiveTickColor = darkerGray,
            )
        )
    }
}