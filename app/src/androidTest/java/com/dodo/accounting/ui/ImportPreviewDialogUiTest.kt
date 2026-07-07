package com.dodo.accounting.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.ui.screen.ImportPreviewDialogContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ImportPreviewDialogUiTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun importPreviewCancel_invokesCancelCallback() {
        var cancelled = false
        composeRule.setContent {
            MaterialTheme {
                ImportPreviewDialogContent(
                    importPreview = BackupPreview(
                        schemaVersion = 1,
                        exportedAt = 0L,
                        accountCount = 1,
                        categoryCount = 2,
                        tagCount = 3,
                        transactionCount = 4,
                        budgetCount = 5,
                        recurringRuleCount = 6
                    ),
                    onConfirm = {},
                    onCancel = { cancelled = true }
                )
            }
        }

        composeRule.onNodeWithText("取消").performClick()

        composeRule.runOnIdle {
            assertTrue(cancelled)
        }
    }
}
