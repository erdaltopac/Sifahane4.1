package com.hazerfen.sifahane.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class ThemeScreenshotSmokeInstrumentedTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun criticalContentRendersInLightDarkOledAndLargeText() {
        listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.OLED).forEach { mode ->
            composeRule.setContent {
                SifahaneTheme(
                    configuration = ThemeConfiguration(
                        mode = mode,
                        fontScale = 2.0f,
                        dynamicColor = false
                    )
                ) {
                    Surface(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Şifahane İlaç Randevu Ölçüm ĞİıŞş", style = MaterialTheme.typography.titleLarge)
                            Text("Uzun açıklama metni büyük yazıda kırpılmadan birden fazla satıra yayılmalıdır.")
                            Button(onClick = {}) { Text("İşlemi Onayla") }
                        }
                    }
                }
            }
            composeRule.waitForIdle()
            val image = composeRule.onRoot().captureToImage()
            check(image.width > 0 && image.height > 0) { "$mode teması görüntü üretmedi." }
        }
    }
}
