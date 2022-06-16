package uwu.narumi.lolibunker.ui.alert

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.ui.component.helper.darkPink
import uwu.narumi.lolibunker.ui.component.helper.darkerGray
import uwu.narumi.lolibunker.ui.component.helper.getLongestLine
import uwu.narumi.lolibunker.ui.component.helper.pink

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun composeErrorAlert(message: String?, state: Boolean, onDismiss: () -> Unit, onClick: (() -> Unit)? = null) {
    if (state && message != null) {
        val size = getLongestLine(message) //Maybe some change idk
        if (size > 0) {
            AlertDialog(
                title = { Text("Error occurred") },
                text = { Text(message) },
                modifier = Modifier.width((size * 10).dp),
                shape = RoundedCornerShape(0.dp),
                onDismissRequest = onDismiss,
                buttons = {
                    Button(
                        modifier = Modifier.offset(y = (-10).dp, x = ((size * 5) - 32).dp).width(64.dp),
                        onClick = onClick ?: onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = pink,
                            disabledBackgroundColor = darkPink,
                            contentColor = Color.White,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text("Ok")
                    }
                },
                backgroundColor = darkerGray,
                contentColor = Color.LightGray
            )
        }
    }
}
