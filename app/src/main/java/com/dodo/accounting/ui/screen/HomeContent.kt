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
internal fun HomeScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    val monthStart = localDateFromMillis(uiState.calendarMonthStartMillis)
    val monthlyTransactions = uiState.calendarMonthTransactions
    val listState = rememberLazyListState()

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    val collapseProgress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (listState.firstVisibleItemScrollOffset / 240f).coerceIn(0f, 1f)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            HomeMonthlySummaryCard(
                monthStart = monthStart,
                transactions = monthlyTransactions,
                onPreviousMonth = { viewModel.moveCalendarMonth(-1) },
                onNextMonth = { viewModel.moveCalendarMonth(1) },
                modifier = Modifier.graphicsLayer {
                    val scale = 1f - collapseProgress * 0.08f
                    scaleX = scale
                    scaleY = scale
                    alpha = 1f - collapseProgress * 0.12f
                    translationY = -collapseProgress * 18.dp.toPx()
                }
            )
        }
        item {
            Spacer(Modifier.height(16.dp))
            HomeBudgetProgress(
                budgetCents = uiState.monthlyBudget?.amountCents ?: 0,
                expenseCents = monthlyTransactions
                    .filter { it.transaction.type == TransactionType.EXPENSE }
                    .sumOf { it.transaction.amountCents },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SectionHeader(
                    title = "账单概览",
                    action = "${monthlyTransactions.size} 笔"
                )
            }
        }
        transactionDayGroups(
            transactions = monthlyTransactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "本月暂无流水",
            showInlineActions = false,
            onLongPress = { actionTarget = it },
            groupModifier = Modifier.padding(horizontal = 16.dp)
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
internal fun HomeMonthlySummaryCard(
    monthStart: LocalDate,
    transactions: List<TransactionWithDetails>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val incomeCents = transactions
        .filter { it.transaction.type == TransactionType.INCOME }
        .sumOf { it.transaction.amountCents }
    val expenseCents = transactions
        .filter { it.transaction.type == TransactionType.EXPENSE }
        .sumOf { it.transaction.amountCents }
    val balanceCents = incomeCents - expenseCents

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${monthStart.monthValue}月记账",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPreviousMonth) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "上月",
                            tint = Color.White.copy(alpha = 0.74f)
                        )
                    }
                    Text(
                        "${monthStart.year}年${monthStart.monthValue}月",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onNextMonth) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "下月",
                            tint = Color.White.copy(alpha = 0.74f)
                        )
                    }
                }
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.88f),
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "本月结余",
                    color = Color.White.copy(alpha = 0.84f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    Money(balanceCents).format(),
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeSummaryMetric("收入", Money(incomeCents).formatPlain(), Color.White.copy(alpha = 0.84f), Modifier.weight(1f))
                    HomeSummaryMetric("支出", Money(expenseCents).formatPlain(), LedgerExpensePink, Modifier.weight(1f))
                    HomeSummaryMetric("笔数", transactions.size.toString(), Color.White, Modifier.weight(1f))
                }
            }
        }
    }
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            color = Color.White.copy(alpha = 0.72f),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            color = valueColor,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun HomeBudgetProgress(
    budgetCents: Long,
    expenseCents: Long,
    modifier: Modifier = Modifier
) {
    val ratio = if (budgetCents > 0) expenseCents.toFloat() / budgetCents.toFloat() else 0f
    val remainingCents = (budgetCents - expenseCents).coerceAtLeast(0)
    val progressColor = when {
        budgetCents <= 0 -> MaterialTheme.colorScheme.outline
        ratio < 0.7f -> MaterialTheme.colorScheme.primary
        ratio <= 1f -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        "月度预算",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    if (budgetCents > 0) "已用 ${(ratio * 100).toInt()}% · 余 ${Money(remainingCents).format()}" else "未设置",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace
                )
            }
            LinearProgressIndicator(
                progress = { ratio.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp),
                color = progressColor,
                trackColor = LedgerDivider
            )
        }
    }
}

@Composable
internal fun AssetOverviewCard(uiState: AccountingUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "总资产",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            Money(uiState.totalAssetsCents).format(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssetMetricPill(
                label = "本期支出",
                cents = uiState.summary?.totals?.expenseCents ?: 0,
                icon = Icons.Default.ArrowUpward,
                color = MaterialTheme.colorScheme.error
            )
            AssetMetricPill(
                label = "本期收入",
                cents = uiState.summary?.totals?.incomeCents ?: 0,
                icon = Icons.Default.ArrowDownward,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
internal fun AssetMetricPill(
    label: String,
    cents: Long,
    icon: ImageVector,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.09f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    Money(cents).format(),
                    color = color,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
internal fun AccountRow(
    row: AccountBalanceRow,
    onDelete: (() -> Unit)? = null
) {
    var confirmDelete by remember(row.account.id) { mutableStateOf(false) }

    LaunchedEffect(confirmDelete) {
        if (confirmDelete) {
            delay(2400)
            confirmDelete = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(row.account.colorArgb).copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        accountIcon(row.account.type),
                        contentDescription = null,
                        tint = Color(row.account.colorArgb)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(row.account.name, fontWeight = FontWeight.SemiBold)
                Text(
                    accountTypeLabel(row.account.type),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                Money(row.balanceCents).format(),
                fontWeight = FontWeight.SemiBold
            )
            if (onDelete != null) {
                Spacer(Modifier.width(4.dp))
                IconButton(
                    onClick = {
                        if (confirmDelete) {
                            confirmDelete = false
                            onDelete()
                        } else {
                            confirmDelete = true
                        }
                    }
                ) {
                    Icon(
                        if (confirmDelete) Icons.Default.Check else Icons.Default.Delete,
                        contentDescription = if (confirmDelete) "确认删除账户" else "删除账户",
                        tint = if (confirmDelete) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
internal fun AddAccountCard(viewModel: AccountingViewModel) {
    var name by remember { mutableStateOf("") }
    var initialBalance by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(AccountType.CUSTOM) }

    LedgerCard {
            Text("新增资产账户", fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountType.entries.forEach { accountType ->
                    FilterChip(
                        selected = type == accountType,
                        onClick = { type = accountType },
                        label = { Text(accountTypeLabel(accountType)) }
                    )
                }
            }
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "账户名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            MinimalInputLine(
                value = initialBalance,
                onValueChange = { initialBalance = it },
                placeholder = "初始余额",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            FilledTonalButton(
                onClick = {
                    viewModel.addAccount(name, type, initialBalance)
                    name = ""
                    initialBalance = ""
                    type = AccountType.CUSTOM
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("添加账户")
            }
    }
}
