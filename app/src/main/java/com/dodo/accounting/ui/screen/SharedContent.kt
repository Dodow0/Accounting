package com.dodo.accounting.ui.screen

import android.widget.Toast
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
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
internal fun Modifier.ledgerPressClickable(
    enabled: Boolean = true,
    pressedScale: Float = 0.98f,
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (enabled && pressed) pressedScale else 1f,
        label = "ledgerPressClickableScale"
    )
    return scale(pressScale).clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        onClick = onClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Modifier.ledgerPressCombinedClickable(
    enabled: Boolean = true,
    pressedScale: Float = 0.98f,
    indication: Indication? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (enabled && pressed) pressedScale else 1f,
        label = "ledgerPressCombinedClickableScale"
    )
    return scale(pressScale).combinedClickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

internal enum class ReorderDragAxis {
    Vertical,
    Horizontal
}

internal class LongPressReorderState<T>(
    initialItems: List<T>,
    private val keyOf: (T) -> Any,
    private val swapThresholdFraction: Float,
    private val itemSpacingPx: Int,
    private val onReordered: (List<T>) -> Unit
) {
    var items by mutableStateOf(initialItems)
        private set
    var draggingKey by mutableStateOf<Any?>(null)
        private set
    private var draggingOffsetPx by mutableStateOf(0f)
    private var changed by mutableStateOf(false)
    private val itemSizes = mutableStateMapOf<Any, Int>()

    fun sync(nextItems: List<T>) {
        if (draggingKey == null && items != nextItems) {
            items = nextItems
        }
    }

    fun updateItemSize(item: T, sizePx: Int) {
        itemSizes[keyOf(item)] = sizePx
    }

    fun startDragging(item: T) {
        draggingKey = keyOf(item)
        draggingOffsetPx = 0f
        changed = false
    }

    fun dragBy(deltaPx: Float) {
        val key = draggingKey ?: return
        val fromIndex = items.indexOfFirst { keyOf(it) == key }
        if (fromIndex == -1) return
        val stepPx = ((itemSizes[key] ?: 0) + itemSpacingPx).coerceAtLeast(1)
        val thresholdPx = stepPx * swapThresholdFraction

        draggingOffsetPx += deltaPx
        while (abs(draggingOffsetPx) >= thresholdPx) {
            val currentIndex = items.indexOfFirst { keyOf(it) == key }
            if (currentIndex == -1) return

            val direction = if (draggingOffsetPx > 0f) 1 else -1
            val targetIndex = (currentIndex + direction).coerceIn(0, items.lastIndex)
            if (targetIndex == currentIndex) return

            items = items.toMutableList().apply {
                add(targetIndex, removeAt(currentIndex))
            }
            draggingOffsetPx -= direction * stepPx
            changed = true
        }
    }

    fun endDragging() {
        val reorderedItems = items
        val shouldSave = changed
        draggingKey = null
        draggingOffsetPx = 0f
        changed = false
        if (shouldSave) {
            onReordered(reorderedItems)
        }
    }

    fun cancelDragging() {
        draggingKey = null
        draggingOffsetPx = 0f
        changed = false
    }

    fun itemOffset(item: T): Int =
        if (keyOf(item) == draggingKey) draggingOffsetPx.roundToInt() else 0

    fun isDragging(item: T): Boolean = keyOf(item) == draggingKey
}

@Composable
internal fun <T> rememberLongPressReorderState(
    items: List<T>,
    keyOf: (T) -> Any,
    swapThresholdFraction: Float = 0.5f,
    itemSpacingPx: Int = 0,
    onReordered: (List<T>) -> Unit
): LongPressReorderState<T> {
    val currentOnReordered = rememberUpdatedState(onReordered)
    val state = remember {
        LongPressReorderState(
            initialItems = items,
            keyOf = keyOf,
            swapThresholdFraction = swapThresholdFraction,
            itemSpacingPx = itemSpacingPx,
            onReordered = { currentOnReordered.value(it) }
        )
    }
    LaunchedEffect(items) {
        state.sync(items)
    }
    return state
}

@Composable
internal fun <T> Modifier.longPressReorderItem(
    state: LongPressReorderState<T>,
    item: T,
    axis: ReorderDragAxis = ReorderDragAxis.Vertical
): Modifier {
    val haptic = LocalHapticFeedback.current
    val offsetPx = state.itemOffset(item)
    val isDragging = state.isDragging(item)
    return this
        .zIndex(if (isDragging) 1f else 0f)
        .offset {
            when (axis) {
                ReorderDragAxis.Vertical -> IntOffset(0, offsetPx)
                ReorderDragAxis.Horizontal -> IntOffset(offsetPx, 0)
            }
        }
        .onSizeChanged { size ->
            state.updateItemSize(
                item = item,
                sizePx = when (axis) {
                    ReorderDragAxis.Vertical -> size.height
                    ReorderDragAxis.Horizontal -> size.width
                }
            )
        }
        .pointerInput(state, item, axis) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    state.startDragging(item)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onDragEnd = { state.endDragging() },
                onDragCancel = { state.cancelDragging() },
                onDrag = { change, dragAmount ->
                    change.consume()
                    state.dragBy(
                        when (axis) {
                            ReorderDragAxis.Vertical -> dragAmount.y
                            ReorderDragAxis.Horizontal -> dragAmount.x
                        }
                    )
                }
            )
        }
}

