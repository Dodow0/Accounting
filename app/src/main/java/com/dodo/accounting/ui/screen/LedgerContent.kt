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
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow as CategorySummary
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.util.handleAmountKey
import com.dodo.accounting.domain.util.hasUnresolvedAmountExpression
import com.dodo.accounting.domain.util.normalizedAmountInput
import com.dodo.accounting.ui.viewmodel.AccountingUiState
import com.dodo.accounting.ui.viewmodel.AccountingViewModel
import com.dodo.accounting.ui.viewmodel.ExportFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
internal fun LedgerScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    var viewMode by remember { mutableStateOf(LedgerViewMode.List) }
    val displayedTransactions = uiState.searchResults.filter { transaction ->
        selectedCategoryId == null || transaction.transaction.categoryId == selectedCategoryId
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LedgerViewModeSelector(
            selectedMode = viewMode,
            onSelected = { viewMode = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            when (viewMode) {
                LedgerViewMode.List -> LedgerListContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = { selectedCategoryId = it },
                    displayedTransactions = displayedTransactions,
                    onEditTransaction = onEditTransaction,
                    onLongPress = { actionTarget = it }
                )
                LedgerViewMode.Calendar -> CalendarScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onEditTransaction = onEditTransaction
                )
            }
        }
    }

    actionTarget?.let { transaction ->
        TransactionActionSheet(
            transaction = transaction,
            onDismiss = { actionTarget = null },
            onEdit = {
                actionTarget = null
                onEditTransaction(transaction)
            },
            onDelete = {
                actionTarget = null
                viewModel.deleteTransaction(transaction.transaction.id)
            }
        )
    }
}

@Composable
internal fun LedgerListContent(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit,
    displayedTransactions: List<TransactionWithDetails>,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    onLongPress: (TransactionWithDetails) -> Unit
) {
    val searchFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { searchFocusRequester.requestFocus() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    MinimalInputLine(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::setSearchQuery,
                        placeholder = "搜索商户、备注、分类、账户、标签",
                        modifier = Modifier.weight(1f),
                        focusRequester = searchFocusRequester
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    LedgerTextFilter(
                        selected = uiState.searchType == null,
                        label = "全部",
                        onClick = { viewModel.setSearchType(null) }
                    )
                    listOf(TransactionType.EXPENSE, TransactionType.INCOME, TransactionType.TRANSFER).forEach { type ->
                        LedgerTextFilter(
                            selected = uiState.searchType == type,
                            label = transactionLabel(type),
                            onClick = { viewModel.setSearchType(type) }
                        )
                    }
                }
                AccountFilterRow(uiState, viewModel)
                CategoryFilterRow(
                    categories = uiState.expenseCategories + uiState.incomeCategories,
                    selectedCategoryId = selectedCategoryId,
                    onSelected = onCategorySelected
                )
            }
        }
        transactionDayGroups(
            transactions = displayedTransactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "没有匹配的流水",
            showInlineActions = false,
            onLongPress = onLongPress
        )
    }
}

