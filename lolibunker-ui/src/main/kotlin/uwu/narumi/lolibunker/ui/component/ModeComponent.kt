package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import uwu.narumi.lolibunker.ui.component.helper.darkerGray
import uwu.narumi.lolibunker.ui.component.helper.lightPink
import uwu.narumi.lolibunker.ui.component.helper.pink


@Composable
fun composeMode(
    selected: String,
    expanded: Boolean,
    onFocus: () -> Unit,
    onDismissRequest: () -> Unit,
    onIconClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var fieldSize by remember { mutableStateOf(Size.Zero) }
    var jebanyKurwaCompose by remember { mutableStateOf(false) }
    if (jebanyKurwaCompose)
        LocalFocusManager.current.clearFocus(true)

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selected,
            onValueChange = { },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .onGloballyPositioned { coordinates -> fieldSize = coordinates.size.toSize() }.width(780.dp)
                .onFocusChanged {
                    if (expanded.not() && it.isFocused) {
                        onFocus.invoke()
                        jebanyKurwaCompose = true
                    }
                },
            label = { Text("Mode") },
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Options",
                    modifier = Modifier.clickable(onClick = onIconClick),
                    tint = Color.White
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.LightGray,
                unfocusedBorderColor = pink,
                focusedBorderColor = lightPink,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White
            ),
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
                .background(color = darkerGray, shape = RoundedCornerShape(0.dp)),
        ) {
            content.invoke(this)
        }
    }
}