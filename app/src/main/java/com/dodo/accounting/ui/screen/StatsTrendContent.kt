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


internal enum class TrendChartMode(val label: String) {
    Expense("支出"),
    Income("收入"),
    Both("收支")
}

internal enum class TrendBucketGranularity {
    Day,
    Week,
    Month
}

internal data class StatsTrendBucket(
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val expenseCents: Long,
    val incomeCents: Long,
    val count: Int
)

@Composable
internal fun StatsTrendView(
    transactions: List<TransactionWithDetails>,
    trendRows: List<TrendSummaryRow>,
    rangeStart: LocalDate,
    rangeEnd: LocalDate,
    amountsHidden: Boolean
) {
    var chartMode by remember { mutableStateOf(TrendChartMode.Both) }
    val safeEnd = if (rangeEnd.isBefore(rangeStart)) rangeStart else rangeEnd
    val buckets = remember(transactions, trendRows, rangeStart, safeEnd) {
        val dayCount = ChronoUnit.DAYS.between(rangeStart, safeEnd).coerceAtLeast(0) + 1
        if (dayCount <= 45 || trendRows.isEmpty()) {
            transactions.toTrendBuckets(rangeStart, safeEnd)
        } else {
            trendRows.toMonthlyTrendBuckets(rangeStart, safeEnd)
        }
    }
    var selectedBucketIndex by remember(buckets) { mutableStateOf<Int?>(null) }
    val selectedBucket = selectedBucketIndex?.let { buckets.getOrNull(it) }
    val selectedTotal = when (chartMode) {
        TrendChartMode.Expense -> buckets.sumOf { it.expenseCents }
        TrendChartMode.Income -> buckets.sumOf { it.incomeCents }
        TrendChartMode.Both -> buckets.sumOf { it.incomeCents - it.expenseCents }
    }
    val dailyExpense = remember(buckets, rangeStart, safeEnd) {
        val days = ChronoUnit.DAYS.between(rangeStart, safeEnd).coerceAtLeast(0) + 1
        buckets.sumOf { it.expenseCents } / days
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
                    amountsHidden = amountsHidden
                )
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
        val dailyAxis = buckets.size > 1 && buckets.all { it.startDate == it.endDate }
        if (dailyAxis) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                buckets.forEach { bucket ->
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(64.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            bucket.label,
                            modifier = Modifier
                                .width(64.dp)
                                .graphicsLayer {
                                    rotationZ = -45f
                                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                                },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                trendAxisBuckets(buckets).forEach { bucket ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            bucket.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Text(
            if (amountsHidden) "点选图表查看笔数" else "点选图表查看金额和笔数",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TrendBucketSummary(
    bucket: StatsTrendBucket,
    amountsHidden: Boolean
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

private fun trendRangeLabel(start: LocalDate, end: LocalDate): String {
    return statsDateRangeLabel(start, end)
}

private fun trendAxisBuckets(buckets: List<StatsTrendBucket>): List<StatsTrendBucket> {
    if (buckets.size <= 6) return buckets
    return (0 until 6).map { index ->
        val bucketIndex = (index * (buckets.lastIndex) / 5f).roundToInt()
            .coerceIn(0, buckets.lastIndex)
        buckets[bucketIndex]
    }.distinctBy { it.startDate }
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

private fun List<TrendSummaryRow>.toMonthlyTrendBuckets(
    start: LocalDate,
    end: LocalDate
): List<StatsTrendBucket> {
    val safeEnd = if (end.isBefore(start)) start else end
    val byMonth = mapNotNull { row ->
        val month = row.bucketMonth?.let { runCatching { YearMonth.parse(it) }.getOrNull() }
        month?.let { it to row }
    }.toMap()
    val buckets = mutableListOf<StatsTrendBucket>()
    var cursor = YearMonth.from(start).atDay(1)

    while (!cursor.isAfter(safeEnd)) {
        val month = YearMonth.from(cursor)
        val bucketStart = if (cursor.isBefore(start)) start else cursor
        val bucketEnd = month.atEndOfMonth().let { if (it.isAfter(safeEnd)) safeEnd else it }
        val row = byMonth[month]
        buckets += StatsTrendBucket(
            label = trendBucketLabel(bucketStart, bucketEnd, TrendBucketGranularity.Month),
            startDate = bucketStart,
            endDate = bucketEnd,
            expenseCents = row?.expenseCents ?: 0L,
            incomeCents = row?.incomeCents ?: 0L,
            count = row?.count ?: 0
        )
        cursor = month.plusMonths(1).atDay(1)
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
