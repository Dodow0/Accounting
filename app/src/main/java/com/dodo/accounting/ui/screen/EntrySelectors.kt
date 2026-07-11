@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.dodo.accounting.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.indication
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.material3.AlertDialog
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
import com.dodo.accounting.domain.model.projectedCategoryBudgetCents
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

private sealed interface EntryCategoryGridItem {
    data class Category(val category: CategoryEntity) : EntryCategoryGridItem
    data object More : EntryCategoryGridItem
}

@Composable
internal fun EntryCategoryGrid(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long) -> Unit
) {
    var showAllCategories by remember { mutableStateOf(false) }
    val hasMore = categories.size > 10
    val visibleItems = buildList {
        val visibleCategories = if (hasMore) categories.take(9) else categories.take(10)
        visibleCategories.forEach { add(EntryCategoryGridItem.Category(it)) }
        if (hasMore) add(EntryCategoryGridItem.More)
    }
    val rows = visibleItems.chunked(5)

    if (showAllCategories) {
        ModalBottomSheet(
            onDismissRequest = { showAllCategories = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "选择分类",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        EntryCategoryTile(
                            category = category,
                            selected = category.id == selectedCategoryId,
                            onClick = {
                                onSelected(category.id)
                                showAllCategories = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { item ->
                    when (item) {
                        is EntryCategoryGridItem.Category -> EntryCategoryTile(
                            category = item.category,
                            selected = item.category.id == selectedCategoryId,
                            onClick = { onSelected(item.category.id) },
                            modifier = Modifier.weight(1f)
                        )
                        EntryCategoryGridItem.More -> EntryMoreCategoryTile(
                            onClick = { showAllCategories = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                repeat(5 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
internal fun EntryCategoryTile(
    category: CategoryEntity,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = Color(category.colorArgb)
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) LedgerMint else Color.Transparent,
        border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .indication(interactionSource, LocalIndication.current),
                shape = RoundedCornerShape(8.dp),
                color = if (selected) MaterialTheme.colorScheme.surface else tint.copy(alpha = 0.11f),
                border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.62f)) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        categoryIcon(category.iconName),
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.primary else tint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.height(13.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Text(
                category.name,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
internal fun EntryMoreCategoryTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.64f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "更多",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
internal fun CategoryBudgetHint(
    uiState: AccountingUiState,
    category: CategoryEntity,
    amount: String,
    editing: TransactionWithDetails?,
    occurredAt: Long
) {
    val budget = uiState.categoryBudgets.firstOrNull { it.categoryId == category.id }
    val projectedCents = projectedCategoryBudgetCents(
        categoryId = category.id,
        amount = amount,
        occurredAt = occurredAt,
        expenseByCategory = uiState.entryMonthExpenseByCategory,
        editingTransaction = editing?.transaction
    )
    val budgetCents = budget?.amountCents ?: 0
    val progress = if (budgetCents > 0) projectedCents.toFloat() / budgetCents.toFloat() else 0f
    val overBudget = budgetCents > 0 && projectedCents > budgetCents
    val tint = if (overBudget) MaterialTheme.colorScheme.error else Color(category.colorArgb)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = tint.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${category.name}预算",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    color = tint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    if (budgetCents > 0) "${Money(projectedCents).format()} / ${Money(budgetCents).format()}" else "未设置",
                    color = tint,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (budgetCents > 0) {
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = tint,
                    trackColor = tint.copy(alpha = 0.14f)
                )
                Text(
                    if (overBudget) {
                    "本次提交后预计超出 ${Money(projectedCents - budgetCents).format()}"
                    } else {
                    "本次提交后剩余 ${Money(budgetCents - projectedCents).format()}"
                    },
                    color = tint,
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace
                )
            } else {
                Text(
                    "可在设置页为该分类设置月预算",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun DateTimeSelector(
    occurredAt: Long,
    onChanged: (Long) -> Unit
) {
    var pickerMode by remember { mutableStateOf<DateTimePickerMode?>(null) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EntryActionPill(
            label = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date(occurredAt)),
            icon = Icons.Default.DateRange,
            onClick = { pickerMode = DateTimePickerMode.Date },
            modifier = Modifier.weight(1f)
        )
        EntryActionPill(
            label = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(occurredAt)),
            icon = Icons.Default.AccessTime,
            onClick = { pickerMode = DateTimePickerMode.Time },
            modifier = Modifier.weight(1f)
        )
    }

    pickerMode?.let { mode ->
        ModalBottomSheet(
            onDismissRequest = { pickerMode = null },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            DateTimeWheelSheet(
                mode = mode,
                occurredAt = occurredAt,
                onCancel = { pickerMode = null },
                onConfirm = { next ->
                    onChanged(next)
                    pickerMode = null
                }
            )
        }
    }
}

@Composable
internal fun DateTimeWheelSheet(
    mode: DateTimePickerMode,
    occurredAt: Long,
    onCancel: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val baseCalendar = remember(occurredAt) {
        Calendar.getInstance().apply { timeInMillis = occurredAt }
    }
    val baseYear = baseCalendar.get(Calendar.YEAR)
    var year by remember(occurredAt) { mutableStateOf(baseYear) }
    var month by remember(occurredAt) { mutableStateOf(baseCalendar.get(Calendar.MONTH) + 1) }
    var day by remember(occurredAt) { mutableStateOf(baseCalendar.get(Calendar.DAY_OF_MONTH)) }
    var hour by remember(occurredAt) { mutableStateOf(baseCalendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember(occurredAt) { mutableStateOf(baseCalendar.get(Calendar.MINUTE)) }
    val daysInMonth = remember(year, month) { YearMonth.of(year, month).lengthOfMonth() }

    LaunchedEffect(daysInMonth) {
        if (day > daysInMonth) {
            day = daysInMonth
        }
    }

    LedgerCard(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("取消", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                if (mode == DateTimePickerMode.Date) "选择日期" else "选择时间",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = {
                    onConfirm(
                        dateTimeToMillis(
                            baseMillis = occurredAt,
                            year = year,
                            month = month,
                            day = day,
                            hour = hour,
                            minute = minute
                        )
                    )
                }
            ) {
                Text("完成", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }

        if (mode == DateTimePickerMode.Date) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WheelPickerColumn(
                    values = ((baseYear - 5)..(baseYear + 5)).toList(),
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
                    selected = day,
                    label = { "${it}日" },
                    onSelected = { day = it },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WheelPickerColumn(
                    values = (0..23).toList(),
                    selected = hour,
                    label = { it.toString().padStart(2, '0') },
                    onSelected = { hour = it },
                    modifier = Modifier.weight(1f)
                )
                WheelPickerColumn(
                    values = (0..59).toList(),
                    selected = minute,
                    label = { it.toString().padStart(2, '0') },
                    onSelected = { minute = it },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
internal fun WheelPickerColumn(
    values: List<Int>,
    selected: Int,
    label: (Int) -> String,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(values, selected) {
        val selectedIndex = values.indexOf(selected)
        if (selectedIndex >= 0) {
            listState.scrollToItem((selectedIndex - 1).coerceAtLeast(0))
        }
    }

    LazyColumn(
        modifier = modifier.height(188.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(vertical = 30.dp)
    ) {
        items(values, key = { it }) { value ->
            val isSelected = value == selected
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onSelected(value) },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) LedgerMint else Color.Transparent,
                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.32f)) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        label(value),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
internal fun CompactAccountPicker(
    label: String,
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.id == selectedAccountId }
    val tint = selected?.let { Color(it.colorArgb) } ?: MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(44.dp)
            .clickable(onClick = { expanded = true }),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = RoundedCornerShape(8.dp),
                color = tint.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        selected?.let { accountIcon(it) } ?: Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    selected?.name ?: "请选择",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(4.dp))
            Icon(
                Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${accounts.size} 个账户")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(accounts, key = { it.id }) { account ->
                        AccountGridItem(
                            account = account,
                            selected = account.id == selectedAccountId,
                            onClick = {
                                onSelected(account.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
internal fun AccountPickerField(
    label: String,
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.id == selectedAccountId }

    PickerField(
        label = label,
        value = selected?.name.orEmpty(),
        icon = selected?.let { accountIcon(it) } ?: Icons.Default.AccountBalanceWallet,
        tint = selected?.let { Color(it.colorArgb) } ?: MaterialTheme.colorScheme.primary,
        onClick = { expanded = true }
    )

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${accounts.size} 个账户")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(accounts, key = { it.id }) { account ->
                        AccountGridItem(
                            account = account,
                            selected = account.id == selectedAccountId,
                            onClick = {
                                onSelected(account.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
internal fun CategoryPickerField(
    label: String,
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = categories.firstOrNull { it.id == selectedCategoryId }

    PickerField(
        label = label,
        value = selected?.name.orEmpty(),
        icon = selected?.let { categoryIcon(it.iconName) } ?: Icons.Default.Category,
        tint = selected?.let { Color(it.colorArgb) } ?: MaterialTheme.colorScheme.primary,
        onClick = { expanded = true }
    )

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${categories.size} 个分类")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        CategoryGridItem(
                            category = category,
                            selected = category.id == selectedCategoryId,
                            onClick = {
                                onSelected(category.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
internal fun PickerField(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = LedgerCardShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(34.dp),
                shape = RoundedCornerShape(8.dp),
                color = tint.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    value.ifBlank { "请选择" },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun SelectionSheetHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun AccountGridItem(
    account: AccountEntity,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SelectionGridItem(
        label = account.name,
        icon = accountIcon(account),
        tint = Color(account.colorArgb),
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
internal fun CategoryGridItem(
    category: CategoryEntity,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SelectionGridItem(
        label = category.name,
        icon = categoryIcon(category.iconName),
        tint = Color(category.colorArgb),
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
internal fun SelectionGridItem(
    label: String,
    icon: ImageVector,
    tint: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "selectionGridItemScale"
    )
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(8.dp),
                color = tint.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint)
                }
            }
            Spacer(Modifier.size(6.dp))
            Text(
                label,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
internal fun TagSelector(
    tags: List<TagEntity>,
    selectedTagIds: Set<Long>,
    onToggle: (Long) -> Unit,
    onAddTag: (String) -> Unit
) {
    var showAddTagSheet by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            "标签",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                TagPill(
                    label = tag.name,
                    selected = tag.id in selectedTagIds,
                    onClick = { onToggle(tag.id) }
                )
            }
            AddTagPill(onClick = { showAddTagSheet = true })
        }
        if (tags.isEmpty()) {
            Text(
                "还没有标签，可以先添加一个固定标签",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showAddTagSheet) {
        AddTagSheet(
            onDismiss = { showAddTagSheet = false },
            onConfirm = { name ->
                onAddTag(name)
                showAddTagSheet = false
            }
        )
    }
}

@Composable
private fun AddTagSheet(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tagName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 42.dp, height = 5.dp),
                shape = RoundedCornerShape(999.dp),
                color = LedgerDivider
            ) {}
            Text("添加标签", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            MinimalInputLine(
                value = tagName,
                onValueChange = { tagName = it },
                placeholder = "标签名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LedgerActionButton(
                    label = "取消",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    borderColor = LedgerDivider
                )
                LedgerActionButton(
                    label = "添加并选择",
                    icon = Icons.Default.Add,
                    onClick = { onConfirm(tagName) },
                    modifier = Modifier.weight(1f),
                    enabled = tagName.isNotBlank()
                )
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
internal fun AddTagPill(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "添加",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
internal fun TagPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        color = if (selected) LedgerMint else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.42f) else LedgerDivider
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                label,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
internal fun TypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    val types = listOf(
        TransactionType.EXPENSE to "支出",
        TransactionType.INCOME to "收入",
        TransactionType.TRANSFER to "转账"
    )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            types.forEach { (type, label) ->
                val selected = selectedType == type
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clickable { onTypeSelected(type) },
                    shape = RoundedCornerShape(7.dp),
                    color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            label,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

internal fun dateTimeToMillis(
    baseMillis: Long,
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int
): Long {
    return Calendar.getInstance().apply {
        timeInMillis = baseMillis
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
