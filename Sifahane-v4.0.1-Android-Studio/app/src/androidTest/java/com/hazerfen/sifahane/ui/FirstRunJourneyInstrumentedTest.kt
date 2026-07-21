package com.hazerfen.sifahane.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hazerfen.sifahane.MainActivity
import org.junit.Rule
import org.junit.Test

class FirstRunJourneyInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun cleanInstallRequiresAdminInformationPinAndPattern() {
        composeRule.waitUntil(timeoutMillis = 8_000L) {
            composeRule.onAllNodes(hasTestTag("first_run_setup")).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("first_run_setup").assertIsDisplayed()
        composeRule.onNodeWithText("İlk Kullanıcıyı Oluştur").assertIsDisplayed()
        composeRule.onNodeWithText("Yönetici Şifresi", substring = true).assertIsDisplayed()
        composeRule.onNodeWithTag("first_run_pattern").assertIsDisplayed()
        composeRule.onNodeWithTag("first_run_save").assertIsDisplayed()
    }
}
