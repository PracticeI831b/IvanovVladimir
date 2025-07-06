import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.*

@Composable
@Preview
fun App() {
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

@Composable
fun EquationDisplay(a: String, b: String, c: String, d: String, e: String, f: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Система уравнений:",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Красивый вывод системы уравнений с фигурной скобкой
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
                    "sin($a·x) + $b·y = $c",
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    "cos(y - $d) + $e·x = $f",
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp
                )
            }
        }
    }
}

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

fun solveSystem(
    a: Double, b: Double, c: Double, d: Double, e: Double, f: Double,
    x0: Double, y0: Double,
    precision: Double = 0.001,
    maxIterations: Int = 1000,
    onSuccess: (Double, Double, Int) -> Unit,
    onError: (String) -> Unit
) {
    // Проверка коэффициентов
    if (b == 0.0) {
        onError("Коэффициент b не может быть нулевым")
        return
    }

    if (e == 0.0) {
        onError("Коэффициент e не может быть нулевым")
        return
    }

    var x = x0
    var y = y0
    var prevX: Double
    var prevY: Double
    var iter = 0

    do {
        prevX = x
        prevY = y

        // Итерационные формулы
        try {
            x = (f - cos(y - d)) / e
            y = (c - sin(a * x)) / b
        } catch (ex: Exception) {
            onError("Ошибка вычислений: ${ex.message}")
            return
        }

        iter++

        // Проверка на расходимость
        if (iter > maxIterations) {
            onError("Превышено максимальное количество итераций ($maxIterations)")
            return
        }

        if (x.isNaN() || y.isNaN() || x.isInfinite() || y.isInfinite()) {
            onError("Метод расходится для данных параметров")
            return
        }

    } while (abs(x - prevX) > precision || abs(y - prevY) > precision)

    onSuccess(x, y, iter)
}

fun String.parseMath(): Double {
    return when (this.lowercase()) {
        "pi", "π" -> Math.PI
        "e" -> Math.E
        else -> {
            // Заменяем запятые на точки и парсим число
            val normalized = this.replace(",", ".")
            try {
                normalized.toDouble()
            } catch (e: Exception) {
                throw IllegalArgumentException("Некорректное значение: '$this'")
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Решение системы нелинейных уравнений",
        state = rememberWindowState(width = 1000.dp, height = 700.dp)
    ) {
        App()
    }
}