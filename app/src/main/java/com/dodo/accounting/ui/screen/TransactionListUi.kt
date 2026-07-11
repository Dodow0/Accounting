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
