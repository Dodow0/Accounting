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


internal enum class StatsViewMode(val label: String) {
    Category("分类"),
    Trend("趋势"),
    Calendar("日历"),
    Flow("流水")
}

internal enum class StatsInitialViewMode {
    Category,
    Trend,
    Calendar,
    Flow
}

private data class StatsCategoryAggregate(
    val categoryId: Long?,
    val type: TransactionType,
    val name: String,
    val amountCents: Long,
    val count: Int,
    val colorArgb: Long,
    val iconName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatsScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    amountsHidden: Boolean = false,
    initialAccountFilterId: Long? = null,
    onInitialAccountFilterConsumed: () -> Unit = {},
    initialViewMode: StatsInitialViewMode? = null,
    onInitialViewModeConsumed: () -> Unit = {}
) {
    var viewMode by remember { mutableStateOf(StatsViewMode.Category) }
    var filters by remember { mutableStateOf(StatsLocalFilters()) }
    var filterSheetOpen by remember { mutableStateOf(false) }
    var selectedCategoryDetail by remember { mutableStateOf<StatsCategoryAggregate?>(null) }

    LaunchedEffect(initialAccountFilterId) {
        val accountId = initialAccountFilterId ?: return@LaunchedEffect
        filters = StatsLocalFilters(accountIds = setOf(accountId))
        viewMode = StatsViewMode.Flow
        onInitialAccountFilterConsumed()
    }

    LaunchedEffect(initialViewMode) {
        val nextMode = initialViewMode ?: return@LaunchedEffect
        viewMode = nextMode.toStatsViewMode()
        onInitialViewModeConsumed()
    }

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    val filteredTransactions = remember(uiState.periodTransactions, filters) {
        uiState.periodTransactions.filterByStatsFilters(filters)
    }
    val calendarTransactions = remember(uiState.calendarMonthTransactions, filters) {
        uiState.calendarMonthTransactions.filterByStatsFilters(filters.copy(dateStart = null, dateEnd = null))
    }
    val summary = remember(filteredTransactions) { filteredTransactions.toStatsSummary() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            StatsPeriodControls(uiState, viewModel)
        }
        item {
            StatsFilterHeader(
                uiState = uiState,
                filters = filters,
                onOpenFilter = { filterSheetOpen = true },
                onFiltersChange = { filters = it },
                onClearFilters = { filters = StatsLocalFilters() }
            )
        }
        item {
            StatsSummaryCard(
                periodLabel = uiState.summary?.periodLabel.orEmpty().ifBlank { "当前周期" },
                summary = summary,
                amountsHidden = amountsHidden
            )
        }
        item {
            StatsViewModeTabs(
                selectedMode = viewMode,
                onSelected = { viewMode = it }
            )
        }
        when (viewMode) {
            StatsViewMode.Category -> statsCategoryView(
                transactions = filteredTransactions,
                filters = filters,
                amountsHidden = amountsHidden,
                onCategorySelected = { selectedCategoryDetail = it }
            )
            StatsViewMode.Trend -> item {
                StatsTrendView(
                    transactions = filteredTransactions,
                    trendRows = if (filters.activeCount == 0) uiState.trendBuckets else emptyList(),
                    rangeStart = filters.dateStart ?: localDateFromMillis(uiState.statsRangeStartMillis),
                    rangeEnd = filters.dateEnd ?: localDateFromMillis(uiState.statsRangeEndMillis).minusDays(1),
                    amountsHidden = amountsHidden
                )
            }
            StatsViewMode.Calendar -> item {
                val calendarMonthStart = localDateFromMillis(uiState.calendarMonthStartMillis)
                val calendarSelectedDate = localDateFromMillis(uiState.calendarSelectedDateMillis)
                StatsCalendarView(
                    transactions = calendarTransactions,
                    monthStart = calendarMonthStart,
                    selectedDate = calendarSelectedDate,
                    onPreviousMonth = { viewModel.moveCalendarMonth(-1) },
                    onNextMonth = { viewModel.moveCalendarMonth(1) },
                    onSelectedDate = { date ->
                        val deltaMonths = ChronoUnit.MONTHS.between(
                            YearMonth.from(calendarMonthStart),
                            YearMonth.from(date)
                        ).toInt()
                        if (deltaMonths != 0) {
                            viewModel.moveCalendarMonth(deltaMonths)
                        }
                        viewModel.selectCalendarDate(localDateStartMillis(date))
                    },
                    onEditTransaction = onEditTransaction,
                    amountsHidden = amountsHidden
                )
            }
            StatsViewMode.Flow -> transactionDayGroups(
                transactions = filteredTransactions,
                onEditTransaction = onEditTransaction,
                onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
                emptyText = "没有符合条件的流水",
                showInlineActions = false,
                onLongPress = onEditTransaction,
                amountsHidden = amountsHidden
            )
        }
    }

    if (filterSheetOpen) {
        StatsFilterSheet(
            uiState = uiState,
            initialFilters = filters,
            onDismiss = { filterSheetOpen = false },
            onApply = { nextFilters, rangeMode, startDate, endDate ->
                filters = nextFilters
                if (rangeMode == StatsRangeMode.CUSTOM) {
                    viewModel.setStatsCustomRange(
                        startMillis = localDateStartMillis(startDate),
                        endMillis = localDateStartMillis(endDate)
                    )
                } else {
                    viewModel.setStatsRangeMode(rangeMode)
                }
                filterSheetOpen = false
            }
        )
    }

    selectedCategoryDetail?.let { row ->
        val detailTransactions = remember(filteredTransactions, row) {
            filteredTransactions.filter { item ->
                val activeCategoryId = item.category?.takeIf { it.deletedAt == null }?.id
                item.transaction.type == row.type && activeCategoryId == row.categoryId
            }
        }
        val totalCents = remember(filteredTransactions, row.type) {
            filteredTransactions
                .filter { it.transaction.type == row.type }
                .sumOf { it.transaction.amountCents }
        }
        StatsCategoryDetailSheet(
            row = row,
            totalCents = totalCents,
            transactions = detailTransactions,
            amountsHidden = amountsHidden,
            onDismiss = { selectedCategoryDetail = null },
            onViewFlow = {
                selectedCategoryDetail = null
                filters = if (row.categoryId == null) {
                    filters.copy(type = row.type)
                } else {
                    filters.copy(type = row.type, categoryIds = filters.categoryIds + row.categoryId)
                }
                viewMode = StatsViewMode.Flow
            },
            onEditTransaction = onEditTransaction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
internal fun StatsPeriodControls(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        StatsRangeSelector(
            selected = uiState.statsRangeMode,
            onSelected = viewModel::setStatsRangeMode
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.moveStatsPeriod(-1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "上一页")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    uiState.summary?.periodLabel.orEmpty().ifBlank { "当前周期" },
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = viewModel::resetStatsPeriod) {
                    Text("回到当前")
                }
            }
            IconButton(onClick = { viewModel.moveStatsPeriod(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "下一页")
            }
        }
    }
}

