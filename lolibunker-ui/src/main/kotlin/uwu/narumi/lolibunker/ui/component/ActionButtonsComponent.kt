package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.ui.component.helper.darkPink
import uwu.narumi.lolibunker.ui.component.helper.pink

@Composable
fun composeActionButtons(
    running: Boolean,
    stopping: Boolean,
    onEncryptClick: () -> Unit,
    onStopClick: () -> Unit,
    onDecryptClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = onEncryptClick,
            modifier = Modifier.width(128.dp).height(ButtonDefaults.MinHeight),
            enabled = running.not() && stopping.not(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = pink,
                disabledBackgroundColor = darkPink,
                contentColor = Color.White,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Encrypt")
        }

        Spacer(Modifier.width(10.dp))
        Button(
            onClick = onStopClick,
            modifier = Modifier.width(128.dp).height(ButtonDefaults.MinHeight),
            enabled = running && stopping.not(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = pink,
                disabledBackgroundColor = darkPink,
                contentColor = Color.White,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Stop")
        }

        Spacer(Modifier.width(10.dp))
        Button(
            onClick = onDecryptClick,
            modifier = Modifier.width(128.dp).height(ButtonDefaults.MinHeight),
            enabled = running.not() && stopping.not(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = pink,
                disabledBackgroundColor = darkPink,
                contentColor = Color.White,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Decrypt")
        }
    }
}