package utils

import kotlin.math.*

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