@Composable
private fun StatsSummaryCard(
    periodLabel: String,
    summary: StatsTotals,
    amountsHidden: Boolean = false
) {
    LedgerCard {
        SectionHeader(periodLabel, "收支总览")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricTile(
                label = "支出",
                cents = summary.expenseCents,
                color = MaterialTheme.colorScheme.onSurface,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
            StatMetricTile(
                label = "收入",
                cents = summary.incomeCents,
                color = MaterialTheme.colorScheme.secondary,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricTile(
                label = "结余",
                cents = summary.incomeCents - summary.expenseCents,
                color = MaterialTheme.colorScheme.primary,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
            StatMetricTile(
                label = "最大支出",
                cents = summary.maxExpenseCents,
                color = MaterialTheme.colorScheme.tertiary,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            "转账和余额校正不进入收支统计。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatsViewModeTabs(
    selectedMode: StatsViewMode,
    onSelected: (StatsViewMode) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StatsViewMode.entries.forEach { mode ->
                val selected = selectedMode == mode
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .ledgerPressClickable(onClick = { onSelected(mode) }),
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (selected) BorderStroke(1.dp, LedgerDivider) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            mode.label,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.statsCategoryView(
    transactions: List<TransactionWithDetails>,
    filters: StatsLocalFilters,
    amountsHidden: Boolean,
    onCategorySelected: (StatsCategoryAggregate) -> Unit
) {
    val categoryType = if (filters.type == TransactionType.INCOME) TransactionType.INCOME else TransactionType.EXPENSE
    val rows = transactions
        .filter { it.transaction.type == categoryType }
        .groupBy { it.category?.takeIf { category -> category.deletedAt == null }?.id }
        .map { (categoryId, items) ->
            val category = items.firstOrNull()?.category?.takeIf { it.deletedAt == null }
            StatsCategoryAggregate(
                categoryId = categoryId,
                type = categoryType,
                name = category?.name ?: "未分类",
                amountCents = items.sumOf { it.transaction.amountCents },
                count = items.size,
                colorArgb = category?.colorArgb ?: 0xFF5C7C8A,
                iconName = category?.iconName ?: "category"
            )
        }
        .filter { it.amountCents > 0 }
        .sortedWith(compareByDescending<StatsCategoryAggregate> { it.amountCents }.thenByDescending { it.count })
    val total = rows.sumOf { it.amountCents }

    item {
        LedgerCard {
            SectionHeader(if (categoryType == TransactionType.INCOME) "收入排行" else "支出排行", "${rows.size} 类")
            if (rows.isEmpty()) {
                Text("当前范围暂无分类数据", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                rows.forEach { row ->
                    StatsCategoryRankRow(
                        row = row,
                        totalCents = total,
                        amountsHidden = amountsHidden,
                        onClick = { onCategorySelected(row) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsCategoryRankRow(
    row: StatsCategoryAggregate,
    totalCents: Long,
    amountsHidden: Boolean,
    onClick: () -> Unit
) {
    val progress = if (totalCents > 0) row.amountCents.toFloat() / totalCents.toFloat() else 0f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .ledgerPressClickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(15.dp),
                color = Color(row.colorArgb).copy(alpha = 0.10f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(row.iconName), contentDescription = null, tint = Color(row.colorArgb), modifier = Modifier.size(21.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(row.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${row.count} 笔 · 占比 ${categoryPercent(row.amountCents, totalCents)}%", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            Text(privacyAmountLabel(row.amountCents, amountsHidden), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = Color(row.colorArgb),
            trackColor = Color(row.colorArgb).copy(alpha = 0.10f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsCategoryDetailSheet(
    row: StatsCategoryAggregate,
    totalCents: Long,
    transactions: List<TransactionWithDetails>,
    amountsHidden: Boolean,
    onDismiss: () -> Unit,
    onViewFlow: () -> Unit,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
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
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 42.dp, height = 4.dp),
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            ) {}
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(row.colorArgb).copy(alpha = 0.10f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            categoryIcon(row.iconName),
                            contentDescription = null,
                            tint = Color(row.colorArgb),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(row.name, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                    "${transactionLabel(row.type)} · ${row.count} 笔 · 占比 ${categoryPercent(row.amountCents, totalCents)}%",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    privacyAmountLabel(row.amountCents, amountsHidden),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricTile(
                    label = "金额",
                    cents = row.amountCents,
                    color = MaterialTheme.colorScheme.onSurface,
                    amountsHidden = amountsHidden,
                    modifier = Modifier.weight(1f)
                )
                StatMetricTile(
                    label = "占比",
                    value = "${categoryPercent(row.amountCents, totalCents)}%",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            LedgerActionButton(
                label = "查看流水",
                icon = Icons.AutoMirrored.Filled.ReceiptLong,
                onClick = onViewFlow,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
            )
            SectionHeader("最近流水", "${transactions.size} 条")
            LazyColumn(
                modifier = Modifier.heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                transactionDayGroups(
                    transactions = transactions.take(8),
                    onEditTransaction = onEditTransaction,
                    onDeleteTransaction = {},
                    emptyText = "该分类暂无流水",
                    showInlineActions = false,
                    onLongPress = onEditTransaction,
                    amountsHidden = amountsHidden
                )
            }
        }
    }
}

@Composable
private fun StatsCalendarView(
    transactions: List<TransactionWithDetails>,
    monthStart: LocalDate,
    selectedDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    amountsHidden: Boolean = false
) {
    val selectedTransactions = transactions.filter { localDateFromMillis(it.transaction.occurredAt) == selectedDate }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "上月")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${monthStart.year}年${monthStart.monthValue}月", style = MaterialTheme.typography.titleMedium)
                Text("日历视图", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onNextMonth) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "下月")
            }
        }
        CalendarMonthGrid(
            monthStart = monthStart,
            selectedDate = selectedDate,
            transactions = transactions,
            recurringRules = emptyList(),
            amountsHidden = amountsHidden,
            onSelected = onSelectedDate
        )
        SectionHeader("${selectedDate.monthValue}月${selectedDate.dayOfMonth}日 ${weekdayLabel(selectedDate)}", "${selectedTransactions.size} 条")
        LazyColumn(
            modifier = Modifier.heightIn(max = 420.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            transactionDayGroups(
                transactions = selectedTransactions,
                onEditTransaction = onEditTransaction,
                onDeleteTransaction = {},
                emptyText = "这天还没有记账",
                showInlineActions = false,
                onLongPress = onEditTransaction,
                amountsHidden = amountsHidden
            )
        }
    }
}

@Composable
internal fun StatMetricTile(
    label: String,
    cents: Long,
    color: Color,
    amountsHidden: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(76.dp),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            Text(
                privacyAmountLabel(cents, amountsHidden),
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatMetricTile(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(76.dp),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            Text(
                value,
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun StatsCategoryDetailScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    category: CategorySummary,
    onBack: () -> Unit,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    val transactions = uiState.periodTransactions.filter { item ->
        val activeCategoryId = item.category?.takeIf { it.deletedAt == null }?.id
        item.transaction.type == TransactionType.EXPENSE && activeCategoryId == category.categoryId
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .ledgerPressClickable(onClick = onBack),
                    shape = LedgerCardShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(category.categoryName ?: "未分类", style = MaterialTheme.typography.titleSmall)
                    Text(
                        Money(category.amountCents).format(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        item {
            SectionHeader("分类明细", "${transactions.size} 条")
        }
        transactionDayGroups(
            transactions = transactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "该分类暂无流水",
            showInlineActions = false,
            onLongPress = { actionTarget = it }
        )
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
private fun StatsRangeSelector(
    selected: StatsRangeMode,
    onSelected: (StatsRangeMode) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                StatsRangeMode.WEEK to "周",
                StatsRangeMode.MONTH to "月",
                StatsRangeMode.YEAR to "年",
                StatsRangeMode.CUSTOM to "自定"
            ).forEach { (mode, label) ->
                val isSelected = selected == mode
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .ledgerPressClickable(onClick = { onSelected(mode) }),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (isSelected) BorderStroke(1.dp, LedgerDivider) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            label,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun StatsDateSelectRow(
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
                statsDateLabelWithYear(date),
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
internal fun StatsDateWheelPicker(
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

internal enum class StatsRangeDateField {
    Start,
    End
}

internal fun statsDateLabel(date: LocalDate): String {
    return "${date.monthValue}月${date.dayOfMonth}日"
}

private data class StatsTotals(
    val expenseCents: Long,
    val incomeCents: Long,
    val maxExpenseCents: Long
)

private fun List<TransactionWithDetails>.toStatsSummary(): StatsTotals {
    val expenses = filter { it.transaction.type == TransactionType.EXPENSE }
    return StatsTotals(
        expenseCents = expenses.sumOf { it.transaction.amountCents },
        incomeCents = filter { it.transaction.type == TransactionType.INCOME }.sumOf { it.transaction.amountCents },
        maxExpenseCents = expenses.maxOfOrNull { it.transaction.amountCents } ?: 0
    )
}

internal fun statsDateRangeLabel(start: LocalDate, end: LocalDate): String {
    val safeEnd = if (end.isBefore(start)) start else end
    return when {
        start == safeEnd -> statsDateLabelWithYear(start)
        start.year == safeEnd.year -> "${start.monthValue}月${start.dayOfMonth}日 - ${safeEnd.monthValue}月${safeEnd.dayOfMonth}日"
        else -> "${statsDateLabelWithYear(start)} - ${statsDateLabelWithYear(safeEnd)}"
    }
}

internal fun statsDateLabelWithYear(date: LocalDate): String {
    return "${date.year}年${date.monthValue}月${date.dayOfMonth}日"
}

