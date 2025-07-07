package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.EquationDisplay
import ui.components.InputPanel
import ui.components.SolutionDisplay
import utils.parseMath
import utils.solveSystem

@Composable
fun MainScreen() {
    // Состояния для коэффициентов
    var a by remember { mutableStateOf("1.0") }
    var b by remember { mutableStateOf("1.0") }
    var c by remember { mutableStateOf("1.0") }
    var d by remember { mutableStateOf("1.0") }
    var e by remember { mutableStateOf("1.0") }
    var f by remember { mutableStateOf("1.0") }

    // Начальные приближения
    var x0 by remember { mutableStateOf("0.5") }
    var y0 by remember { mutableStateOf("0.5") }

    // Результаты решения
    var solution by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var iterations by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showHelp by remember { mutableStateOf(false) }

    // Тёмная тема
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFF2196F3), // Синий акцентный цвет
            primaryVariant = Color(0xFF0D47A1),
            secondary = Color(0xFF03A9F4),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color(0xFFD0D0D0)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    "Решение системы нелинейных уравнений",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Блок с исходной системой уравнений
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Исходная система уравнений:",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            "{",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 48.sp,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .offset(y = (-8).dp)
                        )
                        Column {
                            Text(
                                "sin(a·x) + b·y = c",
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                "cos(y - d) + e·x = f",
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                // Основной контент
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Панель ввода
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InputPanel(
                            a, b, c, d, e, f, x0, y0,
                            onCoeffChange = { coeff, value ->
                                when (coeff) {
                                    "a" -> a = value
                                    "b" -> b = value
                                    "c" -> c = value
                                    "d" -> d = value
                                    "e" -> e = value
                                    "f" -> f = value
                                    "x0" -> x0 = value
                                    "y0" -> y0 = value
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Кнопки управления
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    errorMessage = null
                                    solution = null

                                    try {
                                        val params = listOf(a, b, c, d, e, f, x0, y0).map {
                                            it.replace(",", ".").parseMath()
                                        }

                                        val aVal = params[0]
                                        val bVal = params[1]
                                        val cVal = params[2]
                                        val dVal = params[3]
                                        val eVal = params[4]
                                        val fVal = params[5]
                                        val x0Val = params[6]
                                        val y0Val = params[7]

                                        solveSystem(
                                            aVal, bVal, cVal, dVal, eVal, fVal,
                                            x0Val, y0Val,
                                            onSuccess = { x, y, iter ->
                                                solution = x to y
                                                iterations = iter
                                            },
                                            onError = { errorMessage = it }
                                        )
                                    } catch (ex: Exception) {
                                        errorMessage = "Ошибка ввода: ${ex.message}"
                                    }
                                },
                                modifier = Modifier.width(150.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF1565C0),  // Более насыщенный синий
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Решить", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showHelp = true },
                                modifier = Modifier.width(150.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF1565C0),  // Более насыщенный синий
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Info, contentDescription = "Помощь", modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Справка", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Панель вывода
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Отображение системы уравнений
                        EquationDisplay(a, b, c, d, e, f)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Отображение результатов
                        SolutionDisplay(solution, iterations, errorMessage)
                    }
                }

                // Диалог справки
                if (showHelp) {
                    AlertDialog(
                        onDismissRequest = { showHelp = false },
                        title = {
                            Text(
                                "Объяснение метода итераций",
                                color = MaterialTheme.colors.primary
                            )
                        },
                        text = {
                            Column {
                                Text(
                                    "Метод итераций - это численный метод решения систем уравнений, где решение уточняется на каждом шаге:",
                                    color = MaterialTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("1. Уравнения преобразуются в итерационную форму:", color = MaterialTheme.colors.onSurface)
                                Text("   xₙ₊₁ = (f - cos(yₙ - d)) / e", color = MaterialTheme.colors.primary)
                                Text("   yₙ₊₁ = (c - sin(a·xₙ)) / b", color = MaterialTheme.colors.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("2. Начальные значения (x₀, y₀) выбираются:", color = MaterialTheme.colors.onSurface)
                                Text("   - Пользователем или по умолчанию (0.5, 0.5)", color = MaterialTheme.colors.onSurface)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("3. Итерации повторяются до достижения точности 0.001:", color = MaterialTheme.colors.onSurface)
                                Text("   |Δx| < 0.001 и |Δy| < 0.001", color = MaterialTheme.colors.onSurface)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Важные замечания:", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
                                Text("- Начальные приближения влияют на сходимость", color = MaterialTheme.colors.onSurface)
                                Text("- При неудачном выборе метод может расходиться", color = MaterialTheme.colors.onSurface)
                                Text("- Для сложных систем пробуйте разные начальные значения", color = MaterialTheme.colors.onSurface)
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showHelp = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Закрыть", fontWeight = FontWeight.Bold)
                            }
                        },
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}