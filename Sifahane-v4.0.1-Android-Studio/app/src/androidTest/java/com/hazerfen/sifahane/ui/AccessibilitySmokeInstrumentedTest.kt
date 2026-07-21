package com.hazerfen.sifahane.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.hazerfen.sifahane.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccessibilitySmokeInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true)
    }

    @Test
    fun firstScreenExposesAUsableSemanticTreeAndViewHierarchy() {
        composeRule.waitForIdle()
        composeRule.onRoot().printToLog("SIFAHANE_ACCESSIBILITY")
        check(composeRule.onAllNodes(hasClickAction()).fetchSemanticsNodes().isNotEmpty()) {
            "İlk ekranda erişilebilir bir etkileşim alanı bulunamadı."
        }
        onView(isRoot()).check(matches(isDisplayed()))
    }
}
