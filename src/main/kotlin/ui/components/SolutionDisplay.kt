package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SolutionDisplay(solution: Pair<Double, Double>?, iterations: Int, errorMessage: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                when {
                    errorMessage != null -> Color(0x55B00020)
                    solution != null -> Color(0x5500C853)
                    else -> MaterialTheme.colors.surface
                },
                MaterialTheme.shapes.medium
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Результаты решения",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Ошибка решения:",
                        color = Color(0xFFCF6679),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        errorMessage,
                        color = Color(0xFFCF6679),
                        textAlign = TextAlign.Center
                    )
                }
            }

            solution != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Решение найдено:",
                        color = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "x = ",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 18.sp
                        )
                        Text(
                            "%.6f".format(solution.first),
                            color = MaterialTheme.colors.secondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "y = ",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 18.sp
                        )
                        Text(
                            "%.6f".format(solution.second),
                            color = MaterialTheme.colors.secondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        "Количество итераций: $iterations",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            else -> {
                Text(
                    "Введите параметры и нажмите 'Решить'",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}