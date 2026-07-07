package com.dodo.accounting.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dodo.accounting.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountingCriticalUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addExpense_opensEntrySheetWithExpenseDefaults() {
        composeRule.onNodeWithContentDescription("记一笔").performClick()

        composeRule.onNodeWithText("金额").assertIsDisplayed()
        composeRule.onNodeWithText("支出").assertIsDisplayed()
        composeRule.onNodeWithText("再记一笔").assertIsDisplayed()
    }

    @Test
    fun voiceMultiEntryConfirmation_showsBatchConfirmAction() {
        composeRule.onNodeWithContentDescription("语音记账").performClick()
        composeRule.onNodeWithText("识别文本").performTextInput("早餐10元，午餐20元")

        composeRule.onNodeWithText("按顺序确认 2 笔").assertIsDisplayed()
    }

    @Test
    fun webDavConfigPage_opensFromSettings() {
        openSettingsPage("WebDAV 备份")

        composeRule.onNodeWithText("连接信息").assertIsDisplayed()
        composeRule.onNodeWithText("保存配置").assertIsDisplayed()
    }

    @Test
    fun dangerOperation_zeroTransactionsDisablesClearAll() {
        openSettingsPage("危险操作")

        composeRule.onNodeWithText("未删除流水").assertIsDisplayed()
        composeRule.onNodeWithText("清空所有流水").assertIsNotEnabled()
    }

    private fun openSettingsPage(title: String) {
        composeRule.onAllNodesWithText("设置")[0].performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText(title).performClick()
        composeRule.waitForIdle()
    }
}
