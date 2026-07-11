@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

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
import com.dodo.accounting.ui.viewmodel.StatsUiState
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


internal data class StatsLocalFilters(
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

internal fun StatsInitialViewMode.toStatsViewMode(): StatsViewMode = when (this) {
    StatsInitialViewMode.Category -> StatsViewMode.Category
    StatsInitialViewMode.Trend -> StatsViewMode.Trend
    StatsInitialViewMode.Calendar -> StatsViewMode.Calendar
    StatsInitialViewMode.Flow -> StatsViewMode.Flow
}

private fun StatsUiState.accountFilterLabel(accountId: Long): String {
    return activeAccounts.firstOrNull { it.id == accountId }?.name
        ?: accounts.firstOrNull { it.account.id == accountId }?.account?.name
        ?: "已删除账户"
}

private fun StatsUiState.categoryFilterLabel(categoryId: Long): String {
    return categories.firstOrNull { it.id == categoryId }?.name ?: "已删除分类"
}

private fun StatsUiState.tagFilterLabel(tagId: Long): String {
    return tags.firstOrNull { it.id == tagId }?.name ?: "已删除标签"
}

private fun StatsUiState.statsFilterCategories(type: TransactionType?): List<CategoryEntity> {
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

internal fun amountRangeValidationError(minAmount: String, maxAmount: String): String? {
    val min = minAmount.trim().takeIf { it.isNotBlank() }?.let { Money.parseMajorStrict(it) }
    val max = maxAmount.trim().takeIf { it.isNotBlank() }?.let { Money.parseMajorStrict(it) }
    return when {
        minAmount.isNotBlank() && min == null -> "最小金额格式不正确"
        maxAmount.isNotBlank() && max == null -> "最大金额格式不正确"
        min != null && max != null && min.cents > max.cents -> "最小金额不能大于最大金额"
        else -> null
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

internal fun List<TransactionWithDetails>.filterByStatsFilters(filters: StatsLocalFilters): List<TransactionWithDetails> {
    if (amountRangeValidationError(filters.minAmount, filters.maxAmount) != null) return emptyList()
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

@Composable
internal fun StatsFilterHeader(
    uiState: StatsUiState,
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

@Composable
internal fun StatsFilterSheet(
    uiState: StatsUiState,
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
    val amountRangeError = amountRangeValidationError(minAmount, maxAmount)
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
    val applyLabel = if (amountRangeError != null) {
        "修正金额"
    } else if (canPreviewCount) {
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
                                    label = "最近 7 天",
                                    onClick = {
                                        endDate = LocalDate.now()
                                        startDate = endDate.minusDays(6)
                                    }
                                )
                                LedgerChoiceChip(
                                    selected = false,
                                    label = "最近 30 天",
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
            amountRangeError?.let { error ->
                Text(
                    error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
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
                    modifier = Modifier.weight(1f),
                    enabled = amountRangeError == null
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