@Composable
internal fun LedgerViewModeSelector(
    selectedMode: LedgerViewMode,
    onSelected: (LedgerViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LedgerViewMode.entries.forEach { mode ->
                val selected = selectedMode == mode
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clickable { onSelected(mode) },
                    shape = RoundedCornerShape(7.dp),
                    color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            mode.label,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun LedgerTextFilter(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
    )
}

internal fun LazyListScope.transactionDayGroups(
    transactions: List<TransactionWithDetails>,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    onDeleteTransaction: (TransactionWithDetails) -> Unit,
    emptyText: String = "暂无流水",
    showInlineActions: Boolean = true,
    onLongPress: ((TransactionWithDetails) -> Unit)? = null,
    groupModifier: Modifier = Modifier
) {
    val groups = transactions.groupedByDay()
    if (groups.isEmpty()) {
        item {
            Text(
                emptyText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    groups.forEach { group ->
        item(key = "day-${group.date}") {
            Column(modifier = groupModifier.fillMaxWidth()) {
                TransactionDayHeader(group)
                group.items.forEachIndexed { index, transaction ->
                    TransactionRow(
                        item = transaction,
                        onClick = onLongPress?.let { openActions -> { openActions(transaction) } },
                        onLongClick = onLongPress?.let { longPress -> { longPress(transaction) } },
                        trailing = {
                            if (showInlineActions) {
                                Row {
                                    IconButton(onClick = { onEditTransaction(transaction) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                                    }
                                    IconButton(onClick = { onDeleteTransaction(transaction) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "移入回收站")
                                    }
                                }
                            }
                        }
                    )
                    if (index != group.items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 52.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.42f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun TransactionDayHeader(group: TransactionDayGroup) {
    val summaryParts = buildList {
        if (group.expenseCents > 0) add("支出 ${Money(group.expenseCents).format()}")
        if (group.incomeCents > 0) add("收入 ${Money(group.incomeCents).format()}")
    }
    val today = LocalDate.now()
    val dayLabel = when (group.date) {
        today -> "今天"
        today.minusDays(1) -> "昨天"
        today.minusDays(2) -> "前天"
        else -> weekdayLabel(group.date)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$dayLabel · ${group.date.format(DateTimeFormatter.ofPattern("M月d日", Locale.CHINA))}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Text(
            summaryParts.joinToString(" · ").ifBlank { "无收支" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
internal fun CalendarScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    val monthStart = localDateFromMillis(uiState.calendarMonthStartMillis)
    val selectedDate = localDateFromMillis(uiState.calendarSelectedDateMillis)
    val selectedTransactions = uiState.calendarMonthTransactions.filter {
        localDateFromMillis(it.transaction.occurredAt) == selectedDate
    }
    val selectedReminders = remindersForDate(uiState.recurringRules, selectedDate)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = { viewModel.moveCalendarMonth(-1) }) {
                    Text("上月")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${monthStart.year}年${monthStart.monthValue}月",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "日历账单",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                FilledTonalButton(onClick = { viewModel.moveCalendarMonth(1) }) {
                    Text("下月")
                }
            }
        }
        item {
            CalendarMonthGrid(
                monthStart = monthStart,
                selectedDate = selectedDate,
                transactions = uiState.calendarMonthTransactions,
                recurringRules = uiState.recurringRules,
                onSelected = { viewModel.selectCalendarDate(localDateStartMillis(it)) }
            )
        }
        item {
            FilledTonalButton(
                onClick = viewModel::resetCalendarToToday,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("回到今天")
            }
        }
        item {
            SectionHeader(
                "${selectedDate.monthValue}月${selectedDate.dayOfMonth}日 ${weekdayLabel(selectedDate)}",
                "${selectedTransactions.size} 条 · ${selectedReminders.size} 提醒"
            )
        }
        if (selectedReminders.isNotEmpty()) {
            item {
                CalendarReminderList(selectedReminders)
            }
        }
        transactionDayGroups(
            transactions = selectedTransactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "当天暂无流水"
        )
    }
}

@Composable
internal fun CalendarMonthGrid(
    monthStart: LocalDate,
    selectedDate: LocalDate,
    transactions: List<TransactionWithDetails>,
    recurringRules: List<RecurringRuleEntity>,
    onSelected: (LocalDate) -> Unit
) {
    val groupsByDate = transactions.groupedByDay().associateBy { it.date }
    val remindersByDate = recurringRules
        .filter { it.isEnabled }
        .groupBy { localDateFromMillis(it.nextRunAt) }
    val days = calendarMonthCells(monthStart)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("一", "二", "三", "四", "五", "六", "日").forEach { label ->
                    Text(
                        label,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            days.chunked(7).forEach { week ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { day ->
                        val group = groupsByDate[day.date]
                        val reminderCount = remindersByDate[day.date].orEmpty().size
                        CalendarDayCell(
                            day = day,
                            selected = day.date == selectedDate,
                            group = group,
                            reminderCount = reminderCount,
                            onClick = { onSelected(day.date) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun CalendarDayCell(
    day: CalendarDay,
    selected: Boolean,
    group: TransactionDayGroup?,
    reminderCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = when {
        selected -> MaterialTheme.colorScheme.primaryContainer
        day.inMonth -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f)
    }
    val isToday = day.date == LocalDate.now()
    val dayTextColor = when {
        isToday -> Color.White
        day.inMonth -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        modifier = modifier
            .heightIn(min = 64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = container,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (selected) 1f else 0.34f))
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isToday) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {}
                }
                Text(
                    day.date.dayOfMonth.toString(),
                    fontWeight = if (selected || isToday) FontWeight.Bold else FontWeight.SemiBold,
                    color = dayTextColor,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            group?.let {
                if (it.expenseCents > 0) {
                    Text(
                        "-${Money(it.expenseCents).formatPlain()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Monospace
                    )
                }
                if (it.incomeCents > 0) {
                    Text(
                        "+${Money(it.incomeCents).formatPlain()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            if (reminderCount > 0) {
                Text(
                    "提醒 $reminderCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
internal fun CalendarReminderList(reminders: List<RecurringRuleEntity>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            reminders.forEach { rule ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(rule.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            listOf(transactionLabel(rule.transactionType), rule.merchant, rule.note)
                                .filter { it.isNotBlank() }
                                .joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        Money(rule.amountCents).format(),
                        color = transactionColor(rule.transactionType),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
internal fun AccountFilterRow(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LedgerTextFilter(
            selected = uiState.selectedAccountId == null,
            onClick = { viewModel.setSelectedAccount(null) },
            label = "全部账户"
        )
        uiState.activeAccounts.forEach { account ->
            LedgerTextFilter(
                selected = uiState.selectedAccountId == account.id,
                onClick = { viewModel.setSelectedAccount(account.id) },
                label = account.name
            )
        }
    }
}

@Composable
internal fun CategoryFilterRow(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long?) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LedgerTextFilter(
            selected = selectedCategoryId == null,
            onClick = { onSelected(null) },
            label = "全部分类"
        )
        categories.forEach { category ->
            LedgerTextFilter(
                selected = selectedCategoryId == category.id,
                onClick = { onSelected(category.id) },
                label = category.name
            )
        }
    }
}
