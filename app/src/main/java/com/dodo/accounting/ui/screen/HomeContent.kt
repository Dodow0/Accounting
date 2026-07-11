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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.Mic
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
import androidx.compose.material.icons.filled.TrackChanges
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
import com.dodo.accounting.ui.viewmodel.HomeUiState
import com.dodo.accounting.ui.viewmodel.HomeViewModel
import com.dodo.accounting.ui.viewmodel.HomePeriod
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
internal fun HomeScreen(
    uiState: HomeUiState,
    viewModel: HomeViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    onOpenSearch: () -> Unit,
    amountsHidden: Boolean = false
) {
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    var customRangeSheetOpen by remember { mutableStateOf(false) }
    val homeRangeStart = localDateFromMillis(uiState.homeRangeStartMillis)
    val homeRangeEndExclusive = localDateFromMillis(uiState.homeRangeEndMillis)
    val homeTransactions = uiState.homeTransactions
    var searchQuery by remember { mutableStateOf("") }
    val filteredHomeTransactions = remember(homeTransactions, searchQuery) {
        homeTransactions.filter { it.matchesHomeSearch(searchQuery) }
    }
    val transactionGroups = remember(filteredHomeTransactions) {
        filteredHomeTransactions.groupedByDay()
    }

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                HomeSearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    onSearchClick = onOpenSearch
                )
            }
        }
        item {
            HomeMonthlySummaryCard(
                period = uiState.homePeriod,
                rangeStart = homeRangeStart,
                rangeEndExclusive = homeRangeEndExclusive,
                expenseCents = uiState.periodSummary.expenseCents,
                incomeCents = uiState.periodSummary.incomeCents,
                onPeriodSelected = { period ->
                    viewModel.setHomePeriod(period)
                    if (period == HomePeriod.CUSTOM) customRangeSheetOpen = true
                },
                onPreviousPeriod = { viewModel.moveHomePeriod(-1) },
                onNextPeriod = { viewModel.moveHomePeriod(1) },
                onResetToCurrent = { viewModel.resetHomePeriodToCurrent() },
                amountsHidden = amountsHidden,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        if (transactionGroups.isEmpty()) {
            item {
                HomeEmptyTransactionList(
                    text = if (searchQuery.isBlank()) homeEmptyText(uiState.homePeriod) else "没有匹配的流水"
                )
            }
        } else {
            transactionGroups.forEach { group ->
                item(key = "header-${group.date}") {
                    HomeTransactionGroupHeader(
                        group = group,
                        amountsHidden = amountsHidden
                    )
                }
                items(group.items, key = { it.transaction.id }) { transaction ->
                    HomeTransactionItem(
                        item = transaction,
                        amountsHidden = amountsHidden,
                        onClick = { onEditTransaction(transaction) },
                        onLongClick = { actionTarget = transaction }
                    )
                }
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

    if (customRangeSheetOpen) {
        HomeCustomRangeSheet(
            initialStart = homeRangeStart,
            initialEndInclusive = homeRangeEndExclusive.minusDays(1),
            onDismiss = { customRangeSheetOpen = false },
            onApply = { start, endInclusive ->
                viewModel.setHomeCustomRange(
                    startMillis = localDateStartMillis(start),
                    endMillis = localDateStartMillis(endInclusive)
                )
                customRangeSheetOpen = false
            }
        )
    }
}

@Composable
internal fun HomeMonthlySummaryCard(
    period: HomePeriod,
    rangeStart: LocalDate,
    rangeEndExclusive: LocalDate,
    expenseCents: Long,
    incomeCents: Long,
    onPeriodSelected: (HomePeriod) -> Unit,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    onResetToCurrent: () -> Unit,
    amountsHidden: Boolean = false,
    modifier: Modifier = Modifier
) {
    val balanceCents = incomeCents - expenseCents

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomePeriodSelector(
                    selected = period,
                    onSelected = onPeriodSelected
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPreviousPeriod) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "上一周期",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onNextPeriod) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "下一周期",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    homeRangeLabel(period, rangeStart, rangeEndExclusive),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = onResetToCurrent) {
                    Icon(
                        Icons.Default.Restore,
                        contentDescription = "回到当前周期",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "支出",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    privacyAmountLabel(expenseCents, amountsHidden),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                HomeSummaryMetric(
                    label = "收入",
                    value = privacyPlainAmountLabel(incomeCents, amountsHidden),
                    valueColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                HomeSummaryMetric(
                    label = "结余",
                    value = privacyPlainAmountLabel(balanceCents, amountsHidden),
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HomePeriodSelector(
    selected: HomePeriod,
    onSelected: (HomePeriod) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(
            HomePeriod.WEEK to "周",
            HomePeriod.MONTH to "月",
            HomePeriod.YEAR to "年",
            HomePeriod.CUSTOM to "自定"
        ).forEach { (period, label) ->
            LedgerChoiceChip(
                selected = selected == period,
                label = label,
                onClick = { onSelected(period) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeCustomRangeSheet(
    initialStart: LocalDate,
    initialEndInclusive: LocalDate,
    onDismiss: () -> Unit,
    onApply: (LocalDate, LocalDate) -> Unit
) {
    var start by remember(initialStart) { mutableStateOf(initialStart) }
    var end by remember(initialEndInclusive) { mutableStateOf(initialEndInclusive) }
    var editingDate by remember { mutableStateOf<HomeRangeDateField?>(null) }
    if (end.isBefore(start)) end = start

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(width = 42.dp, height = 5.dp),
                    shape = RoundedCornerShape(999.dp),
                    color = LedgerDivider
                ) {}
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    when (editingDate) {
                        HomeRangeDateField.Start -> "选择开始日期"
                        HomeRangeDateField.End -> "选择结束日期"
                        null -> "自定义时间"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { if (editingDate == null) onDismiss() else editingDate = null }) {
                    Text(if (editingDate == null) "取消" else "完成")
                }
            }

            when (val field = editingDate) {
                null -> {
                    HomeDateSelectRow(
                        label = "开始",
                        date = start,
                        onClick = { editingDate = HomeRangeDateField.Start }
                    )
                    HomeDateSelectRow(
                        label = "结束",
                        date = end,
                        onClick = { editingDate = HomeRangeDateField.End }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LedgerChoiceChip(
                            selected = false,
                            label = "最近7天",
                            onClick = {
                                end = LocalDate.now()
                                start = end.minusDays(6)
                            }
                        )
                        LedgerChoiceChip(
                            selected = false,
                            label = "本月",
                            onClick = {
                                val today = LocalDate.now()
                                start = today.withDayOfMonth(1)
                                end = today
                            }
                        )
                        LedgerChoiceChip(
                            selected = false,
                            label = "今年",
                            onClick = {
                                val today = LocalDate.now()
                                start = today.withDayOfYear(1)
                                end = today
                            }
                        )
                    }
                    LedgerActionButton(
                        label = "查看这个范围",
                        icon = Icons.Default.DateRange,
                        onClick = { onApply(start, end) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> {
                    HomeDateWheelPicker(
                        date = if (field == HomeRangeDateField.Start) start else end,
                        onDateChange = { nextDate ->
                            if (field == HomeRangeDateField.Start) {
                                start = nextDate
                                if (end.isBefore(start)) end = start
                            } else {
                                end = if (nextDate.isBefore(start)) start else nextDate
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeDateSelectRow(
    label: String,
    date: LocalDate,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .ledgerPressClickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.width(48.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                homeDateLabel(date),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = "选择$label")
        }
    }
}

@Composable
private fun HomeDateWheelPicker(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val currentYear = remember { LocalDate.now().year }
    var year by remember(date) { mutableStateOf(date.year) }
    var month by remember(date) { mutableStateOf(date.monthValue) }
    var day by remember(date) { mutableStateOf(date.dayOfMonth) }
    val daysInMonth = remember(year, month) { YearMonth.of(year, month).lengthOfMonth() }

    LaunchedEffect(daysInMonth) {
        if (day > daysInMonth) day = daysInMonth
    }

    LaunchedEffect(year, month, day) {
        onDateChange(LocalDate.of(year, month, day.coerceAtMost(daysInMonth)))
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        WheelPickerColumn(
            values = ((currentYear - 5)..(currentYear + 5)).toList(),
            selected = year,
            label = { "${it}年" },
            onSelected = { year = it },
            modifier = Modifier.weight(1f)
        )
        WheelPickerColumn(
            values = (1..12).toList(),
            selected = month,
            label = { "${it}月" },
            onSelected = { month = it },
            modifier = Modifier.weight(1f)
        )
        WheelPickerColumn(
            values = (1..daysInMonth).toList(),
            selected = day.coerceAtMost(daysInMonth),
            label = { "${it}日" },
            onSelected = { day = it },
            modifier = Modifier.weight(1f)
        )
    }
}

private enum class HomeRangeDateField {
    Start,
    End
}

private fun homeRangeLabel(
    period: HomePeriod,
    start: LocalDate,
    endExclusive: LocalDate
): String {
    val endInclusive = endExclusive.minusDays(1)
    return when (period) {
        HomePeriod.WEEK -> "${start.monthValue}月${start.dayOfMonth}日 - ${endInclusive.monthValue}月${endInclusive.dayOfMonth}日"
        HomePeriod.MONTH -> "${start.year}年${start.monthValue}月"
        HomePeriod.YEAR -> "${start.year}年"
        HomePeriod.CUSTOM -> {
            if (start == endInclusive) {
                homeDateLabel(start)
            } else {
                "${homeDateLabel(start)} - ${homeDateLabel(endInclusive)}"
            }
        }
    }
}

private fun homeDateLabel(date: LocalDate): String {
    return "${date.monthValue}月${date.dayOfMonth}日"
}

private fun homeEmptyText(period: HomePeriod): String = when (period) {
    HomePeriod.WEEK -> "本周暂无流水"
    HomePeriod.MONTH -> "本月暂无流水"
    HomePeriod.YEAR -> "今年暂无流水"
    HomePeriod.CUSTOM -> "当前范围暂无流水"
}

private fun TransactionWithDetails.matchesHomeSearch(query: String): Boolean {
    val q = query.trim()
    if (q.isBlank()) return true
    val transaction = transaction
    return transaction.note.contains(q, ignoreCase = true) ||
        transaction.merchant.contains(q, ignoreCase = true) ||
        category?.name.orEmpty().contains(q, ignoreCase = true) ||
        account?.name.orEmpty().contains(q, ignoreCase = true) ||
        fromAccount?.name.orEmpty().contains(q, ignoreCase = true) ||
        toAccount?.name.orEmpty().contains(q, ignoreCase = true) ||
        tags.any { it.name.contains(q, ignoreCase = true) } ||
        amountLabel(transaction.type, transaction.amountCents).contains(q, ignoreCase = true) ||
        Money(transaction.amountCents).formatPlain().contains(q, ignoreCase = true)
}

@Composable
internal fun HomeSummaryMetric(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            value,
            color = valueColor,
            style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = "tnum"),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HomeTransactionGroupHeader(
    group: TransactionDayGroup,
    amountsHidden: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${group.date.monthValue}月${group.date.dayOfMonth}日 ${weekdayLabel(group.date)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row {
            Text(
                "支 ${homeGroupAmountLabel(group.expenseCents, amountsHidden)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "收 ${homeGroupAmountLabel(group.incomeCents, amountsHidden)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HomeTransactionItem(
    item: TransactionWithDetails,
    amountsHidden: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val transaction = item.transaction
    val icon = item.category?.let { categoryIcon(it.iconName) } ?: transactionIcon(transaction.type)
    val amountColor = if (transaction.type == TransactionType.INCOME) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .ledgerPressCombinedClickable(
                    pressedScale = 0.99f,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    homeTransactionTitle(item),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    homeTransactionSubtitle(item),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = plainAmountLabel(transaction.type, transaction.amountCents, amountsHidden),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFeatureSettings = "tnum",
                    fontSize = 18.sp
                ),
                color = amountColor,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun HomeEmptyTransactionList(text: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 28.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun homeGroupAmountLabel(cents: Long, hidden: Boolean): String {
    return if (hidden) "****" else Money(cents).formatPlain()
}

private fun homeTransactionTitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    if (transaction.type != TransactionType.EXPENSE && transaction.type != TransactionType.INCOME) {
        return transactionTitle(item)
    }
    val title = item.category?.name ?: transactionTitle(item)
    val detail = listOf(transaction.merchant, transaction.note)
        .firstOrNull { it.isNotBlank() && !it.equals(title, ignoreCase = true) }
    return listOf(title, detail)
        .filterNotNull()
        .joinToString(" · ")
}

private fun homeTransactionSubtitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    val time = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(transaction.occurredAt))
    val accountName = when (transaction.type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> item.account?.name.orEmpty()
        TransactionType.TRANSFER -> {
            val from = item.fromAccount?.name.orEmpty()
            val to = item.toAccount?.name.orEmpty()
            listOf(from, to).filter { it.isNotBlank() }.joinToString(" -> ")
        }
        TransactionType.BALANCE_ADJUSTMENT -> item.account?.name.orEmpty()
    }
    return listOf(accountName, time)
        .filter { it.isNotBlank() }
        .joinToString(" · ")
        .ifBlank { transactionSubtitle(item) }
}

@Composable
internal fun HomeSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .ledgerPressClickable(onClick = onSearchClick),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "搜索",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            "搜索备注、分类、金额...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}