@Composable
internal fun LedgerChoiceChip(
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(36.dp)
            .ledgerPressClickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) LedgerMint else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.64f),
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
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun LedgerActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = Color.Transparent,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier
            .heightIn(min = 44.dp)
            .ledgerPressClickable(enabled = enabled, pressedScale = 0.97f, onClick = onClick),
        shape = LedgerCardShape,
        color = if (enabled) containerColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                label,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun LedgerIconActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Surface(
        modifier = modifier
            .size(44.dp)
            .ledgerPressClickable(pressedScale = 0.96f, onClick = onClick),
        shape = LedgerCardShape,
        color = containerColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription, tint = contentColor)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TransactionRow(
    item: TransactionWithDetails,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    amountsHidden: Boolean = false,
    trailing: @Composable () -> Unit
) {
    val transaction = item.transaction
    val category = item.activeCategory
    val rowTint = category?.let { Color(it.colorArgb) } ?: transactionColor(transaction.type)
    val rowIcon = category?.let { categoryIcon(it.iconName) } ?: transactionIcon(transaction.type)
    val title = when (transaction.type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> category?.name ?: transactionTitle(item)
        else -> transactionTitle(item)
    }
    val subtitle = when (transaction.type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> {
            listOf(transaction.merchant, transaction.note, item.account?.name.orEmpty())
                .filter { it.isNotBlank() }
                .joinToString(" · ")
                .ifBlank { transactionSubtitle(item) }
        }
        else -> transactionSubtitle(item)
    }
    val rowModifier = if (onClick != null || onLongClick != null) {
        Modifier
            .fillMaxWidth()
            .ledgerPressCombinedClickable(
                pressedScale = 0.985f,
                onClick = { onClick?.invoke() },
                onLongClick = onLongClick
            )
    } else {
        Modifier.fillMaxWidth()
    }
    Row(
        modifier = rowModifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(8.dp),
            color = rowTint.copy(alpha = 0.13f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    rowIcon,
                    contentDescription = null,
                    tint = rowTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                plainAmountLabel(transaction.type, transaction.amountCents, amountsHidden),
                color = transactionColor(transaction.type),
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(transaction.occurredAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.Monospace
            )
        }
        trailing()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionActionSheet(
    transaction: TransactionWithDetails,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var confirmDelete by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(
                transactionTitle(transaction),
                amountLabel(transaction.transaction.type, transaction.transaction.amountCents)
            )
            LedgerActionButton(
                label = "编辑",
                icon = Icons.Default.Edit,
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                containerColor = LedgerMint,
                contentColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            LedgerActionButton(
                label = if (confirmDelete) "确认删除" else "删除",
                icon = Icons.Default.Delete,
                onClick = {
                    if (confirmDelete) {
                        onDelete()
                    } else {
                        confirmDelete = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.26f)
            )
            Spacer(Modifier.size(12.dp))
        }
    }
}

@Composable
internal fun CategoryManagementCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var showCreateCategorySheet by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryEntity?>(null) }
    val categoryItemGap = 6.dp
    val categoryItemGapPx = with(LocalDensity.current) { categoryItemGap.roundToPx() }

    LedgerCard {
            SectionHeader("分类管理", "${uiState.categories.size} 类")
            LedgerActionButton(
                label = "新增分类",
                icon = Icons.Default.Add,
                onClick = { showCreateCategorySheet = true },
                modifier = Modifier.fillMaxWidth()
            )
            CategoryKind.entries.forEach { kind ->
                val categories = uiState.categories.filter { it.kind == kind }
                val reorderState = rememberLongPressReorderState(
                    items = categories,
                    keyOf = { it.id },
                    swapThresholdFraction = 0.58f,
                    itemSpacingPx = categoryItemGapPx,
                    onReordered = { reorderedCategories ->
                        viewModel.reorderCategories(reorderedCategories.map { it.id })
                    }
                )
                if (categories.isEmpty()) return@forEach
                Text(
                    categoryKindLabel(kind),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(categoryItemGap)) {
                    reorderState.items.forEach { category ->
                        key(category.id) {
                            Box(
                                modifier = Modifier.longPressReorderItem(
                                    state = reorderState,
                                    item = category
                                )
                            ) {
                                CategoryManagementRow(
                                    category = category,
                                    onEdit = { categoryToEdit = category }
                                )
                            }
                        }
                    }
                }
            }
    }

    categoryToEdit?.let { category ->
        CategoryEditDialog(
            category = category,
            onDismiss = { categoryToEdit = null },
            onSave = { name, iconName, colorArgb ->
                viewModel.updateCategory(category.id, name, iconName, colorArgb)
                categoryToEdit = null
            },
            onDelete = {
                viewModel.deleteCategory(category.id)
                categoryToEdit = null
            }
        )
    }

    if (showCreateCategorySheet) {
        CategoryCreateSheet(
            onDismiss = { showCreateCategorySheet = false },
            onSave = { name, kind, iconName, colorArgb ->
                viewModel.addCategory(name, kind, iconName, colorArgb)
                showCreateCategorySheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryCreateSheet(
    onDismiss: () -> Unit,
    onSave: (String, CategoryKind, String, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var kind by remember { mutableStateOf(CategoryKind.EXPENSE) }
    var iconName by remember { mutableStateOf("receipt_long") }
    var colorArgb by remember { mutableStateOf(0xFFEA580C) }

    fun selectKind(nextKind: CategoryKind) {
        if (kind == nextKind) return
        kind = nextKind
        iconName = if (nextKind == CategoryKind.EXPENSE) "receipt_long" else "work"
        colorArgb = if (nextKind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text("取消", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("新增分类", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = { onSave(name.trim(), kind, iconName, colorArgb) },
                    enabled = name.isNotBlank()
                ) {
                    Text("保存", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "分类名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text("类型", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(CategoryKind.EXPENSE, CategoryKind.INCOME).forEach { option ->
                    LedgerChoiceChip(
                        selected = kind == option,
                        label = categoryKindLabel(option),
                        onClick = { selectKind(option) }
                    )
                }
            }
            CategoryIconPicker(
                kind = kind,
                selectedIconName = iconName,
                tint = Color(colorArgb),
                onSelected = { iconName = it }
            )
            Text("颜色", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categoryColorOptions().forEach { option ->
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { colorArgb = option },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(option),
                        border = BorderStroke(
                            if (colorArgb == option) 3.dp else 1.dp,
                            if (colorArgb == option) MaterialTheme.colorScheme.onSurface else LedgerDivider
                        )
                    ) {}
                }
            }
            Spacer(Modifier.height(14.dp))
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
internal fun CategoryManagementRow(
    category: CategoryEntity,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ledgerPressClickable(onClick = onEdit),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(category.colorArgb).copy(alpha = 0.14f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(categoryIcon(category.iconName), contentDescription = null, tint = Color(category.colorArgb))
            }
        }
        Text(
            category.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "编辑分类")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryEditDialog(
    category: CategoryEntity,
    onDismiss: () -> Unit,
    onSave: (String, String, Long) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(category.id) { mutableStateOf(category.name) }
    var iconName by remember(category.id) { mutableStateOf(category.iconName) }
    var colorArgb by remember(category.id) { mutableStateOf(category.colorArgb) }
    var confirmDelete by remember(category.id) { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text("取消", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("编辑分类", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = { onSave(name, iconName, colorArgb) }) {
                    Text("保存", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "分类名称",
                modifier = Modifier.fillMaxWidth()
            )
            CategoryIconPicker(
                kind = category.kind,
                selectedIconName = iconName,
                tint = Color(colorArgb),
                onSelected = { iconName = it }
            )
            Text("颜色", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categoryColorOptions().forEach { option ->
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { colorArgb = option },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(option),
                        border = BorderStroke(
                            if (colorArgb == option) 3.dp else 1.dp,
                            if (colorArgb == option) MaterialTheme.colorScheme.onSurface else LedgerDivider
                        )
                    ) {}
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            LedgerActionButton(
                label = if (confirmDelete) "确认删除" else "删除分类",
                icon = Icons.Default.Delete,
                onClick = {
                    if (confirmDelete) {
                        onDelete()
                    } else {
                        confirmDelete = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.26f)
            )
            Spacer(Modifier.height(14.dp))
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun CategoryIconPicker(
    kind: CategoryKind,
    selectedIconName: String,
    tint: Color,
    onSelected: (String) -> Unit
) {
    val groups = remember(kind) { categoryIconGroups(kind) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("图标", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        groups.forEach { group ->
            if (group.options.isNotEmpty()) {
                Text(
                    group.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    group.options.forEach { option ->
                        CategoryEditPill(
                            selected = selectedIconName == option.name,
                            label = option.label,
                            icon = categoryIcon(option.name),
                            tint = tint,
                            onClick = { onSelected(option.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun CategoryEditPill(
    selected: Boolean,
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(38.dp)
            .ledgerPressClickable(onClick = onClick),
        shape = RoundedCornerShape(19.dp),
        color = if (selected) LedgerMint else Color.Transparent,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.42f) else LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagManagementCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var newTagName by remember { mutableStateOf("") }
    var tagToRename by remember { mutableStateOf<TagEntity?>(null) }
    val reorderState = rememberLongPressReorderState(
        items = uiState.tags,
        keyOf = { it.id },
        onReordered = { reorderedTags ->
            viewModel.reorderTags(reorderedTags.map { it.id })
        }
    )

    LedgerCard {
            SectionHeader("标签管理", "${uiState.tags.size} 个")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MinimalInputLine(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    placeholder = "新标签名称",
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                LedgerIconActionButton(
                    icon = Icons.Default.Add,
                    contentDescription = "新增标签",
                    onClick = {
                        if (newTagName.isNotBlank()) {
                            viewModel.addTag(newTagName)
                            newTagName = ""
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reorderState.items.forEach { tag ->
                    Box(
                        modifier = Modifier.longPressReorderItem(
                            state = reorderState,
                            item = tag,
                            axis = ReorderDragAxis.Horizontal
                        )
                    ) {
                        TagPill(
                            tag = tag,
                            onRename = { tagToRename = tag },
                            onDelete = { viewModel.deleteTag(tag.id) }
                        )
                    }
                }
            }
    }

    if (tagToRename != null) {
        var renamedName by remember { mutableStateOf(tagToRename?.name.orEmpty()) }
        ModalBottomSheet(
            onDismissRequest = { tagToRename = null },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SectionHeader("重命名标签", tagToRename?.name.orEmpty())
                MinimalInputLine(
                    value = renamedName,
                    onValueChange = { renamedName = it },
                    placeholder = "标签名称",
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LedgerActionButton(
                        label = "取消",
                        onClick = { tagToRename = null },
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        borderColor = LedgerDivider
                    )
                    LedgerActionButton(
                        label = "保存",
                        onClick = {
                            tagToRename?.let { viewModel.renameTag(it.id, renamedName) }
                            tagToRename = null
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
internal fun SummaryMetric(
    label: String,
    cents: Long,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(Money(cents).format(), color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun TagPill(
    tag: TagEntity,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(38.dp)
            .ledgerPressClickable(onClick = onRename),
        shape = RoundedCornerShape(19.dp),
        color = LedgerMint,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                tag.name,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                Icons.Default.Delete,
                contentDescription = "删除标签",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onDelete)
            )
        }
    }
}

@Composable
internal fun ExpenseDonutChart(
    rows: List<CategorySummary>,
    totalCents: Long,
    categories: List<CategoryEntity>
) {
    val maxLegendItems = 6
    val directRows = if (rows.size > maxLegendItems) rows.take(maxLegendItems - 1) else rows
    val remainingRows = rows.drop(directRows.size)
    val fallbackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.56f)
    val remainingColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.72f)
    fun categoryColor(row: CategorySummary): Color {
        return categories.firstOrNull { it.id == row.categoryId }
            ?.let { Color(it.colorArgb) }
            ?: fallbackColor
    }
    val segments = buildList {
        directRows.forEach { row ->
            add(
                DonutSegment(
                    label = row.categoryName ?: "未分类",
                    amountCents = row.amountCents,
                    color = categoryColor(row)
                )
            )
        }
        if (remainingRows.isNotEmpty()) {
            add(
                DonutSegment(
                    label = "其余 ${remainingRows.size} 类",
                    amountCents = remainingRows.sumOf { it.amountCents },
                    color = remainingColor
                )
            )
        }
    }

    LedgerCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(132.dp)) {
                    val strokeWidth = 18.dp.toPx()
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.22f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    if (totalCents > 0) {
                        var startAngle = -90f
                        segments.forEach { segment ->
                            val sweep = segment.amountCents.toFloat() / totalCents.toFloat() * 360f
                            drawArc(
                                color = segment.color,
                                startAngle = startAngle,
                                sweepAngle = sweep.coerceAtLeast(1.4f),
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                            startAngle += sweep
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "支出",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        Money(totalCents).format(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("分类占比", style = MaterialTheme.typography.titleMedium)
                if (rows.isEmpty()) {
                    Text(
                        "本期暂无支出",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    segments.forEach { segment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(10.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = segment.color
                                ) {}
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    segment.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                "${categoryPercent(segment.amountCents, totalCents)}%",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class DonutSegment(
    val label: String,
    val amountCents: Long,
    val color: Color
)

@Composable
internal fun CategorySummaryRow(
    name: String,
    cents: Long,
    totalCents: Long,
    onClick: (() -> Unit)? = null
) {
    val progress = if (totalCents > 0) {
        cents.toFloat() / totalCents.toFloat()
    } else {
        0f
    }
    val clickModifier = if (onClick != null) {
        Modifier.ledgerPressClickable(onClick = onClick)
    } else {
        Modifier
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickModifier)
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                Money(cents).format(),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        Text(
            "占比 ${categoryPercent(cents, totalCents)}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
internal fun MonthlyTrendBarChart(
    transactions: List<TransactionWithDetails>
) {
    val trends = buildMonthlyTrends(transactions)
    val maxCents = trends
        .flatMap { listOf(it.expenseCents, it.incomeCents) }
        .maxOrNull()
        ?.coerceAtLeast(1L) ?: 1L
    val expenseColor = MaterialTheme.colorScheme.onSurfaceVariant
    val incomeColor = MaterialTheme.colorScheme.secondary

    LedgerCard {
        SectionHeader("近 6 个月趋势", "收入 / 支出")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TrendLegend("支出", expenseColor)
            TrendLegend("收入", incomeColor)
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
        ) {
            val groupWidth = size.width / trends.size
            val barWidth = groupWidth * 0.22f
            trends.forEachIndexed { index, trend ->
                val center = groupWidth * index + groupWidth / 2f
                val expenseHeight = size.height * (trend.expenseCents.toFloat() / maxCents.toFloat())
                val incomeHeight = size.height * (trend.incomeCents.toFloat() / maxCents.toFloat())
                drawRect(
                    color = expenseColor,
                    topLeft = Offset(center - barWidth - 2.dp.toPx(), size.height - expenseHeight),
                    size = Size(barWidth, expenseHeight)
                )
                drawRect(
                    color = incomeColor,
                    topLeft = Offset(center + 2.dp.toPx(), size.height - incomeHeight),
                    size = Size(barWidth, incomeHeight)
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            trends.forEach { trend ->
                Text(
                    "${trend.month.monthValue}月",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun TrendLegend(
    label: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(10.dp),
            shape = RoundedCornerShape(8.dp),
            color = color
        ) {}
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun SectionHeader(
    title: String,
    action: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(action, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun InfoCard(text: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            text,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

internal fun accountIcon(account: AccountEntity): ImageVector = accountIcon(account.iconName)

internal fun accountIcon(iconName: String): ImageVector = when (iconName) {
    "payments" -> Icons.Default.Payments
    "credit_card" -> Icons.Default.CreditCard
    "chat" -> Icons.AutoMirrored.Filled.Chat
    "account_balance_wallet" -> Icons.Default.AccountBalanceWallet
    "wallet" -> Icons.Default.Wallet
    "phone_iphone" -> Icons.Default.PhoneIphone
    "directions_bus" -> Icons.Default.DirectionsBus
    "storefront" -> Icons.Default.Storefront
    "savings" -> Icons.Default.Savings
    "add_card" -> Icons.Default.AddCard
    "assessment" -> Icons.Default.Assessment
    "business_center" -> Icons.Default.BusinessCenter
    else -> Icons.Default.Wallet
}

private data class AccountIconOption(
    val name: String,
    val label: String,
    val keywords: List<String> = emptyList()
)

private fun allAccountIconOptions(): List<AccountIconOption> = listOf(
    AccountIconOption("account_balance_wallet", "钱包", listOf("余额", "账户", "支付")),
    AccountIconOption("payments", "现金", listOf("现金", "收支", "付款")),
    AccountIconOption("credit_card", "卡片", listOf("银行卡", "信用卡", "储值卡")),
    AccountIconOption("chat", "聊天支付", listOf("微信", "社交", "转账")),
    AccountIconOption("wallet", "零钱包", listOf("钱包", "现金包")),
    AccountIconOption("phone_iphone", "手机", listOf("手机", "数字余额", "电子")),
    AccountIconOption("directions_bus", "交通卡", listOf("公交", "地铁", "通勤")),
    AccountIconOption("storefront", "门店卡", listOf("会员卡", "储值", "门店")),
    AccountIconOption("savings", "储蓄", listOf("存款", "理财", "储蓄")),
    AccountIconOption("add_card", "新增卡", listOf("虚拟卡", "备用卡")),
    AccountIconOption("assessment", "投资", listOf("基金", "证券", "收益")),
    AccountIconOption("business_center", "商务", listOf("工作", "报销", "公司"))
)

internal fun accountIconOptions(query: String = ""): List<Pair<String, String>> {
    val trimmedQuery = query.trim()
    val options = allAccountIconOptions()
    val filtered = if (trimmedQuery.isBlank()) {
        options
    } else {
        options.filter { option ->
            option.name.contains(trimmedQuery, ignoreCase = true) ||
                option.label.contains(trimmedQuery, ignoreCase = true) ||
                option.keywords.any { keyword -> keyword.contains(trimmedQuery, ignoreCase = true) }
        }
    }
    return filtered.map { it.name to it.label }
}

internal fun accountIconLabel(iconName: String): String {
    return allAccountIconOptions()
        .firstOrNull { it.name == iconName }
        ?.label ?: "通用"
}

@Composable
internal fun AccountIconPicker(
    selectedIconName: String,
    tint: Color,
    onSelected: (String) -> Unit
) {
    val options = remember { accountIconOptions("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("图标", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { (name, label) ->
                CategoryEditPill(
                    selected = selectedIconName == name,
                    label = label,
                    icon = accountIcon(name),
                    tint = tint,
                    onClick = { onSelected(name) }
                )
            }
        }
    }
}

private data class CategoryIconGroup(
    val title: String,
    val options: List<CategoryIconOption>
)

private data class CategoryIconOption(
    val name: String,
    val label: String,
    val keywords: List<String> = emptyList()
)

internal fun categoryIcon(iconName: String): ImageVector = when (iconName) {
    "restaurant" -> Icons.Default.Restaurant
    "fastfood" -> Icons.Default.Fastfood
    "local_cafe" -> Icons.Default.LocalCafe
    "commute" -> Icons.Default.Commute
    "directions_bus" -> Icons.Default.DirectionsBus
    "train" -> Icons.Default.Train
    "directions_car" -> Icons.Default.DirectionsCar
    "local_gas_station" -> Icons.Default.LocalGasStation
    "shopping_bag" -> Icons.Default.ShoppingBag
    "local_mall" -> Icons.Default.LocalMall
    "local_grocery_store" -> Icons.Default.LocalGroceryStore
    "storefront" -> Icons.Default.Storefront
    "home" -> Icons.Default.Home
    "local_laundry_service" -> Icons.Default.LocalLaundryService
    "devices" -> Icons.Default.Devices
    "phone_iphone" -> Icons.Default.PhoneIphone
    "movie" -> Icons.Default.Movie
    "sports_esports" -> Icons.Default.SportsEsports
    "fitness_center" -> Icons.Default.FitnessCenter
    "flight_takeoff" -> Icons.Default.FlightTakeoff
    "hotel" -> Icons.Default.Hotel
    "school" -> Icons.Default.School
    "menu_book" -> Icons.AutoMirrored.Filled.MenuBook
    "local_hospital" -> Icons.Default.LocalHospital
    "health_and_safety" -> Icons.Default.HealthAndSafety
    "child_care" -> Icons.Default.ChildCare
    "receipt_long" -> Icons.AutoMirrored.Filled.ReceiptLong
    "credit_card" -> Icons.Default.CreditCard
    "payments" -> Icons.Default.Payments
    "wallet" -> Icons.Default.Wallet
    "event_repeat" -> Icons.Default.EventRepeat
    "work" -> Icons.Default.Work
    "business_center" -> Icons.Default.BusinessCenter
    "redeem" -> Icons.Default.Redeem
    "card_giftcard" -> Icons.Default.CardGiftcard
    "add_card" -> Icons.Default.AddCard
    "assessment" -> Icons.Default.Assessment
    "savings" -> Icons.Default.Savings
    "monetization_on" -> Icons.Default.MonetizationOn
    "attach_money" -> Icons.Default.AttachMoney
    "local_shipping" -> Icons.Default.LocalShipping
    "history" -> Icons.Default.History
    else -> Icons.Default.Category
}

internal fun categoryKindLabel(kind: CategoryKind): String = when (kind) {
    CategoryKind.EXPENSE -> "支出分类"
    CategoryKind.INCOME -> "收入分类"
}

internal fun categoryIconOptions(kind: CategoryKind): List<String> {
    return categoryIconGroups(kind)
        .flatMap { it.options }
        .distinctBy { it.name }
        .map { it.name }
}

internal fun categoryIconLabel(iconName: String): String {
    return allCategoryIconOptions()
        .firstOrNull { it.name == iconName }
        ?.label ?: "通用"
}

private fun categoryIconGroups(kind: CategoryKind): List<CategoryIconGroup> {
    val common = CategoryIconGroup(
        title = "通用",
        options = listOf(
            iconOption("receipt_long", "账单", "账单", "票据", "水电", "缴费"),
            iconOption("credit_card", "卡片", "卡片", "银行卡", "信用卡"),
            iconOption("payments", "现金", "现金", "收款", "付款"),
            iconOption("wallet", "钱包", "钱包", "余额"),
            iconOption("event_repeat", "周期", "周期", "订阅", "固定"),
            iconOption("category", "通用", "其他", "默认")
        )
    )
    val expenseGroups = listOf(
        CategoryIconGroup(
            title = "餐饮日常",
            options = listOf(
                iconOption("restaurant", "餐饮", "吃饭", "饭店", "午饭", "晚饭"),
                iconOption("fastfood", "快餐", "外卖", "零食", "快餐"),
                iconOption("local_cafe", "咖啡", "奶茶", "饮品", "咖啡"),
                iconOption("local_grocery_store", "买菜", "超市", "菜场", "食品")
            )
        ),
        CategoryIconGroup(
            title = "出行",
            options = listOf(
                iconOption("commute", "交通", "通勤", "打车", "出行"),
                iconOption("directions_bus", "公交", "公交", "地铁"),
                iconOption("train", "火车", "高铁", "铁路"),
                iconOption("directions_car", "汽车", "开车", "停车"),
                iconOption("local_gas_station", "加油", "油费", "充电")
            )
        ),
        CategoryIconGroup(
            title = "购物服务",
            options = listOf(
                iconOption("shopping_bag", "购物", "网购", "买东西"),
                iconOption("local_mall", "商场", "服饰", "百货"),
                iconOption("storefront", "门店", "线下", "店铺"),
                iconOption("local_laundry_service", "洗护", "洗衣", "家政"),
                iconOption("devices", "数码", "手机", "电脑", "软件"),
                iconOption("phone_iphone", "手机", "通信", "话费")
            )
        ),
        CategoryIconGroup(
            title = "家庭健康",
            options = listOf(
                iconOption("home", "居家", "房租", "物业", "家庭"),
                iconOption("local_hospital", "医疗", "医院", "药", "看病"),
                iconOption("health_and_safety", "健康", "保险", "体检"),
                iconOption("child_care", "育儿", "孩子", "教育支出")
            )
        ),
        CategoryIconGroup(
            title = "成长娱乐",
            options = listOf(
                iconOption("school", "教育", "课程", "学费"),
                iconOption("menu_book", "书籍", "阅读", "学习"),
                iconOption("movie", "娱乐", "电影", "会员"),
                iconOption("sports_esports", "游戏", "娱乐", "游戏"),
                iconOption("fitness_center", "运动", "健身", "锻炼"),
                iconOption("flight_takeoff", "旅行", "机票", "旅游"),
                iconOption("hotel", "住宿", "酒店", "民宿")
            )
        )
    )
    val incomeGroups = listOf(
        CategoryIconGroup(
            title = "收入",
            options = listOf(
                iconOption("work", "工资", "工资", "薪资", "工作"),
                iconOption("business_center", "副业", "项目", "兼职", "外快"),
                iconOption("redeem", "优惠", "返现", "红包", "赠送"),
                iconOption("card_giftcard", "礼金", "礼物", "奖金"),
                iconOption("add_card", "入账", "到账", "转入"),
                iconOption("assessment", "理财", "基金", "收益"),
                iconOption("savings", "储蓄", "存款", "利息"),
                iconOption("monetization_on", "奖金", "提成", "补贴"),
                iconOption("attach_money", "其他", "其他收入"),
                iconOption("local_shipping", "报销", "物流", "差旅")
            )
        )
    )
    return when (kind) {
        CategoryKind.EXPENSE -> expenseGroups + common
        CategoryKind.INCOME -> incomeGroups + common
    }
}

private fun allCategoryIconOptions(): List<CategoryIconOption> {
    return (categoryIconGroups(CategoryKind.EXPENSE) + categoryIconGroups(CategoryKind.INCOME))
        .flatMap { it.options }
        .distinctBy { it.name }
}

private fun iconOption(
    name: String,
    label: String,
    vararg keywords: String
): CategoryIconOption {
    return CategoryIconOption(name = name, label = label, keywords = keywords.toList())
}

internal fun categoryColorOptions(): List<Long> = listOf(
    0xFFEA580C,
    0xFF0891B2,
    0xFFDB2777,
    0xFF4F46E5,
    0xFF0F766E,
    0xFF16A34A,
    0xFF65A30D,
    0xFF2563EB,
    0xFF7C3AED,
    0xFF52525B
)

internal fun transactionLabel(type: TransactionType): String = when (type) {
    TransactionType.EXPENSE -> "支出"
    TransactionType.INCOME -> "收入"
    TransactionType.TRANSFER -> "转账"
    TransactionType.BALANCE_ADJUSTMENT -> "校正"
}

internal fun transactionIcon(type: TransactionType): ImageVector = when (type) {
    TransactionType.EXPENSE -> Icons.Default.ArrowUpward
    TransactionType.INCOME -> Icons.Default.ArrowDownward
    TransactionType.TRANSFER -> Icons.Default.SwapHoriz
    TransactionType.BALANCE_ADJUSTMENT -> Icons.Default.AccountBalanceWallet
}

@Composable
internal fun transactionColor(type: TransactionType): Color = when (type) {
    TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
    TransactionType.TRANSFER -> MaterialTheme.colorScheme.primary
    TransactionType.BALANCE_ADJUSTMENT -> MaterialTheme.colorScheme.tertiary
}

internal fun transactionTitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    val category = item.activeCategory
    return when (transaction.type) {
        TransactionType.TRANSFER -> "${item.fromAccount?.name ?: "未知账户"} -> ${item.toAccount?.name ?: "未知账户"}"
        TransactionType.BALANCE_ADJUSTMENT -> "${item.account?.name ?: "未知账户"} 余额校正"
        else -> transaction.merchant.ifBlank { category?.name ?: transactionLabel(transaction.type) }
    }
}

internal fun transactionSubtitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    val account = when (transaction.type) {
        TransactionType.TRANSFER -> "转账"
        else -> item.account?.name ?: "未选账户"
    }
    val category = item.activeCategory?.name.orEmpty()
    val note = transaction.note
    return listOf(account, category, note)
        .filter { it.isNotBlank() }
        .joinToString(" · ")
}

private val TransactionWithDetails.activeCategory
    get() = category?.takeIf { it.deletedAt == null }

internal fun amountLabel(type: TransactionType, cents: Long): String {
    val prefix = when (type) {
        TransactionType.EXPENSE -> "-"
        TransactionType.INCOME -> "+"
        TransactionType.TRANSFER -> ""
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+" else ""
    }
    return prefix + Money(cents).format()
}

internal fun privacyAmountLabel(cents: Long, hidden: Boolean): String {
    return if (hidden) "¥ ****" else Money(cents).format()
}

internal fun privacyPlainAmountLabel(cents: Long, hidden: Boolean): String {
    return if (hidden) "¥ ****" else Money(cents).formatPlain()
}

internal fun plainAmountLabel(type: TransactionType, cents: Long, hidden: Boolean = false): String {
    if (hidden) {
        val marker = "¥ ****"
        return when (type) {
            TransactionType.EXPENSE -> "-$marker"
            TransactionType.INCOME -> "+$marker"
            TransactionType.TRANSFER -> marker
            TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+$marker" else "-$marker"
        }
    }
    val plain = Money(cents).formatPlain()
    return when (type) {
        TransactionType.EXPENSE -> "-${plain.removePrefix("-")}"
        TransactionType.INCOME -> "+${plain.removePrefix("-")}"
        TransactionType.TRANSFER -> plain
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+$plain" else plain
    }
}

internal fun dateLabel(millis: Long): String {
    return SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(millis))
}

internal data class TransactionDayGroup(
    val date: LocalDate,
    val items: List<TransactionWithDetails>,
    val expenseCents: Long,
    val incomeCents: Long
)

internal data class MonthlyTrend(
    val month: LocalDate,
    val expenseCents: Long,
    val incomeCents: Long
)

internal data class CalendarDay(
    val date: LocalDate,
    val inMonth: Boolean
)

internal fun buildMonthlyTrends(transactions: List<TransactionWithDetails>): List<MonthlyTrend> {
    val zoneId = ZoneId.systemDefault()
    val currentMonth = LocalDate.now(zoneId).withDayOfMonth(1)
    val months = (5 downTo 0).map { currentMonth.minusMonths(it.toLong()) }
    val byMonth = transactions.groupBy { item ->
        Instant.ofEpochMilli(item.transaction.occurredAt)
            .atZone(zoneId)
            .toLocalDate()
            .withDayOfMonth(1)
    }
    return months.map { month ->
        val items = byMonth[month].orEmpty()
        MonthlyTrend(
            month = month,
            expenseCents = items
                .filter { it.transaction.type == TransactionType.EXPENSE }
                .sumOf { it.transaction.amountCents },
            incomeCents = items
                .filter { it.transaction.type == TransactionType.INCOME }
                .sumOf { it.transaction.amountCents }
        )
    }
}

internal fun List<TransactionWithDetails>.groupedByDay(): List<TransactionDayGroup> {
    val zoneId = ZoneId.systemDefault()
    return sortedByDescending { it.transaction.occurredAt }
        .groupBy { item ->
            Instant.ofEpochMilli(item.transaction.occurredAt)
                .atZone(zoneId)
                .toLocalDate()
        }
        .map { (date, items) ->
            TransactionDayGroup(
                date = date,
                items = items,
                expenseCents = items
                    .filter { it.transaction.type == TransactionType.EXPENSE }
                    .sumOf { it.transaction.amountCents },
                incomeCents = items
                    .filter { it.transaction.type == TransactionType.INCOME }
                    .sumOf { it.transaction.amountCents }
            )
        }
}

internal fun weekdayLabel(date: LocalDate): String = when (date.dayOfWeek.value) {
    1 -> "星期一"
    2 -> "星期二"
    3 -> "星期三"
    4 -> "星期四"
    5 -> "星期五"
    6 -> "星期六"
    else -> "星期日"
}

internal fun calendarMonthCells(monthStart: LocalDate): List<CalendarDay> {
    val firstDay = monthStart.withDayOfMonth(1)
    val startOffset = firstDay.dayOfWeek.value - 1
    val gridStart = firstDay.minusDays(startOffset.toLong())
    return (0 until 42).map { offset ->
        val date = gridStart.plusDays(offset.toLong())
        CalendarDay(date = date, inMonth = date.monthValue == monthStart.monthValue)
    }
}

internal fun localDateFromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

internal fun localDateStartMillis(date: LocalDate): Long {
    return date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

internal fun categoryPercent(cents: Long, totalCents: Long): Int {
    if (totalCents <= 0) return 0
    return ((cents.toDouble() / totalCents.toDouble()) * 100).toInt()
}
