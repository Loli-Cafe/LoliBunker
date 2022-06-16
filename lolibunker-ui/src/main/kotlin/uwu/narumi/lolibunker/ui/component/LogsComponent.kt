package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.ui.component.helper.lightPink
import uwu.narumi.lolibunker.ui.component.helper.pink

@Composable
fun composeLogs(logs: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = logs.joinToString(separator = "\n"),
            onValueChange = { },
            label = { Text("Logs") },
            modifier = Modifier.width(740.dp).height(404.dp).padding(top = 20.dp),
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.LightGray,
                unfocusedBorderColor = pink,
                focusedBorderColor = lightPink,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White
            )
        )
    }
}