package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun InputPanel(
    a: String, b: String, c: String, d: String, e: String, f: String,
    x0: String, y0: String,
    onCoeffChange: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Параметры системы",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Коэффициенты системы
        val coefficients = listOf(
            "a" to a, "b" to b, "c" to c,
            "d" to d, "e" to e, "f" to f
        )

        // Разбиваем на две строки
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                coefficients.take(3).forEach { (name, value) ->
                    CoefficientInputWithMenu(name, value) { onCoeffChange(name, it) }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                coefficients.drop(3).take(3).forEach { (name, value) ->
                    CoefficientInputWithMenu(name, value) { onCoeffChange(name, it) }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Начальные приближения",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Начальные приближения
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoefficientInputWithMenu("x₀", x0) { onCoeffChange("x0", it) }
            CoefficientInputWithMenu("y₀", y0) { onCoeffChange("y0", it) }
        }
    }
}
