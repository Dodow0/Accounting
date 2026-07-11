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
