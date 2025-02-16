import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import net.k1ra.kotlinmultiplatformstripe.LocalLayerContainer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = "KotlinMultiplatformStripeDemo") {
        CompositionLocalProvider(LocalLayerContainer provides document.body!!) {
            App()
        }
    }
}