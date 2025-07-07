package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CoefficientInputWithMenu(
    name: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Метка с именем коэффициента
        Text(
            "$name:",
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Контейнер для ввода и меню
        Box {
            // Поле ввода
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("[\\d.,eEπ\\-]*"))) {
                        onValueChange(newValue)
                    }
                },
                placeholder = {
                    Text(
                        "введите $name",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .width(120.dp)
                    .background(MaterialTheme.colors.surface, MaterialTheme.shapes.small)
            )

            // Кнопка вызова меню
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Константы",
                    tint = MaterialTheme.colors.primary
                )
            }

            // Выпадающее меню с константами
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colors.surface)
            ) {
                listOf("π", "e").forEach { constant ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(constant)
                            expanded = false
                        }
                    ) {
                        Text(constant, color = MaterialTheme.colors.onSurface)
                    }
                }
            }
        }
    }
}
