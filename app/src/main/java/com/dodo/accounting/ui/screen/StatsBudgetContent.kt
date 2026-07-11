package com.dodo.accounting.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow as CategorySummary
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.util.handleAmountKey
import com.dodo.accounting.domain.util.hasUnresolvedAmountExpression
import com.dodo.accounting.domain.util.normalizedAmountInput
import com.dodo.accounting.ui.viewmodel.AccountingUiState
import com.dodo.accounting.ui.viewmodel.AccountingViewModel
import com.dodo.accounting.ui.viewmodel.ExportFormat
import com.dodo.accounting.ui.viewmodel.StatsRangeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


@Composable
internal fun BudgetProgressCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var budgetAmount by remember(uiState.monthlyBudget?.amountCents) {
        mutableStateOf(uiState.monthlyBudget?.amountCents?.let { Money(it).formatPlain() } ?: "")
    }
    val monthlyExpense = uiState.monthlyExpenseByCategory.sumOf { it.amountCents }
    val budgetCents = uiState.monthlyBudget?.amountCents ?: 0
    val progress = if (budgetCents > 0) {
        (monthlyExpense.toFloat() / budgetCents.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    LedgerCard {
            SectionHeader("月度预算", if (budgetCents > 0) Money(budgetCents).format() else "未设置")
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
            SummaryMetric("本月支出", monthlyExpense, MaterialTheme.colorScheme.onSurface)
            if (budgetCents > 0) {
                SummaryMetric("剩余额度", budgetCents - monthlyExpense, MaterialTheme.colorScheme.primary)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MinimalInputLine(
                    value = budgetAmount,
                    onValueChange = { budgetAmount = it },
                    placeholder = "月预算",
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                LedgerActionButton(
                    label = "设置",
                    onClick = { viewModel.setMonthlyBudget(budgetAmount) }
                )
            }
            SectionHeader("分类预算", "${uiState.expenseCategories.size} 类")
            uiState.expenseCategories.forEach { category ->
                val budget = uiState.categoryBudgets.firstOrNull { it.categoryId == category.id }
                val spent = uiState.monthlyExpenseByCategory
                    .orEmpty()
                    .firstOrNull { it.categoryId == category.id }
                    ?.amountCents ?: 0
                CategoryBudgetRow(
                    category = category,
                    budget = budget,
                    spentCents = spent,
                    onSetBudget = { amount -> viewModel.setCategoryBudget(category, amount) }
                )
            }
    }
}

@Composable
internal fun CategoryBudgetRow(
    category: CategoryEntity,
    budget: com.dodo.accounting.data.local.entity.BudgetEntity?,
    spentCents: Long,
    onSetBudget: (String) -> Unit
) {
    var amount by remember(budget?.amountCents) {
        mutableStateOf(budget?.amountCents?.let { Money(it).formatPlain() } ?: "")
    }
    val budgetCents = budget?.amountCents ?: 0
    val progress = if (budgetCents > 0) {
        spentCents.toFloat() / budgetCents.toFloat()
    } else {
        0f
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(category.colorArgb).copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(category.iconName), contentDescription = null, tint = Color(category.colorArgb))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    "${Money(spentCents).format()} / ${if (budgetCents > 0) Money(budgetCents).format() else "未设置"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = FontFamily.Monospace
                )
            }
            MinimalInputLine(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.width(96.dp),
                singleLine = true,
                placeholder = "预算",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            IconButton(onClick = { onSetBudget(amount) }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "设置预算",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = if (progress > 1f) MaterialTheme.colorScheme.error else Color(category.colorArgb),
            trackColor = Color(category.colorArgb).copy(alpha = 0.12f)
        )
    }
}

@Composable
internal fun RecurringRulesCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var accountId by remember { mutableStateOf<Long?>(null) }
    var fromAccountId by remember { mutableStateOf<Long?>(null) }
    var toAccountId by remember { mutableStateOf<Long?>(null) }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var merchant by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val categories = when (selectedType) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }

    LedgerCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EventRepeat, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("周期账单", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = viewModel::runDueRecurringRules) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "生成到期账单")
                }
            }

            TypeSelector(selectedType = selectedType, onTypeSelected = {
                selectedType = it
                categoryId = null
            })
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "规则名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            MinimalInputLine(
                value = amount,
                onValueChange = { amount = it },
                placeholder = "金额",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            if (selectedType == TransactionType.TRANSFER) {
                AccountPickerField(
                    label = "转出账户",
                    accounts = uiState.activeAccounts,
                    selectedAccountId = fromAccountId,
                    onSelected = { fromAccountId = it }
                )
                AccountPickerField(
                    label = "转入账户",
                    accounts = uiState.activeAccounts,
                    selectedAccountId = toAccountId,
                    onSelected = { toAccountId = it }
                )
            } else {
                AccountPickerField(
                    label = "资产账户",
                    accounts = uiState.activeAccounts,
                    selectedAccountId = accountId,
                    onSelected = { accountId = it }
                )
            }
            if (categories.isNotEmpty()) {
                CategoryPickerField(
                    label = "分类",
                    categories = categories,
                    selectedCategoryId = categoryId,
                    onSelected = { categoryId = it }
                )
            }
            if (selectedType != TransactionType.TRANSFER && selectedType != TransactionType.BALANCE_ADJUSTMENT) {
                MinimalInputLine(
                    value = merchant,
                    onValueChange = { merchant = it },
                    placeholder = "商户",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            MinimalInputLine(
                value = note,
                onValueChange = { note = it },
                placeholder = "备注",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 2
            )
            LedgerActionButton(
                label = "添加每月规则",
                icon = Icons.Default.EventRepeat,
                onClick = {
                    viewModel.addMonthlyRecurringRule(
                        name = name,
                        type = selectedType,
                        amount = amount,
                        accountId = accountId,
                        fromAccountId = fromAccountId,
                        toAccountId = toAccountId,
                        categoryId = categoryId,
                        merchant = merchant,
                        note = note
                    )
                    name = ""
                    amount = ""
                    merchant = ""
                    note = ""
                    categoryId = null
                },
                modifier = Modifier.fillMaxWidth()
            )

            uiState.recurringRules.forEach { rule ->
                RecurringRuleRow(rule, viewModel)
            }
    }
}

@Composable
internal fun RecurringRuleRow(
    rule: RecurringRuleEntity,
    viewModel: AccountingViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(rule.name, style = MaterialTheme.typography.titleSmall)
            Text(
                "${transactionLabel(rule.transactionType)} ${Money(rule.amountCents).format()} · 下次 ${dateLabel(rule.nextRunAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Switch(
            checked = rule.isEnabled,
            onCheckedChange = { viewModel.setRecurringRuleEnabled(rule.id, it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
        IconButton(onClick = { viewModel.deleteRecurringRule(rule.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "删除周期规则")
        }
    }
}
