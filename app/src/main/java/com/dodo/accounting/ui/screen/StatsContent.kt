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


private enum class StatsViewMode(val label: String) {
    Category("分类"),
    Trend("趋势"),
    Calendar("日历"),
    Flow("流水")
}

private enum class TrendChartMode(val label: String) {
    Expense("支出"),
    Income("收入"),
    Both("收支")
}

private enum class TrendBucketGranularity {
    Day,
    Week,
    Month
}

private data class StatsLocalFilters(
    val query: String = "",
    val type: TransactionType? = null,
    val accountIds: Set<Long> = emptySet(),
    val categoryIds: Set<Long> = emptySet(),
    val tagIds: Set<Long> = emptySet(),
    val minAmount: String = "",
    val maxAmount: String = "",
    val dateStart: LocalDate? = null,
    val dateEnd: LocalDate? = null
) {
    val activeCount: Int
        get() = listOf(
            query.takeIf { it.isNotBlank() },
            type,
            accountIds.takeIf { it.isNotEmpty() },
            categoryIds.takeIf { it.isNotEmpty() },
            tagIds.takeIf { it.isNotEmpty() },
            Unit.takeIf { minAmount.isNotBlank() || maxAmount.isNotBlank() },
            Unit.takeIf { dateStart != null || dateEnd != null }
        ).count { it != null }
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

private data class StatsTrendBucket(
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val expenseCents: Long,
    val incomeCents: Long,
    val count: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatsScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    amountsHidden: Boolean = false,
    initialAccountFilterId: Long? = null,
    onInitialAccountFilterConsumed: () -> Unit = {}
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

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    val filteredTransactions = remember(uiState.periodTransactions, filters) {
        uiState.periodTransactions.filterByStatsFilters(filters)
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
                    rangeStart = filters.dateStart ?: localDateFromMillis(uiState.statsRangeStartMillis),
                    rangeEnd = filters.dateEnd ?: localDateFromMillis(uiState.statsRangeEndMillis).minusDays(1),
                    amountsHidden = amountsHidden,
                    onBucketSelected = { bucket ->
                        filters = filters.copy(dateStart = bucket.startDate, dateEnd = bucket.endDate)
                        viewMode = StatsViewMode.Flow
                    }
                )
            }
            StatsViewMode.Calendar -> item {
                StatsCalendarView(
                    transactions = filteredTransactions,
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

@Composable
private fun StatsFilterHeader(
    uiState: AccountingUiState,
    filters: StatsLocalFilters,
    onOpenFilter: () -> Unit,
    onFiltersChange: (StatsLocalFilters) -> Unit,
    onClearFilters: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LedgerActionButton(
                label = if (filters.activeCount > 0) "筛选 ${filters.activeCount}" else "筛选",
                icon = Icons.Default.Search,
                onClick = onOpenFilter,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
            )
            if (filters.activeCount > 0) {
                TextButton(onClick = onClearFilters) {
                    Text("清空")
                }
            }
        }
        if (filters.activeCount > 0) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.query.takeIf { it.isNotBlank() }?.let {
                    StatsFilterChip(
                        label = "关键词 $it",
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(query = "")) }
                    )
                }
                filters.type?.let {
                    StatsFilterChip(
                        label = transactionLabel(it),
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(type = null)) }
                    )
                }
                filters.accountIds.forEach { accountId ->
                    StatsFilterChip(
                        label = "账户 ${uiState.accountFilterLabel(accountId)}",
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(accountIds = filters.accountIds - accountId)) }
                    )
                }
                filters.categoryIds.forEach { categoryId ->
                    StatsFilterChip(
                        label = "分类 ${uiState.categoryFilterLabel(categoryId)}",
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(categoryIds = filters.categoryIds - categoryId)) }
                    )
                }
                filters.tagIds.forEach { tagId ->
                    StatsFilterChip(
                        label = "标签 ${uiState.tagFilterLabel(tagId)}",
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(tagIds = filters.tagIds - tagId)) }
                    )
                }
                if (filters.minAmount.isNotBlank() || filters.maxAmount.isNotBlank()) {
                    StatsFilterChip(
                        label = amountRangeFilterLabel(filters.minAmount, filters.maxAmount),
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(minAmount = "", maxAmount = "")) }
                    )
                }
                if (filters.dateStart != null || filters.dateEnd != null) {
                    StatsFilterChip(
                        label = dateRangeFilterLabel(filters.dateStart, filters.dateEnd),
                        onClick = onOpenFilter,
                        onRemove = { onFiltersChange(filters.copy(dateStart = null, dateEnd = null)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsFilterChip(
    label: String,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.ledgerPressClickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 7.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                Icons.Default.Close,
                contentDescription = "移除筛选",
                modifier = Modifier
                    .size(15.dp)
                    .ledgerPressClickable(pressedScale = 0.88f, onClick = onRemove),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsFilterSheet(
    uiState: AccountingUiState,
    initialFilters: StatsLocalFilters,
    onDismiss: () -> Unit,
    onApply: (StatsLocalFilters, StatsRangeMode, LocalDate, LocalDate) -> Unit
) {
    var query by remember(initialFilters) { mutableStateOf(initialFilters.query) }
    var type by remember(initialFilters) { mutableStateOf(initialFilters.type) }
    var accountIds by remember(initialFilters) { mutableStateOf(initialFilters.accountIds) }
    var categoryIds by remember(initialFilters) { mutableStateOf(initialFilters.categoryIds) }
    var tagIds by remember(initialFilters) { mutableStateOf(initialFilters.tagIds) }
    var tagSearch by remember(initialFilters) { mutableStateOf("") }
    var minAmount by remember(initialFilters) { mutableStateOf(initialFilters.minAmount) }
    var maxAmount by remember(initialFilters) { mutableStateOf(initialFilters.maxAmount) }
    var rangeMode by remember(uiState.statsRangeMode) { mutableStateOf(uiState.statsRangeMode) }
    var editingDate by remember { mutableStateOf<StatsRangeDateField?>(null) }
    var startDate by remember(uiState.statsRangeStartMillis) {
        mutableStateOf(localDateFromMillis(uiState.statsRangeStartMillis))
    }
    var endDate by remember(uiState.statsRangeEndMillis) {
        mutableStateOf(localDateFromMillis(uiState.statsRangeEndMillis).minusDays(1))
    }
    val effectiveEndDate = if (endDate.isBefore(startDate)) startDate else endDate
    val previewFilters = StatsLocalFilters(
        query = query,
        type = type,
        accountIds = accountIds,
        categoryIds = categoryIds,
        tagIds = tagIds,
        minAmount = minAmount,
        maxAmount = maxAmount
    )
    val canPreviewCount = rangeMode == uiState.statsRangeMode &&
        startDate == localDateFromMillis(uiState.statsRangeStartMillis) &&
        effectiveEndDate == localDateFromMillis(uiState.statsRangeEndMillis).minusDays(1)
    val applyLabel = if (canPreviewCount) {
        "查看 ${uiState.periodTransactions.filterByStatsFilters(previewFilters).size} 条结果"
    } else {
        "查看结果"
    }
    val accountItems = remember(uiState.activeAccounts, accountIds) {
        uiState.activeAccounts
            .map { it.id to it.name }
            .sortedWith(
                compareByDescending<Pair<Long, String>> { if (it.first in accountIds) 1 else 0 }
                    .thenBy { it.second }
            )
    }
    val categoriesForType = uiState.statsFilterCategories(type)
    val categoryUsage = remember(uiState.periodTransactions, type) {
        uiState.periodTransactions
            .filter { type == null || it.transaction.type == type }
            .mapNotNull { it.transaction.categoryId }
            .groupingBy { it }
            .eachCount()
    }
    val categoryItems = remember(categoriesForType, categoryIds, categoryUsage) {
        categoriesForType
            .map { it.id to it.name }
            .sortedWith(
                compareByDescending<Pair<Long, String>> { if (it.first in categoryIds) 1 else 0 }
                    .thenByDescending { categoryUsage[it.first] ?: 0 }
                    .thenBy { it.second }
            )
    }
    val tagUsage = remember(uiState.periodTransactions) {
        uiState.periodTransactions
            .flatMap { it.tags }
            .groupingBy { it.id }
            .eachCount()
    }
    val tagItems = remember(uiState.tags, tagIds, tagSearch, tagUsage) {
        val keyword = tagSearch.trim()
        uiState.tags
            .asSequence()
            .filter { keyword.isBlank() || it.name.contains(keyword, ignoreCase = true) }
            .map { it.id to it.name }
            .sortedWith(
                compareByDescending<Pair<Long, String>> { if (it.first in tagIds) 1 else 0 }
                    .thenByDescending { tagUsage[it.first] ?: 0 }
                    .thenBy { it.second }
            )
            .toList()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StatsSheetHandle()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("筛选", style = MaterialTheme.typography.titleLarge)
                TextButton(
                    onClick = {
                        query = ""
                        type = null
                        accountIds = emptySet()
                        categoryIds = emptySet()
                        tagIds = emptySet()
                        tagSearch = ""
                        minAmount = ""
                        maxAmount = ""
                    }
                ) {
                    Text("重置")
                }
            }
            StatsSheetSection("时间") {
                StatsChoiceRow(
                    options = listOf(
                        StatsRangeMode.WEEK to "本周",
                        StatsRangeMode.MONTH to "本月",
                        StatsRangeMode.YEAR to "本年",
                        StatsRangeMode.CUSTOM to "自定义"
                    ),
                    selected = rangeMode,
                    onSelected = {
                        rangeMode = it
                        if (it != StatsRangeMode.CUSTOM) editingDate = null
                    }
                )
                if (rangeMode == StatsRangeMode.CUSTOM) {
                    when (val field = editingDate) {
                        null -> {
                            StatsDateSelectRow(
                                label = "开始",
                                date = startDate,
                                onClick = { editingDate = StatsRangeDateField.Start }
                            )
                            StatsDateSelectRow(
                                label = "结束",
                                date = effectiveEndDate,
                                onClick = { editingDate = StatsRangeDateField.End }
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                LedgerChoiceChip(
                                    selected = false,
                                    label = "最近7天",
                                    onClick = {
                                        endDate = LocalDate.now()
                                        startDate = endDate.minusDays(6)
                                    }
                                )
                                LedgerChoiceChip(
                                    selected = false,
                                    label = "最近30天",
                                    onClick = {
                                        endDate = LocalDate.now()
                                        startDate = endDate.minusDays(29)
                                    }
                                )
                                LedgerChoiceChip(
                                    selected = false,
                                    label = "本月",
                                    onClick = {
                                        val today = LocalDate.now()
                                        startDate = today.withDayOfMonth(1)
                                        endDate = today
                                    }
                                )
                            }
                        }
                        else -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    if (field == StatsRangeDateField.Start) "选择开始日期" else "选择结束日期",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                TextButton(onClick = { editingDate = null }) {
                                    Text("完成")
                                }
                            }
                            StatsDateWheelPicker(
                                date = if (field == StatsRangeDateField.Start) startDate else effectiveEndDate,
                                onDateChange = { nextDate ->
                                    if (field == StatsRangeDateField.Start) {
                                        startDate = nextDate
                                        if (endDate.isBefore(nextDate)) endDate = nextDate
                                    } else {
                                        endDate = if (nextDate.isBefore(startDate)) startDate else nextDate
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        uiState.statsRangeLabel.ifBlank { "当前范围" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            MinimalInputLine(
                value = query,
                onValueChange = { query = it },
                placeholder = "关键词：备注、分类、账户、标签",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            StatsSheetSection("收支类型") {
                StatsChoiceRow(
                    options = listOf(null to "全部", TransactionType.EXPENSE to "支出", TransactionType.INCOME to "收入", TransactionType.TRANSFER to "转账"),
                    selected = type,
                    onSelected = { selectedType ->
                        type = selectedType
                        val availableCategoryIds = uiState.statsFilterCategories(selectedType).map { it.id }.toSet()
                        categoryIds = categoryIds.intersect(availableCategoryIds)
                    }
                )
            }
            StatsSheetSection("账户") {
                StatsMultiSelectChipRow(
                    allLabel = "全部账户",
                    items = accountItems,
                    selectedIds = accountIds,
                    onSelectedIdsChange = { accountIds = it }
                )
            }
            StatsSheetSection("分类") {
                StatsMultiSelectChipRow(
                    allLabel = "全部分类",
                    items = categoryItems,
                    selectedIds = categoryIds,
                    onSelectedIdsChange = { categoryIds = it }
                )
            }
            StatsSheetSection("标签") {
                MinimalInputLine(
                    value = tagSearch,
                    onValueChange = { tagSearch = it },
                    placeholder = "搜索标签",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                StatsMultiSelectChipRow(
                    allLabel = "全部标签",
                    items = tagItems,
                    selectedIds = tagIds,
                    onSelectedIdsChange = { tagIds = it }
                )
                if (tagSearch.isNotBlank() && tagItems.isEmpty()) {
                    Text(
                        "没有匹配的标签",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MinimalInputLine(
                    value = minAmount,
                    onValueChange = { minAmount = it },
                    placeholder = "最小金额",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                MinimalInputLine(
                    value = maxAmount,
                    onValueChange = { maxAmount = it },
                    placeholder = "最大金额",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LedgerActionButton(
                    label = "清空",
                    onClick = { onApply(StatsLocalFilters(), rangeMode, startDate, effectiveEndDate) },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    borderColor = LedgerDivider
                )
                LedgerActionButton(
                    label = applyLabel,
                    onClick = {
                        onApply(
                            previewFilters,
                            rangeMode,
                            startDate,
                            effectiveEndDate
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatsSheetHandle() {
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
}

@Composable
private fun StatsSheetSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
private fun <T> StatsChoiceRow(
    options: List<Pair<T, String>>,
    selected: T,
    onSelected: (T) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (value, label) ->
            LedgerChoiceChip(
                selected = selected == value,
                label = label,
                onClick = { onSelected(value) }
            )
        }
    }
}

@Composable
private fun StatsMultiSelectChipRow(
    allLabel: String,
    items: List<Pair<Long, String>>,
    selectedIds: Set<Long>,
    onSelectedIdsChange: (Set<Long>) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LedgerChoiceChip(
            selected = selectedIds.isEmpty(),
            label = allLabel,
            onClick = { onSelectedIdsChange(emptySet()) }
        )
        items.forEach { (id, label) ->
            LedgerChoiceChip(
                selected = id in selectedIds,
                label = label,
                onClick = {
                    onSelectedIdsChange(
                        if (id in selectedIds) selectedIds - id else selectedIds + id
                    )
                }
            )
        }
    }
}

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "上一期")
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
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "下一期")
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
private fun StatsTrendView(
    transactions: List<TransactionWithDetails>,
    rangeStart: LocalDate,
    rangeEnd: LocalDate,
    amountsHidden: Boolean,
    onBucketSelected: (StatsTrendBucket) -> Unit
) {
    var chartMode by remember { mutableStateOf(TrendChartMode.Both) }
    val safeEnd = if (rangeEnd.isBefore(rangeStart)) rangeStart else rangeEnd
    val buckets = remember(transactions, rangeStart, safeEnd) {
        transactions.toTrendBuckets(rangeStart, safeEnd)
    }
    var selectedBucketIndex by remember(buckets) { mutableStateOf<Int?>(null) }
    val selectedBucket = selectedBucketIndex?.let { buckets.getOrNull(it) }
    val selectedTotal = when (chartMode) {
        TrendChartMode.Expense -> buckets.sumOf { it.expenseCents }
        TrendChartMode.Income -> buckets.sumOf { it.incomeCents }
        TrendChartMode.Both -> buckets.sumOf { it.incomeCents - it.expenseCents }
    }
    val dailyExpense = remember(transactions, rangeStart, safeEnd) {
        val days = ChronoUnit.DAYS.between(rangeStart, safeEnd).coerceAtLeast(0) + 1
        transactions
            .filter { it.transaction.type == TransactionType.EXPENSE }
            .sumOf { it.transaction.amountCents } / days
    }

    LedgerCard {
        SectionHeader("收支趋势", trendRangeLabel(rangeStart, safeEnd))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TrendChartMode.entries.forEach { mode ->
                LedgerChoiceChip(
                    selected = chartMode == mode,
                    label = mode.label,
                    onClick = { chartMode = mode }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricTile(
                label = when (chartMode) {
                    TrendChartMode.Expense -> "总支出"
                    TrendChartMode.Income -> "总收入"
                    TrendChartMode.Both -> "净结余"
                },
                cents = selectedTotal,
                color = if (chartMode == TrendChartMode.Income) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
            StatMetricTile(
                label = "日均支出",
                cents = dailyExpense,
                color = MaterialTheme.colorScheme.onSurface,
                amountsHidden = amountsHidden,
                modifier = Modifier.weight(1f)
            )
        }
        if (buckets.isEmpty() || buckets.all { it.expenseCents == 0L && it.incomeCents == 0L }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("当前范围暂无趋势数据", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            TrendLineChart(
                buckets = buckets,
                mode = chartMode,
                amountsHidden = amountsHidden,
                selectedBucketIndex = selectedBucketIndex,
                onBucketFocused = { selectedBucketIndex = it }
            )
            selectedBucket?.let { bucket ->
                TrendBucketSummary(
                    bucket = bucket,
                    amountsHidden = amountsHidden,
                    onViewFlow = { onBucketSelected(bucket) }
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                buckets.forEachIndexed { index, bucket ->
                    LedgerChoiceChip(
                        selected = selectedBucketIndex == index,
                        label = bucket.label,
                        onClick = { selectedBucketIndex = index }
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendLineChart(
    buckets: List<StatsTrendBucket>,
    mode: TrendChartMode,
    amountsHidden: Boolean,
    selectedBucketIndex: Int?,
    onBucketFocused: (Int) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val expenseColor = MaterialTheme.colorScheme.onSurfaceVariant
    val incomeColor = MaterialTheme.colorScheme.secondary
    val selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.36f)
    val values = remember(buckets, mode) {
        when (mode) {
            TrendChartMode.Expense -> listOf(buckets.map { it.expenseCents })
            TrendChartMode.Income -> listOf(buckets.map { it.incomeCents })
            TrendChartMode.Both -> listOf(
                buckets.map { it.expenseCents },
                buckets.map { it.incomeCents }
            )
        }
    }
    val maxCents = values.flatten().maxOrNull()?.coerceAtLeast(1L) ?: 1L

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            when (mode) {
                TrendChartMode.Expense -> TrendLegend("支出", expenseColor)
                TrendChartMode.Income -> TrendLegend("收入", incomeColor)
                TrendChartMode.Both -> {
                    TrendLegend("支出", expenseColor)
                    TrendLegend("收入", incomeColor)
                }
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .pointerInput(buckets) {
                    detectTapGestures { offset ->
                        if (buckets.isEmpty()) return@detectTapGestures
                        val left = 10.dp.toPx()
                        val right = size.width - 10.dp.toPx()
                        val denominator = (buckets.size - 1).coerceAtLeast(1).toFloat()
                        val index = (((offset.x - left) / (right - left).coerceAtLeast(1f)) * denominator)
                            .roundToInt()
                            .coerceIn(0, buckets.lastIndex)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onBucketFocused(index)
                    }
                }
        ) {
            val left = 10.dp.toPx()
            val right = size.width - 10.dp.toPx()
            val top = 12.dp.toPx()
            val bottom = size.height - 18.dp.toPx()
            val chartHeight = (bottom - top).coerceAtLeast(1f)
            val denominator = (buckets.size - 1).coerceAtLeast(1).toFloat()

            fun point(index: Int, value: Long): Offset {
                val x = left + (right - left) * (index.toFloat() / denominator)
                val y = bottom - chartHeight * (value.toFloat() / maxCents.toFloat())
                return Offset(x, y)
            }

            repeat(3) { line ->
                val y = top + chartHeight * (line + 1) / 4f
                drawLine(
                    color = Color(0xFFE5E7EB),
                    start = Offset(left, y),
                    end = Offset(right, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            fun drawSeries(series: List<Long>, color: Color) {
                if (series.isEmpty()) return
                series.zipWithNext().forEachIndexed { index, pair ->
                    drawLine(
                        color = color,
                        start = point(index, pair.first),
                        end = point(index + 1, pair.second),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                series.forEachIndexed { index, value ->
                    drawCircle(
                        color = color,
                        radius = 3.5.dp.toPx(),
                        center = point(index, value)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 1.6.dp.toPx(),
                        center = point(index, value)
                    )
                }
            }

            selectedBucketIndex?.takeIf { it in buckets.indices }?.let { index ->
                val x = point(index, values.firstOrNull()?.getOrNull(index) ?: 0L).x
                drawLine(
                    color = selectedIndicatorColor,
                    start = Offset(x, top),
                    end = Offset(x, bottom),
                    strokeWidth = 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            when (mode) {
                TrendChartMode.Expense -> drawSeries(buckets.map { it.expenseCents }, expenseColor)
                TrendChartMode.Income -> drawSeries(buckets.map { it.incomeCents }, incomeColor)
                TrendChartMode.Both -> {
                    drawSeries(buckets.map { it.expenseCents }, expenseColor)
                    drawSeries(buckets.map { it.incomeCents }, incomeColor)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            buckets.take(6).forEach { bucket ->
                Text(
                    bucket.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
        Text(
            if (amountsHidden) "点选时间段查看笔数，再进入流水核对" else "点选图表或时间段查看金额和笔数",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TrendBucketSummary(
    bucket: StatsTrendBucket,
    amountsHidden: Boolean,
    onViewFlow: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(bucket.label, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "${statsDateRangeLabel(bucket.startDate, bucket.endDate)} · ${bucket.count} 笔",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    privacyAmountLabel(bucket.incomeCents - bucket.expenseCents, amountsHidden),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TrendBucketMetric(
                    label = "支出",
                    value = privacyAmountLabel(bucket.expenseCents, amountsHidden),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                TrendBucketMetric(
                    label = "收入",
                    value = privacyAmountLabel(bucket.incomeCents, amountsHidden),
                    color = MaterialTheme.colorScheme.secondary,
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
        }
    }
}

@Composable
private fun TrendBucketMetric(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            Text(
                value,
                color = color,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatsCalendarView(
    transactions: List<TransactionWithDetails>,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    amountsHidden: Boolean = false
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val monthStart = selectedDate.withDayOfMonth(1)
    val selectedTransactions = transactions.filter { localDateFromMillis(it.transaction.occurredAt) == selectedDate }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { selectedDate = selectedDate.minusMonths(1).withDayOfMonth(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "上月")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${monthStart.year}年${monthStart.monthValue}月", style = MaterialTheme.typography.titleMedium)
                Text("日历视图", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { selectedDate = selectedDate.plusMonths(1).withDayOfMonth(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "下月")
            }
        }
        CalendarMonthGrid(
            monthStart = monthStart,
            selectedDate = selectedDate,
            transactions = transactions,
            recurringRules = emptyList(),
            amountsHidden = amountsHidden,
            onSelected = { selectedDate = it }
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
                emptyText = "这天还没有记录",
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
private fun StatsDateSelectRow(
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
private fun StatsDateWheelPicker(
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

private enum class StatsRangeDateField {
    Start,
    End
}

private fun statsDateLabel(date: LocalDate): String {
    return "${date.monthValue}月${date.dayOfMonth}日"
}

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

private fun AccountingUiState.accountFilterLabel(accountId: Long): String {
    return activeAccounts.firstOrNull { it.id == accountId }?.name
        ?: accounts.firstOrNull { it.account.id == accountId }?.account?.name
        ?: "已删除账户"
}

private fun AccountingUiState.categoryFilterLabel(categoryId: Long): String {
    return categories.firstOrNull { it.id == categoryId }?.name ?: "已删除分类"
}

private fun AccountingUiState.tagFilterLabel(tagId: Long): String {
    return tags.firstOrNull { it.id == tagId }?.name ?: "已删除标签"
}

private fun AccountingUiState.statsFilterCategories(type: TransactionType?): List<CategoryEntity> {
    return when (type) {
        TransactionType.EXPENSE -> expenseCategories
        TransactionType.INCOME -> incomeCategories
        else -> expenseCategories + incomeCategories
    }
}

private fun amountRangeFilterLabel(minAmount: String, maxAmount: String): String {
    return when {
        minAmount.isNotBlank() && maxAmount.isNotBlank() -> "金额 $minAmount-$maxAmount"
        minAmount.isNotBlank() -> "金额 >= $minAmount"
        maxAmount.isNotBlank() -> "金额 <= $maxAmount"
        else -> "金额范围"
    }
}

private fun dateRangeFilterLabel(start: LocalDate?, end: LocalDate?): String {
    return when {
        start != null && end != null -> "日期 ${statsDateRangeLabel(start, end)}"
        start != null -> "日期 >= ${statsDateLabelWithYear(start)}"
        end != null -> "日期 <= ${statsDateLabelWithYear(end)}"
        else -> "日期范围"
    }
}

private fun trendRangeLabel(start: LocalDate, end: LocalDate): String {
    return statsDateRangeLabel(start, end)
}

private fun statsDateRangeLabel(start: LocalDate, end: LocalDate): String {
    val safeEnd = if (end.isBefore(start)) start else end
    return when {
        start == safeEnd -> statsDateLabelWithYear(start)
        start.year == safeEnd.year -> "${start.monthValue}月${start.dayOfMonth}日 - ${safeEnd.monthValue}月${safeEnd.dayOfMonth}日"
        else -> "${statsDateLabelWithYear(start)} - ${statsDateLabelWithYear(safeEnd)}"
    }
}

private fun statsDateLabelWithYear(date: LocalDate): String {
    return "${date.year}年${date.monthValue}月${date.dayOfMonth}日"
}

private fun List<TransactionWithDetails>.toTrendBuckets(
    start: LocalDate,
    end: LocalDate
): List<StatsTrendBucket> {
    val safeEnd = if (end.isBefore(start)) start else end
    val dayCount = ChronoUnit.DAYS.between(start, safeEnd) + 1
    val granularity = when {
        dayCount <= 45 -> TrendBucketGranularity.Day
        dayCount <= 180 -> TrendBucketGranularity.Week
        else -> TrendBucketGranularity.Month
    }
    val byDate = groupBy { localDateFromMillis(it.transaction.occurredAt) }
    val buckets = mutableListOf<StatsTrendBucket>()
    var cursor = start

    while (!cursor.isAfter(safeEnd)) {
        val bucketEnd = when (granularity) {
            TrendBucketGranularity.Day -> cursor
            TrendBucketGranularity.Week -> cursor.plusDays(6)
            TrendBucketGranularity.Month -> cursor.plusMonths(1).minusDays(1)
        }.let { if (it.isAfter(safeEnd)) safeEnd else it }

        val items = generateSequence(cursor) { date ->
            date.plusDays(1).takeIf { !it.isAfter(bucketEnd) }
        }.flatMap { date -> byDate[date].orEmpty().asSequence() }.toList()

        buckets += StatsTrendBucket(
            label = trendBucketLabel(cursor, bucketEnd, granularity),
            startDate = cursor,
            endDate = bucketEnd,
            expenseCents = items
                .filter { it.transaction.type == TransactionType.EXPENSE }
                .sumOf { it.transaction.amountCents },
            incomeCents = items
                .filter { it.transaction.type == TransactionType.INCOME }
                .sumOf { it.transaction.amountCents },
            count = items.size
        )
        cursor = bucketEnd.plusDays(1)
    }

    return buckets
}

private fun trendBucketLabel(
    start: LocalDate,
    end: LocalDate,
    granularity: TrendBucketGranularity
): String {
    return when (granularity) {
        TrendBucketGranularity.Day -> statsDateLabel(start)
        TrendBucketGranularity.Week -> "${start.monthValue}/${start.dayOfMonth}-${end.monthValue}/${end.dayOfMonth}"
        TrendBucketGranularity.Month -> {
            if (start.year == LocalDate.now().year) "${start.monthValue}月" else "${start.year}年${start.monthValue}月"
        }
    }
}

private fun List<TransactionWithDetails>.filterByStatsFilters(filters: StatsLocalFilters): List<TransactionWithDetails> {
    val minCents = Money.parseMajorStrict(filters.minAmount)?.cents
    val maxCents = Money.parseMajorStrict(filters.maxAmount)?.cents
    val query = filters.query.trim()
    return filter { item ->
        val transaction = item.transaction
        val occurredDate = localDateFromMillis(transaction.occurredAt)
        val accountMatches = filters.accountIds.isEmpty() ||
            transaction.accountId in filters.accountIds ||
            transaction.fromAccountId in filters.accountIds ||
            transaction.toAccountId in filters.accountIds
        val categoryMatches = filters.categoryIds.isEmpty() || transaction.categoryId in filters.categoryIds
        val tagMatches = filters.tagIds.isEmpty() || item.tags.any { it.id in filters.tagIds }
        val typeMatches = filters.type == null || transaction.type == filters.type
        val minMatches = minCents == null || transaction.amountCents >= minCents
        val maxMatches = maxCents == null || transaction.amountCents <= maxCents
        val dateMatches = (filters.dateStart == null || !occurredDate.isBefore(filters.dateStart)) &&
            (filters.dateEnd == null || !occurredDate.isAfter(filters.dateEnd))
        val queryMatches = query.isBlank() ||
            transaction.note.contains(query, ignoreCase = true) ||
            transaction.merchant.contains(query, ignoreCase = true) ||
            item.category?.name.orEmpty().contains(query, ignoreCase = true) ||
            item.account?.name.orEmpty().contains(query, ignoreCase = true) ||
            item.fromAccount?.name.orEmpty().contains(query, ignoreCase = true) ||
            item.toAccount?.name.orEmpty().contains(query, ignoreCase = true) ||
            item.tags.any { it.name.contains(query, ignoreCase = true) }
        typeMatches && accountMatches && categoryMatches && tagMatches && minMatches && maxMatches && dateMatches && queryMatches
    }
}
