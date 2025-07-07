import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.screens.MainScreen


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Решение системы нелинейных уравнений",
        state = rememberWindowState(width = 1000.dp, height = 700.dp)
    ) {
        MainScreen()
    }
}