package uwu.narumi.lolibunker.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uwu.narumi.lolibunker.ui.component.helper.lightPink
import uwu.narumi.lolibunker.ui.component.helper.pink

@Composable
fun composePassword(
    password: String,
    passwordVisibility: Boolean,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text("Password") },
        singleLine = true,
        modifier = Modifier.width(780.dp).padding(20.dp),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    if (passwordVisibility) Icons.Outlined.Lock else Icons.Filled.Lock,
                    if (passwordVisibility) "Hide password" else "Show password",
                    tint = Color.White
                )
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
fun composePasswordWithRetype(
    password: String,
    passwordVisibility: Boolean,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
    passwordRetype: String,
    passwordVisibilityRetype: Boolean,
    onClickRetype: () -> Unit,
    onValueChangeRetype: (String) -> Unit
) {
    Row {
        OutlinedTextField(
            value = password,
            onValueChange = onValueChange,
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.width(395.dp).padding(20.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        if (passwordVisibility) Icons.Outlined.Lock else Icons.Filled.Lock,
                        if (passwordVisibility) "Hide password" else "Show password",
                        tint = Color.White
                    )
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

        OutlinedTextField(
            value = passwordRetype,
            onValueChange = onValueChangeRetype,
            label = { Text("Retype Password") },
            singleLine = true,
            modifier = Modifier.width(395.dp).padding(20.dp),
            visualTransformation = if (passwordVisibilityRetype) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Row {
                    IconButton(onClick = onClickRetype) {
                        Icon(
                            if (passwordVisibilityRetype) Icons.Outlined.Lock else Icons.Filled.Lock,
                            if (passwordVisibilityRetype) "Hide password" else "Show password",
                            tint = Color.White
                        )
                    }

                    if (password != passwordRetype) {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.Warning,
                                "Passwords does not match",
                                tint = Color.Red,
                            )
                        }
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
}