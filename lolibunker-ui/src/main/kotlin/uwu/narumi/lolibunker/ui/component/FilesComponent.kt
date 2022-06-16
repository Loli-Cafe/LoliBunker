package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.ui.component.helper.lightPink
import uwu.narumi.lolibunker.ui.component.helper.pink


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun composePath(path: String, onSearchClick: () -> Unit, onAddClick: () -> Unit, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = path,
        placeholder = { Text(System.getProperty("user.dir")) },
        onValueChange = onValueChange,
        label = { Text("Path") },
        singleLine = true,
        modifier = Modifier.width(780.dp).padding(20.dp).onKeyEvent {
            if (it.key.keyCode == Key.Enter.keyCode)
                onAddClick.invoke()

            false
        },
        trailingIcon = {
            Row {
                IconButton(
                    onClick = onSearchClick
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "File Chooser",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = onAddClick
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        },
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

@Composable
fun composeFiles(files: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = files,
            onValueChange = onValueChange,
            label = { Text("Files") },
            modifier = Modifier.width(740.dp).height(300.dp),
            singleLine = false,
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