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
import com.dodo.accounting.ui.viewmodel.AccountingUiState
import com.dodo.accounting.ui.viewmodel.AccountingViewModel
import com.dodo.accounting.ui.viewmodel.ExportFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayDeque
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val LedgerMint = Color(0xFFDDF4EC)
private val LedgerPanel = Color(0xFFF8F6EF)
private val LedgerDivider = Color(0xFFDFDCD3)
private val LedgerExpensePink = Color(0xFFFF9AA9)
private val LedgerCardShape = RoundedCornerShape(8.dp)

@Composable
private fun LedgerCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun LedgerPanelSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickModifier = onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(clickModifier),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountingApp(
    viewModel: AccountingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearMessage()
            }
        }
    }

    var selectedTab by remember { mutableStateOf(AppTab.Home) }
    var entrySheetOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppBottomBar(
                selectedTab = selectedTab,
                onSelected = { selectedTab = it },
                onAdd = {
                    viewModel.cancelEditTransaction()
                    entrySheetOpen = true
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .statusBarsPadding()
        ) {
            val openEditor: (TransactionWithDetails) -> Unit = { transaction ->
                viewModel.startEditTransaction(transaction)
                entrySheetOpen = true
            }
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith
                        fadeOut(animationSpec = tween(90))
                },
                label = "tabFadeThrough"
            ) { tab ->
                when (tab) {
                    AppTab.Home -> HomeScreen(uiState, viewModel, openEditor)
                    AppTab.Stats -> StatsScreen(uiState, viewModel, openEditor)
                    AppTab.Bills -> LedgerScreen(uiState, viewModel, openEditor)
                    AppTab.Mine -> MineScreen(uiState, viewModel)
                }
            }
        }
    }

    if (entrySheetOpen) {
        BackHandler {
            viewModel.cancelEditTransaction()
            entrySheetOpen = false
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            EntrySheetContentV2(
                uiState = uiState,
                viewModel = viewModel,
                onDone = {
                    entrySheetOpen = false
                    viewModel.cancelEditTransaction()
                }
            )
        }
    }
}

@Composable
private fun AppBottomBar(
    selectedTab: AppTab,
    onSelected: (AppTab) -> Unit,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, LedgerDivider.copy(alpha = 0.72f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavItem(
                tab = AppTab.Home,
                selected = selectedTab == AppTab.Home,
                onClick = { onSelected(AppTab.Home) },
                modifier = Modifier.weight(1f)
            )
            BottomNavItem(
                tab = AppTab.Stats,
                selected = selectedTab == AppTab.Stats,
                onClick = { onSelected(AppTab.Stats) },
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(52.dp)
                        .clickable(onClick = onAdd),
                    shape = RoundedCornerShape(26.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "记账",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
            BottomNavItem(
                tab = AppTab.Bills,
                selected = selectedTab == AppTab.Bills,
                onClick = { onSelected(AppTab.Bills) },
                modifier = Modifier.weight(1f)
            )
            BottomNavItem(
                tab = AppTab.Mine,
                selected = selectedTab == AppTab.Mine,
                onClick = { onSelected(AppTab.Mine) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: AppTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    Column(
        modifier = modifier
            .height(54.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            tab.icon,
            contentDescription = tab.label,
            tint = tint,
            modifier = Modifier.size(23.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            tab.label,
            color = tint,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1
        )
    }
}

private enum class AppTab(
    val label: String,
    val icon: ImageVector
) {
    Home("首页", Icons.Default.Home),
    Stats("统计", Icons.Default.Assessment),
    Bills("账单", Icons.AutoMirrored.Filled.ReceiptLong),
    Mine("设置", Icons.Default.Devices)
}

private enum class LedgerViewMode(val label: String) {
    List("列表"),
    Calendar("日历")
}

private enum class DateTimePickerMode {
    Date,
    Time
}

@Composable
private fun HomeScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    val monthStart = localDateFromMillis(uiState.calendarMonthStartMillis)
    val monthlyTransactions = uiState.calendarMonthTransactions
    val listState = rememberLazyListState()
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
private fun HomeMonthlySummaryCard(
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
        shape = RoundedCornerShape(0.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 34.dp),
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
                    Text(
                        "◀",
                        modifier = Modifier
                            .clickable(onClick = onPreviousMonth)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White.copy(alpha = 0.74f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${monthStart.year}年${monthStart.monthValue}月",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "▶",
                        modifier = Modifier
                            .clickable(onClick = onNextMonth)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White.copy(alpha = 0.74f),
                        fontWeight = FontWeight.Bold
                    )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(8.dp))
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeSummaryMetric("收入", Money(incomeCents).formatPlain(), Color.White.copy(alpha = 0.74f), Modifier.weight(1f))
                HomeSummaryMetric("支出", Money(expenseCents).formatPlain(), LedgerExpensePink, Modifier.weight(1f))
                HomeSummaryMetric("笔数", transactions.size.toString(), Color.White, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HomeSummaryMetric(
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
private fun HomeBudgetProgress(
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
                Text(
                    "◎ 月度预算",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
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
private fun AssetOverviewCard(uiState: AccountingUiState) {
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
private fun AssetMetricPill(
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
private fun AccountRow(row: AccountBalanceRow) {
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
        }
    }
}

@Composable
private fun AddAccountCard(viewModel: AccountingViewModel) {
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

@Composable
private fun EntrySheetContent(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onDone: () -> Unit
) {
    val editing = uiState.editingTransaction
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var accountId by remember { mutableStateOf<Long?>(null) }
    var fromAccountId by remember { mutableStateOf<Long?>(null) }
    var toAccountId by remember { mutableStateOf<Long?>(null) }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var merchant by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedTagIds by remember { mutableStateOf(setOf<Long>()) }
    var occurredAt by remember { mutableStateOf(System.currentTimeMillis()) }
    var formError by remember { mutableStateOf<String?>(null) }
    var cursorVisible by remember { mutableStateOf(true) }

    fun clearForm() {
        amount = ""
        merchant = ""
        note = ""
        formError = null
        selectedTagIds = emptySet()
        categoryId = null
        occurredAt = System.currentTimeMillis()
    }

    LaunchedEffect(editing?.transaction?.id) {
        if (editing != null) {
            val transaction = editing.transaction
            selectedType = transaction.type
            amount = Money(transaction.amountCents).formatPlain()
            accountId = transaction.accountId
            fromAccountId = transaction.fromAccountId
            toAccountId = transaction.toAccountId
            categoryId = transaction.categoryId
            merchant = transaction.merchant
            note = transaction.note
            selectedTagIds = editing.tags.map { it.id }.toSet()
            occurredAt = transaction.occurredAt
        } else {
            clearForm()
        }
    }

    val categories = when (selectedType) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }

    fun submit() {
        val submittedAmount = normalizedAmountInput(amount)
        val validationError = validateEntryDraft(
            type = selectedType,
            amount = submittedAmount,
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId
        )
        if (validationError != null) {
            formError = validationError
            return
        }
        if (editing == null) {
            when (selectedType) {
                TransactionType.EXPENSE -> viewModel.addExpense(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                TransactionType.INCOME -> viewModel.addIncome(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                TransactionType.TRANSFER -> viewModel.addTransfer(submittedAmount, fromAccountId, toAccountId, note, selectedTagIds.toList(), occurredAt)
                TransactionType.BALANCE_ADJUSTMENT -> viewModel.addBalanceAdjustment(submittedAmount, accountId, note, selectedTagIds.toList(), occurredAt)
            }
            clearForm()
        } else {
            viewModel.saveEditedTransaction(
                transactionId = editing.transaction.id,
                type = selectedType,
                amount = submittedAmount,
                accountId = accountId,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = selectedTagIds.toList(),
                occurredAt = occurredAt
            )
        }
        onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 720.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (editing == null) "新增流水" else "编辑流水",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (editing != null) {
                        FilledTonalButton(
                            onClick = {
                                viewModel.cancelEditTransaction()
                                clearForm()
                            }
                        ) {
                            Text("取消")
                        }
                    }
                }
                TypeSelector(selectedType = selectedType, onTypeSelected = {
                    selectedType = it
                    categoryId = null
                })
                DateTimeSelector(
                    occurredAt = occurredAt,
                    onChanged = { occurredAt = it }
                )
                MinimalInputLine(
                    value = amount,
                    onValueChange = {},
                    placeholder = "金额",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                AmountKeypad(
                    value = amount,
                    onValueChange = { amount = it }
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
                if (uiState.tags.isNotEmpty()) {
                    TagSelector(
                        tags = uiState.tags,
                        selectedTagIds = selectedTagIds,
                        onToggle = { tagId ->
                            selectedTagIds = if (tagId in selectedTagIds) {
                                selectedTagIds - tagId
                            } else {
                                selectedTagIds + tagId
                            }
                        }
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
                Button(
                    onClick = {
                        val submittedAmount = normalizedAmountInput(amount)
                        if (editing == null) {
                            when (selectedType) {
                                TransactionType.EXPENSE -> viewModel.addExpense(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                                TransactionType.INCOME -> viewModel.addIncome(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                                TransactionType.TRANSFER -> viewModel.addTransfer(submittedAmount, fromAccountId, toAccountId, note, selectedTagIds.toList(), occurredAt)
                                TransactionType.BALANCE_ADJUSTMENT -> viewModel.addBalanceAdjustment(submittedAmount, accountId, note, selectedTagIds.toList(), occurredAt)
                            }
                            clearForm()
                        } else {
                            viewModel.saveEditedTransaction(
                                transactionId = editing.transaction.id,
                                type = selectedType,
                                amount = submittedAmount,
                                accountId = accountId,
                                fromAccountId = fromAccountId,
                                toAccountId = toAccountId,
                                categoryId = categoryId,
                                merchant = merchant,
                                note = note,
                                tagIds = selectedTagIds.toList(),
                                occurredAt = occurredAt
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(if (editing == null) Icons.Default.Add else Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (editing == null) "保存记录" else "保存修改")
                }
            }
        }
    }
}

@Composable
private fun EntrySheetContentV2(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onDone: () -> Unit
) {
    val editing = uiState.editingTransaction
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var accountId by remember { mutableStateOf<Long?>(null) }
    var fromAccountId by remember { mutableStateOf<Long?>(null) }
    var toAccountId by remember { mutableStateOf<Long?>(null) }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var merchant by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedTagIds by remember { mutableStateOf(setOf<Long>()) }
    var occurredAt by remember { mutableStateOf(System.currentTimeMillis()) }
    var formError by remember { mutableStateOf<String?>(null) }
    var cursorVisible by remember { mutableStateOf(true) }
    var detailsExpanded by remember { mutableStateOf(false) }

    fun clearForm() {
        amount = ""
        merchant = ""
        note = ""
        formError = null
        detailsExpanded = false
        selectedTagIds = emptySet()
        categoryId = null
        occurredAt = System.currentTimeMillis()
        accountId = uiState.activeAccounts.firstOrNull()?.id
        fromAccountId = uiState.activeAccounts.firstOrNull()?.id
        toAccountId = uiState.activeAccounts.drop(1).firstOrNull()?.id
    }

    LaunchedEffect(editing?.transaction?.id, uiState.activeAccounts.size) {
        if (editing != null) {
            val transaction = editing.transaction
            selectedType = transaction.type
            amount = Money(transaction.amountCents).formatPlain()
            accountId = transaction.accountId
            fromAccountId = transaction.fromAccountId
            toAccountId = transaction.toAccountId
            categoryId = transaction.categoryId
            merchant = transaction.merchant
            note = transaction.note
            detailsExpanded = transaction.merchant.isNotBlank() || transaction.note.isNotBlank()
            selectedTagIds = editing.tags.map { it.id }.toSet()
            occurredAt = transaction.occurredAt
        } else if (amount.isBlank() && accountId == null && fromAccountId == null) {
            clearForm()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(520)
            cursorVisible = !cursorVisible
        }
    }

    val categories = when (selectedType) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }
    val sortedCategories = remember(categories, uiState.recentTransactions) {
        categories.sortedByCommonUsage(uiState.recentTransactions)
    }
    val selectedCategory = categories.firstOrNull { it.id == categoryId }
    val amountColor = transactionColor(selectedType)

    fun submit() {
        val submittedAmount = normalizedAmountInput(amount)
        val validationError = validateEntryDraft(
            type = selectedType,
            amount = submittedAmount,
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId
        )
        if (validationError != null) {
            formError = validationError
            return
        }
        if (editing == null) {
            when (selectedType) {
                TransactionType.EXPENSE -> viewModel.addExpense(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                TransactionType.INCOME -> viewModel.addIncome(submittedAmount, accountId, categoryId, merchant, note, selectedTagIds.toList(), occurredAt)
                TransactionType.TRANSFER -> viewModel.addTransfer(submittedAmount, fromAccountId, toAccountId, note, selectedTagIds.toList(), occurredAt)
                TransactionType.BALANCE_ADJUSTMENT -> viewModel.addBalanceAdjustment(submittedAmount, accountId, note, selectedTagIds.toList(), occurredAt)
            }
            clearForm()
        } else {
            viewModel.saveEditedTransaction(
                transactionId = editing.transaction.id,
                type = selectedType,
                amount = submittedAmount,
                accountId = accountId,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = selectedTagIds.toList(),
                occurredAt = occurredAt
            )
        }
        onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        viewModel.cancelEditTransaction()
                        onDone()
                    }
                ) {
                    Text("取消", color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    if (editing == null) "记一笔" else "编辑流水",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = ::submit) {
                    Text("完成", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LedgerCard {
                TypeSelector(selectedType = selectedType, onTypeSelected = {
                    selectedType = it
                    categoryId = null
                    formError = null
                })

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "金额",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "¥${amount.ifBlank { "0" }}${if (cursorVisible) "|" else " "}",
                        color = if (selectedType == TransactionType.EXPENSE) MaterialTheme.colorScheme.onSurface else amountColor,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                formError?.let { error ->
                    Text(
                        error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }

                if (categories.isNotEmpty()) {
                    EntryCategoryGrid(
                        categories = sortedCategories,
                        selectedCategoryId = categoryId,
                        onSelected = {
                            categoryId = it
                            formError = null
                        }
                    )
                }

                if (selectedType == TransactionType.EXPENSE && selectedCategory != null) {
                    CategoryBudgetHint(
                        uiState = uiState,
                        category = selectedCategory,
                        amount = normalizedAmountInput(amount),
                        editing = editing
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    EntryActionPill(
                        label = if (note.isBlank()) "添加备注" else "已备注",
                        icon = Icons.Default.Edit,
                        modifier = Modifier.weight(1f),
                        onClick = { detailsExpanded = !detailsExpanded }
                    )
                    EntryActionPill(
                        label = dateChipLabel(occurredAt),
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f),
                        onClick = { detailsExpanded = true }
                    )
                    EntryActionPill(
                        label = if (selectedType == TransactionType.TRANSFER) "转账账户" else selectedAccountName(uiState.activeAccounts, accountId),
                        icon = Icons.Default.Payments,
                        modifier = Modifier.weight(1f),
                        onClick = { detailsExpanded = true }
                    )
                }

                if (detailsExpanded) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (selectedType == TransactionType.TRANSFER) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    AccountPickerField(
                                        label = "转出",
                                        accounts = uiState.activeAccounts,
                                        selectedAccountId = fromAccountId,
                                        onSelected = {
                                            fromAccountId = it
                                            formError = null
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    AccountPickerField(
                                        label = "转入",
                                        accounts = uiState.activeAccounts,
                                        selectedAccountId = toAccountId,
                                        onSelected = {
                                            toAccountId = it
                                            formError = null
                                        }
                                    )
                                }
                            }
                        } else {
                            AccountPickerField(
                                label = "账户",
                                accounts = uiState.activeAccounts,
                                selectedAccountId = accountId,
                                onSelected = {
                                    accountId = it
                                    formError = null
                                }
                            )
                        }
                        DateTimeSelector(
                            occurredAt = occurredAt,
                            onChanged = { occurredAt = it }
                        )
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
                            minLines = 1,
                            maxLines = 3
                        )
                        if (uiState.tags.isNotEmpty()) {
                            TagSelector(
                                tags = uiState.tags,
                                selectedTagIds = selectedTagIds,
                                onToggle = { tagId ->
                                    selectedTagIds = if (tagId in selectedTagIds) {
                                        selectedTagIds - tagId
                                    } else {
                                        selectedTagIds + tagId
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        AmountKeypad(
            value = amount,
            onValueChange = {
                amount = it
                formError = null
            },
            onConfirm = ::submit
        )
    }
}

@Composable
private fun EntryCategoryGrid(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long) -> Unit
) {
    val rows = categories.take(10).chunked(5)
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { category ->
                    EntryCategoryTile(
                        category = category,
                        selected = category.id == selectedCategoryId,
                        onClick = { onSelected(category.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(5 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EntryCategoryTile(
    category: CategoryEntity,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = Color(category.colorArgb)
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(8.dp),
            color = if (selected) LedgerMint else tint.copy(alpha = 0.11f),
            border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.32f)) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    categoryIcon(category.iconName),
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.primary else tint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(
            category.name,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EntryActionPill(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun MinimalInputLine(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null
) {
    var focused by remember { mutableStateOf(false) }
    val lineColor = if (focused) MaterialTheme.colorScheme.primary else LedgerDivider

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
                .onFocusChanged { focused = it.isFocused },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            ),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 9.dp)
                ) {
                    if (value.isBlank()) {
                        Text(
                            placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            }
        )
        HorizontalDivider(color = lineColor, thickness = if (focused) 1.5.dp else 1.dp)
    }
}

private data class QuickEntryDraft(
    val type: TransactionType,
    val amount: String,
    val accountId: Long?,
    val categoryId: Long?,
    val merchant: String,
    val occurredAt: Long
)

private fun selectedAccountName(
    accounts: List<AccountEntity>,
    selectedAccountId: Long?
): String {
    return accounts.firstOrNull { it.id == selectedAccountId }?.name ?: "现金"
}

private fun List<CategoryEntity>.sortedByCommonUsage(
    recentTransactions: List<TransactionWithDetails>
): List<CategoryEntity> {
    val usageScore = mutableMapOf<Long, Int>()
    recentTransactions.forEachIndexed { index, item ->
        val categoryId = item.transaction.categoryId ?: return@forEachIndexed
        val recencyScore = (recentTransactions.size - index).coerceAtLeast(1)
        usageScore[categoryId] = (usageScore[categoryId] ?: 0) + 100 + recencyScore
    }
    return sortedWith(
        compareByDescending<CategoryEntity> { usageScore[it.id] ?: 0 }
            .thenBy { it.sortOrder }
            .thenBy { it.createdAt }
    )
}

private fun dateChipLabel(millis: Long): String {
    val date = localDateFromMillis(millis)
    val today = LocalDate.now()
    return when (date) {
        today -> "今天"
        today.minusDays(1) -> "昨天"
        today.minusDays(2) -> "前天"
        else -> date.format(DateTimeFormatter.ofPattern("M月d日", Locale.CHINA))
    }
}

private fun remindersForDate(
    rules: List<RecurringRuleEntity>,
    date: LocalDate
): List<RecurringRuleEntity> {
    return rules
        .filter { it.isEnabled && localDateFromMillis(it.nextRunAt) == date }
        .sortedBy { it.nextRunAt }
}

private fun validateEntryDraft(
    type: TransactionType,
    amount: String,
    accountId: Long?,
    fromAccountId: Long?,
    toAccountId: Long?
): String? {
    if (hasUnresolvedAmountExpression(amount)) {
        return "请先完成金额计算"
    }
    val cents = Money.fromMajor(amount).cents
    if (cents == 0L || (cents < 0 && type != TransactionType.BALANCE_ADJUSTMENT)) {
        return "请输入有效金额"
    }
    return when (type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> if (accountId == null) "请选择账户" else null
        TransactionType.TRANSFER -> when {
            fromAccountId == null -> "请选择转出账户"
            toAccountId == null -> "请选择转入账户"
            fromAccountId == toAccountId -> "转出账户和转入账户不能相同"
            else -> null
        }
        TransactionType.BALANCE_ADJUSTMENT -> if (accountId == null) "请选择账户" else null
    }
}

private fun parseQuickEntry(
    input: String,
    uiState: AccountingUiState
): QuickEntryDraft? {
    val text = input.trim()
    val amount = extractQuickEntryAmount(text)
        ?: return null
    val type = if (listOf("收入", "工资", "薪资", "奖金", "报销", "到账").any { text.contains(it) }) {
        TransactionType.INCOME
    } else {
        TransactionType.EXPENSE
    }
    val account = findQuickEntryAccount(text, uiState.activeAccounts) ?: uiState.activeAccounts.firstOrNull()
    val categories = if (type == TransactionType.INCOME) uiState.incomeCategories else uiState.expenseCategories
    val category = findQuickEntryCategory(text, categories)
    val occurredAt = parseQuickEntryTime(text)
    val merchant = stripQuickEntryMeta(text, amount, account, category)
        .trim()
        .ifBlank { category?.name ?: transactionLabel(type) }

    return QuickEntryDraft(
        type = type,
        amount = amount,
        accountId = account?.id,
        categoryId = category?.id,
        merchant = merchant,
        occurredAt = occurredAt
    )
}

private fun extractQuickEntryAmount(text: String): String? {
    val withoutDateTime = text
        .replace(Regex("""\d{4}[年/-]\d{1,2}[月/-]\d{1,2}日?"""), " ")
        .replace(Regex("""\d{1,2}月\d{1,2}日?"""), " ")
        .replace(Regex("""\d{1,2}/\d{1,2}"""), " ")
        .replace(Regex("""\d{1,2}:\d{1,2}"""), " ")
        .replace(Regex("""\d{1,2}点(?:\d{1,2}分?)?"""), " ")
    return Regex("""\d+(?:\.\d{1,2})?""")
        .findAll(withoutDateTime)
        .lastOrNull()
        ?.value
}

private fun findQuickEntryAccount(
    text: String,
    accounts: List<AccountEntity>
): AccountEntity? {
    return accounts.firstOrNull { account ->
        text.contains(account.name, ignoreCase = true)
    } ?: accounts.firstOrNull { account ->
        quickAccountAliases(account).any { alias -> text.contains(alias, ignoreCase = true) }
    }
}

private fun quickAccountAliases(account: AccountEntity): List<String> {
    return when {
        account.name.contains("微信") -> listOf("微信", "wx")
        account.name.contains("支付宝") -> listOf("支付宝", "花呗")
        account.type == AccountType.CASH -> listOf("现金")
        account.type == AccountType.BANK_CARD -> listOf("银行卡", "银行", "储蓄卡")
        account.type == AccountType.CREDIT -> listOf("信用卡")
        account.type == AccountType.TRANSIT_CARD -> listOf("公交卡", "交通卡")
        else -> emptyList()
    }
}

private fun findQuickEntryCategory(
    text: String,
    categories: List<CategoryEntity>
): CategoryEntity? {
    return categories.firstOrNull { category ->
        text.contains(category.name, ignoreCase = true)
    } ?: categories.firstOrNull { category ->
        quickCategoryAliases(category.name).any { alias -> text.contains(alias, ignoreCase = true) }
    }
}

private fun quickCategoryAliases(categoryName: String): List<String> {
    return when (categoryName) {
        "餐饮" -> listOf("早餐", "早饭", "午饭", "午餐", "晚饭", "晚餐", "外卖", "咖啡", "奶茶", "餐厅", "吃饭")
        "交通" -> listOf("地铁", "公交", "打车", "出租", "高铁", "火车", "机票", "停车", "加油")
        "购物" -> listOf("购物", "超市", "淘宝", "京东", "拼多多", "买了", "采购")
        "数码服务" -> listOf("会员", "订阅", "软件", "手机", "流量", "宽带", "数码")
        "生活缴费" -> listOf("电费", "水费", "燃气", "物业", "房租", "话费", "缴费")
        "工资" -> listOf("工资", "薪资", "薪水", "奖金")
        "优惠/赠送" -> listOf("红包", "优惠", "赠送", "返现")
        "其他收入" -> listOf("收入", "报销", "到账")
        else -> emptyList()
    }
}

private fun stripQuickEntryMeta(
    text: String,
    amount: String,
    account: AccountEntity?,
    category: CategoryEntity?
): String {
    val withoutDateTime = text
        .replace(Regex("""\d{4}[年/-]\d{1,2}[月/-]\d{1,2}日?"""), "")
        .replace(Regex("""\d{1,2}月\d{1,2}日?"""), "")
        .replace(Regex("""\d{1,2}/\d{1,2}"""), "")
        .replace(Regex("""\d{1,2}:\d{1,2}"""), "")
        .replace(Regex("""\d{1,2}点(?:\d{1,2}分?)?"""), "")
    return withoutDateTime
        .replace(amount, "")
        .replace("今天", "")
        .replace("昨天", "")
        .replace("前天", "")
        .replace("明天", "")
        .let { raw ->
            val withoutAccount = account?.let { raw.replace(it.name, "") } ?: raw
            category?.let { withoutAccount.replace(it.name, "") } ?: withoutAccount
        }
        .replace("收入", "")
        .replace("支出", "")
}

private fun parseQuickEntryTime(text: String): Long {
    val calendar = Calendar.getInstance()
    val absoluteDate = Regex("""(?:(\d{4})[年/-])?(\d{1,2})[月/-](\d{1,2})日?""").find(text)
    if (absoluteDate != null) {
        val year = absoluteDate.groupValues[1].takeIf { it.isNotBlank() }?.toInt()
            ?: calendar.get(Calendar.YEAR)
        val month = absoluteDate.groupValues[2].toInt()
        val day = absoluteDate.groupValues[3].toInt()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
    } else when {
        text.contains("前天") -> calendar.add(Calendar.DAY_OF_MONTH, -2)
        text.contains("昨天") -> calendar.add(Calendar.DAY_OF_MONTH, -1)
        text.contains("明天") -> calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    Regex("""(\d{1,2})(?::|点)(\d{1,2})?分?""").find(text)?.let { match ->
        calendar.set(Calendar.HOUR_OF_DAY, match.groupValues[1].toInt().coerceIn(0, 23))
        calendar.set(Calendar.MINUTE, match.groupValues[2].takeIf { it.isNotBlank() }?.toInt()?.coerceIn(0, 59) ?: 0)
    }
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

@Composable
private fun CategoryBudgetHint(
    uiState: AccountingUiState,
    category: CategoryEntity,
    amount: String,
    editing: TransactionWithDetails?
) {
    val budget = uiState.categoryBudgets.firstOrNull { it.categoryId == category.id }
    val spentCents = uiState.monthlyExpenseByCategory
        .firstOrNull { it.categoryId == category.id }
        ?.amountCents ?: 0
    val editingDeduction = editing?.transaction
        ?.takeIf {
            it.type == TransactionType.EXPENSE &&
                it.categoryId == category.id &&
                isInCurrentMonth(it.occurredAt)
        }
        ?.amountCents ?: 0
    val enteredCents = if (hasUnresolvedAmountExpression(amount)) {
        0
    } else {
        Money.fromMajor(amount).cents.coerceAtLeast(0)
    }
    val projectedCents = (spentCents - editingDeduction).coerceAtLeast(0) + enteredCents
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${category.name}预算", fontWeight = FontWeight.SemiBold, color = tint)
                Text(
                    if (budgetCents > 0) "${Money(projectedCents).format()} / ${Money(budgetCents).format()}" else "未设置",
                    color = tint,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace
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
                        "本次提交后预计超支 ${Money(projectedCents - budgetCents).format()}"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
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
            containerColor = MaterialTheme.colorScheme.surface
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
private fun DateTimeWheelSheet(
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
private fun WheelPickerColumn(
    values: List<Int>,
    selected: Int,
    label: (Int) -> String,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.height(188.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(vertical = 46.dp)
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

private fun dateTimeToMillis(
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

@Composable
private fun AmountKeypad(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit = {}
) {
    val rows = listOf(
        listOf("7", "8", "9", "÷"),
        listOf("4", "5", "6", "×"),
        listOf("1", "2", "3", "-"),
        listOf(".", "0", "⌫", "ok")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { key ->
                        AmountKey(
                            label = key,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (key == "ok") {
                                    onConfirm()
                                } else {
                                    onValueChange(handleAmountKey(value, key))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AmountKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isOperator = label in setOf("+", "-", "×", "÷")
    val isConfirm = label == "ok"
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        label = "amountKeyScale"
    )
    val haptic = LocalHapticFeedback.current
    val clickWithFeedback = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onClick()
    }

    Surface(
        modifier = modifier
            .height(66.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = clickWithFeedback
            ),
        color = if (isConfirm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.5.dp, LedgerDivider)
    ) {
        Box(contentAlignment = Alignment.Center) {
            when {
                label == "⌫" -> Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "退格",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                isConfirm -> Icon(
                    Icons.Default.Add,
                    contentDescription = "完成",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                else -> Text(
                    label,
                    color = if (isOperator) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountPickerField(
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
        ModalBottomSheet(onDismissRequest = { expanded = false }) {
            SelectionSheetHeader(title = label, subtitle = "${accounts.size} 个账户")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                contentPadding = PaddingValues(16.dp),
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerField(
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
        ModalBottomSheet(onDismissRequest = { expanded = false }) {
            SelectionSheetHeader(title = label, subtitle = "${categories.size} 个分类")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                contentPadding = PaddingValues(16.dp),
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
    }
}

@Composable
private fun PickerField(
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
private fun SelectionSheetHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AccountGridItem(
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
private fun CategoryGridItem(
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
private fun SelectionGridItem(
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
private fun TagSelector(
    tags: List<TagEntity>,
    selectedTagIds: Set<Long>,
    onToggle: (Long) -> Unit
) {
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
        }
    }
}

@Composable
private fun TagPill(
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
private fun TypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    val types = listOf(
        TransactionType.EXPENSE to "支出",
        TransactionType.INCOME to "收入",
        TransactionType.TRANSFER to "转账",
        TransactionType.BALANCE_ADJUSTMENT to "校正"
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

@Composable
private fun LedgerScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    var viewMode by remember { mutableStateOf(LedgerViewMode.List) }
    val displayedTransactions = uiState.searchResults.filter { transaction ->
        selectedCategoryId == null || transaction.transaction.categoryId == selectedCategoryId
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LedgerViewModeSelector(
            selectedMode = viewMode,
            onSelected = { viewMode = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            when (viewMode) {
                LedgerViewMode.List -> LedgerListContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = { selectedCategoryId = it },
                    displayedTransactions = displayedTransactions,
                    onEditTransaction = onEditTransaction,
                    onLongPress = { actionTarget = it }
                )
                LedgerViewMode.Calendar -> CalendarScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onEditTransaction = onEditTransaction
                )
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
}

@Composable
private fun LedgerListContent(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit,
    displayedTransactions: List<TransactionWithDetails>,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    onLongPress: (TransactionWithDetails) -> Unit
) {
    val searchFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { searchFocusRequester.requestFocus() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    MinimalInputLine(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::setSearchQuery,
                        placeholder = "搜索商户、备注、分类、账户、标签",
                        modifier = Modifier.weight(1f),
                        focusRequester = searchFocusRequester
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    LedgerTextFilter(
                        selected = uiState.searchType == null,
                        label = "全部",
                        onClick = { viewModel.setSearchType(null) }
                    )
                    listOf(TransactionType.EXPENSE, TransactionType.INCOME, TransactionType.TRANSFER).forEach { type ->
                        LedgerTextFilter(
                            selected = uiState.searchType == type,
                            label = transactionLabel(type),
                            onClick = { viewModel.setSearchType(type) }
                        )
                    }
                }
                AccountFilterRow(uiState, viewModel)
                CategoryFilterRow(
                    categories = uiState.expenseCategories + uiState.incomeCategories,
                    selectedCategoryId = selectedCategoryId,
                    onSelected = onCategorySelected
                )
            }
        }
        transactionDayGroups(
            transactions = displayedTransactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "没有匹配的流水",
            showInlineActions = false,
            onLongPress = onLongPress
        )
    }
}

@Composable
private fun LedgerViewModeSelector(
    selectedMode: LedgerViewMode,
    onSelected: (LedgerViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LedgerViewMode.entries.forEach { mode ->
                val selected = selectedMode == mode
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clickable { onSelected(mode) },
                    shape = RoundedCornerShape(7.dp),
                    color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            mode.label,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LedgerTextFilter(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
    )
}

private fun LazyListScope.transactionDayGroups(
    transactions: List<TransactionWithDetails>,
    onEditTransaction: (TransactionWithDetails) -> Unit,
    onDeleteTransaction: (TransactionWithDetails) -> Unit,
    emptyText: String = "暂无流水",
    showInlineActions: Boolean = true,
    onLongPress: ((TransactionWithDetails) -> Unit)? = null,
    groupModifier: Modifier = Modifier
) {
    val groups = transactions.groupedByDay()
    if (groups.isEmpty()) {
        item {
            Text(
                emptyText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    groups.forEach { group ->
        item(key = "day-${group.date}") {
            Column(modifier = groupModifier.fillMaxWidth()) {
                TransactionDayHeader(group)
                group.items.forEachIndexed { index, transaction ->
                    TransactionRow(
                        item = transaction,
                        onLongClick = onLongPress?.let { longPress -> { longPress(transaction) } },
                        trailing = {
                            if (showInlineActions) {
                                Row {
                                    IconButton(onClick = { onEditTransaction(transaction) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                                    }
                                    IconButton(onClick = { onDeleteTransaction(transaction) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "移入回收站")
                                    }
                                }
                            }
                        }
                    )
                    if (index != group.items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 52.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.42f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionDayHeader(group: TransactionDayGroup) {
    val summaryParts = buildList {
        if (group.expenseCents > 0) add("支出 ${Money(group.expenseCents).format()}")
        if (group.incomeCents > 0) add("收入 ${Money(group.incomeCents).format()}")
    }
    val today = LocalDate.now()
    val dayLabel = when (group.date) {
        today -> "今天"
        today.minusDays(1) -> "昨天"
        today.minusDays(2) -> "前天"
        else -> weekdayLabel(group.date)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$dayLabel · ${group.date.format(DateTimeFormatter.ofPattern("M月d日", Locale.CHINA))}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Text(
            summaryParts.joinToString(" · ").ifBlank { "无收支" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun CalendarScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    val monthStart = localDateFromMillis(uiState.calendarMonthStartMillis)
    val selectedDate = localDateFromMillis(uiState.calendarSelectedDateMillis)
    val selectedTransactions = uiState.calendarMonthTransactions.filter {
        localDateFromMillis(it.transaction.occurredAt) == selectedDate
    }
    val selectedReminders = remindersForDate(uiState.recurringRules, selectedDate)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = { viewModel.moveCalendarMonth(-1) }) {
                    Text("上月")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${monthStart.year}年${monthStart.monthValue}月",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "日历账单",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                FilledTonalButton(onClick = { viewModel.moveCalendarMonth(1) }) {
                    Text("下月")
                }
            }
        }
        item {
            CalendarMonthGrid(
                monthStart = monthStart,
                selectedDate = selectedDate,
                transactions = uiState.calendarMonthTransactions,
                recurringRules = uiState.recurringRules,
                onSelected = { viewModel.selectCalendarDate(localDateStartMillis(it)) }
            )
        }
        item {
            FilledTonalButton(
                onClick = viewModel::resetCalendarToToday,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("回到今天")
            }
        }
        item {
            SectionHeader(
                "${selectedDate.monthValue}月${selectedDate.dayOfMonth}日 ${weekdayLabel(selectedDate)}",
                "${selectedTransactions.size} 条 · ${selectedReminders.size} 提醒"
            )
        }
        if (selectedReminders.isNotEmpty()) {
            item {
                CalendarReminderList(selectedReminders)
            }
        }
        transactionDayGroups(
            transactions = selectedTransactions,
            onEditTransaction = onEditTransaction,
            onDeleteTransaction = { viewModel.deleteTransaction(it.transaction.id) },
            emptyText = "当天暂无流水"
        )
    }
}

@Composable
private fun CalendarMonthGrid(
    monthStart: LocalDate,
    selectedDate: LocalDate,
    transactions: List<TransactionWithDetails>,
    recurringRules: List<RecurringRuleEntity>,
    onSelected: (LocalDate) -> Unit
) {
    val groupsByDate = transactions.groupedByDay().associateBy { it.date }
    val remindersByDate = recurringRules
        .filter { it.isEnabled }
        .groupBy { localDateFromMillis(it.nextRunAt) }
    val days = calendarMonthCells(monthStart)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("一", "二", "三", "四", "五", "六", "日").forEach { label ->
                    Text(
                        label,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            days.chunked(7).forEach { week ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { day ->
                        val group = groupsByDate[day.date]
                        val reminderCount = remindersByDate[day.date].orEmpty().size
                        CalendarDayCell(
                            day = day,
                            selected = day.date == selectedDate,
                            group = group,
                            reminderCount = reminderCount,
                            onClick = { onSelected(day.date) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDay,
    selected: Boolean,
    group: TransactionDayGroup?,
    reminderCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = when {
        selected -> MaterialTheme.colorScheme.primaryContainer
        day.inMonth -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f)
    }
    Surface(
        modifier = modifier
            .aspectRatio(0.88f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = container,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (selected) 1f else 0.34f))
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                day.date.dayOfMonth.toString(),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (day.inMonth) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            group?.let {
                if (it.expenseCents > 0) {
                    Text(
                        "-${Money(it.expenseCents).formatPlain()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Monospace
                    )
                }
                if (it.incomeCents > 0) {
                    Text(
                        "+${Money(it.incomeCents).formatPlain()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            if (reminderCount > 0) {
                Text(
                    "提醒 $reminderCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CalendarReminderList(reminders: List<RecurringRuleEntity>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            reminders.forEach { rule ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(rule.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            listOf(transactionLabel(rule.transactionType), rule.merchant, rule.note)
                                .filter { it.isNotBlank() }
                                .joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        Money(rule.amountCents).format(),
                        color = transactionColor(rule.transactionType),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountFilterRow(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LedgerTextFilter(
            selected = uiState.selectedAccountId == null,
            onClick = { viewModel.setSelectedAccount(null) },
            label = "全部账户"
        )
        uiState.activeAccounts.forEach { account ->
            LedgerTextFilter(
                selected = uiState.selectedAccountId == account.id,
                onClick = { viewModel.setSelectedAccount(account.id) },
                label = account.name
            )
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long?) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LedgerTextFilter(
            selected = selectedCategoryId == null,
            onClick = { onSelected(null) },
            label = "全部分类"
        )
        categories.forEach { category ->
            LedgerTextFilter(
                selected = selectedCategoryId == category.id,
                onClick = { onSelected(category.id) },
                label = category.name
            )
        }
    }
}

@Composable
private fun StatsScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<CategorySummary?>(null) }
    val expenseRows = uiState.summary?.expenseByCategory.orEmpty()
    val totalExpenseCents = expenseRows.sumOf { it.amountCents }

    BackHandler(enabled = selectedCategory != null) {
        selectedCategory = null
    }

    AnimatedContent(
        targetState = selectedCategory,
        transitionSpec = {
            (fadeIn(animationSpec = tween(180)) + scaleIn(initialScale = 0.94f, animationSpec = tween(180))) togetherWith
                (fadeOut(animationSpec = tween(120)) + scaleOut(targetScale = 1.04f, animationSpec = tween(120)))
        },
        label = "statsSharedAxisZ"
    ) { category ->
        if (category == null) {
            StatsOverviewContent(
                uiState = uiState,
                viewModel = viewModel,
                expenseRows = expenseRows,
                totalExpenseCents = totalExpenseCents,
                onSelectCategory = { selectedCategory = it }
            )
        } else {
            StatsCategoryDetailScreen(
                uiState = uiState,
                viewModel = viewModel,
                category = category,
                onBack = { selectedCategory = null },
                onEditTransaction = onEditTransaction
            )
        }
    }
}

@Composable
private fun StatsOverviewContent(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    expenseRows: List<CategorySummary>,
    totalExpenseCents: Long,
    onSelectCategory: (CategorySummary) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PeriodSelector(uiState.selectedPeriod, viewModel::setPeriod)
        }
        item {
            StatsSummaryCard(uiState)
        }
        item {
            ExpenseDonutChart(rows = expenseRows, totalCents = totalExpenseCents)
        }
        item {
            LedgerCard {
                SectionHeader("支出分类", "${expenseRows.size} 类")
                if (expenseRows.isEmpty()) {
                    Text(
                        "本期暂无支出分类",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    expenseRows.forEachIndexed { index, row ->
                        CategorySummaryRow(
                            name = row.categoryName ?: "未分类",
                            cents = row.amountCents,
                            totalCents = totalExpenseCents,
                            onClick = { onSelectCategory(row) }
                        )
                        if (index != expenseRows.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.56f))
                        }
                    }
                }
            }
        }
        item {
            MonthlyTrendBarChart(uiState.trendTransactions)
        }
    }
}

@Composable
private fun StatsSummaryCard(uiState: AccountingUiState) {
    LedgerCard {
        SectionHeader(uiState.summary?.periodLabel.orEmpty().ifBlank { "当前周期" }, "收支总览")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricTile(
                label = "支出",
                cents = uiState.summary?.totals?.expenseCents ?: 0,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            StatMetricTile(
                label = "收入",
                cents = uiState.summary?.totals?.incomeCents ?: 0,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricTile(
                label = "转账流水",
                cents = uiState.summary?.totals?.transferOutCents ?: 0,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            StatMetricTile(
                label = "净收入",
                cents = (uiState.summary?.totals?.incomeCents ?: 0) - (uiState.summary?.totals?.expenseCents ?: 0),
                color = MaterialTheme.colorScheme.tertiary,
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
private fun StatMetricTile(
    label: String,
    cents: Long,
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
                Money(cents).format(),
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
private fun StatsCategoryDetailScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    category: CategorySummary,
    onBack: () -> Unit,
    onEditTransaction: (TransactionWithDetails) -> Unit
) {
    var actionTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    val transactions = uiState.periodTransactions.filter { item ->
        item.transaction.type == TransactionType.EXPENSE && item.transaction.categoryId == category.categoryId
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
                        .clickable(onClick = onBack),
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
                    Text(category.categoryName ?: "未分类", fontWeight = FontWeight.SemiBold)
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
private fun PeriodSelector(
    selected: StatsPeriod,
    onSelected: (StatsPeriod) -> Unit
) {
    TabRow(selectedTabIndex = StatsPeriod.entries.indexOf(selected)) {
        StatsPeriod.entries.forEach { period ->
            Tab(
                selected = selected == period,
                onClick = { onSelected(period) },
                text = {
                    Text(
                        when (period) {
                            StatsPeriod.WEEK -> "周"
                            StatsPeriod.MONTH -> "月"
                            StatsPeriod.YEAR -> "年"
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun BudgetProgressCard(
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
                Button(onClick = { viewModel.setMonthlyBudget(budgetAmount) }) {
                    Text("设置")
                }
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
private fun CategoryBudgetRow(
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
                Text(category.name, fontWeight = FontWeight.SemiBold)
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
            FilledTonalButton(onClick = { onSetBudget(amount) }) {
                Text("设")
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
private fun RecurringRulesCard(
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
                    Text("周期账单", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
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
            Button(
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
            ) {
                Icon(Icons.Default.EventRepeat, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("添加每月规则")
            }

            uiState.recurringRules.forEach { rule ->
                RecurringRuleRow(rule, viewModel)
            }
    }
}

@Composable
private fun RecurringRuleRow(
    rule: RecurringRuleEntity,
    viewModel: AccountingViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(rule.name, fontWeight = FontWeight.SemiBold)
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
            onCheckedChange = { viewModel.setRecurringRuleEnabled(rule.id, it) }
        )
        IconButton(onClick = { viewModel.deleteRecurringRule(rule.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "删除周期规则")
        }
    }
}

private enum class MinePage(val title: String) {
    Menu("我的"),
    Accounts("账户管理"),
    Categories("分类管理"),
    Budget("预算设置"),
    Data("数据导出"),
    About("关于")
}

@Composable
private fun MineScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var page by remember { mutableStateOf(MinePage.Menu) }

    BackHandler(enabled = page != MinePage.Menu) {
        page = MinePage.Menu
    }

    when (page) {
        MinePage.Menu -> MineMenu(onOpen = { page = it })
        MinePage.Accounts -> AccountManagementPage(uiState, viewModel, onBack = { page = MinePage.Menu })
        MinePage.Categories -> CategoryManagementPage(uiState, viewModel, onBack = { page = MinePage.Menu })
        MinePage.Budget -> BudgetSettingsPage(uiState, viewModel, onBack = { page = MinePage.Menu })
        MinePage.Data -> DataManagementPage(uiState, viewModel, onBack = { page = MinePage.Menu })
        MinePage.About -> AboutPage(onBack = { page = MinePage.Menu })
    }
}

@Composable
private fun MineMenu(
    onOpen: (MinePage) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "设置",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        item { MineMenuRow("账户管理", "资产账户与初始余额", Icons.Default.AccountBalanceWallet) { onOpen(MinePage.Accounts) } }
        item { MineMenuRow("分类管理", "分类、标签、排序和图标颜色", Icons.Default.Category) { onOpen(MinePage.Categories) } }
        item { MineMenuRow("预算设置", "总预算、分类预算和周期账单", Icons.Default.Assessment) { onOpen(MinePage.Budget) } }
        item { MineMenuRow("数据导出", "JSON / CSV 备份与回收站", Icons.Default.Download) { onOpen(MinePage.Data) } }
        item { MineMenuRow("关于", "本地优先的个人账本", Icons.AutoMirrored.Filled.Label) { onOpen(MinePage.About) } }
    }
}

@Composable
private fun MineMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    LedgerPanelSurface(onClick = onClick) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MineBackHeader(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onBack),
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
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AccountManagementPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("账户管理", onBack) }
        item { AddAccountCard(viewModel) }
        item { SectionHeader("资产账户", "${uiState.accounts.size} 个") }
        items(uiState.accounts, key = { it.account.id }) { row ->
            AccountRow(row)
        }
    }
}

@Composable
private fun CategoryManagementPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("分类管理", onBack) }
        item { CategoryManagementCard(uiState, viewModel) }
        item { TagManagementCard(uiState, viewModel) }
    }
}

@Composable
private fun BudgetSettingsPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("预算设置", onBack) }
        item { BudgetProgressCard(uiState, viewModel) }
        item { RecurringRulesCard(uiState, viewModel) }
    }
}

@Composable
private fun DataManagementPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val jsonSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(uiState.exportContent.toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(context, "JSON 已保存", Toast.LENGTH_SHORT).show()
        }
    }
    val csvSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(uiState.exportContent.toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(context, "CSV 已保存", Toast.LENGTH_SHORT).show()
        }
    }
    val jsonImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val content = context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader(Charsets.UTF_8).readText()
            }
            if (content != null) {
                viewModel.importJson(content)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("数据导出", onBack) }
        item {
            LedgerCard {
                    SectionHeader("备份", "JSON / CSV")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.export(ExportFormat.JSON) }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("生成 JSON")
                        }
                        FilledTonalButton(onClick = { viewModel.export(ExportFormat.CSV) }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("生成 CSV")
                        }
                    }
                    FilledTonalButton(
                        onClick = { jsonImportLauncher.launch(arrayOf("application/json", "text/*")) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("导入 JSON 备份")
                    }
                    if (uiState.exportPreview.isNotBlank()) {
                        FilledTonalButton(
                            onClick = {
                                when (uiState.exportFormat) {
                                    ExportFormat.JSON -> jsonSaveLauncher.launch("accounting-backup.json")
                                    ExportFormat.CSV -> csvSaveLauncher.launch("accounting-transactions.csv")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("保存到文件")
                        }
                        Text(
                            uiState.exportPreview,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                                .verticalScroll(rememberScrollState()),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
            }
        }
        item {
            SectionHeader("回收站", "${uiState.trash.size} 条")
        }
        items(uiState.trash, key = { it.transaction.id }) { transaction ->
            TransactionRow(
                item = transaction,
                trailing = {
                    Row {
                        IconButton(onClick = { viewModel.restoreTransaction(transaction.transaction.id) }) {
                            Icon(Icons.Default.Restore, contentDescription = "恢复")
                        }
                        IconButton(onClick = { viewModel.permanentlyDeleteTransaction(transaction.transaction.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "彻底删除")
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AboutPage(
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("关于", onBack) }
        item {
            InfoCard("个人账本 · 本地优先 · 支持账户、分类、预算、周期账单、导入导出和回收站。")
        }
    }
}

@Composable
private fun TrashAndExportScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    val context = LocalContext.current
    val jsonSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(uiState.exportContent.toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(context, "JSON 已保存", Toast.LENGTH_SHORT).show()
        }
    }
    val csvSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(uiState.exportContent.toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(context, "CSV 已保存", Toast.LENGTH_SHORT).show()
        }
    }
    val jsonImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val content = context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader(Charsets.UTF_8).readText()
            }
            if (content != null) {
                viewModel.importJson(content)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(8.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader("导出备份", "JSON / CSV")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.export(ExportFormat.JSON) }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("生成 JSON")
                        }
                        FilledTonalButton(onClick = { viewModel.export(ExportFormat.CSV) }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("生成 CSV")
                        }
                    }
                    FilledTonalButton(
                        onClick = { jsonImportLauncher.launch(arrayOf("application/json", "text/*")) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("导入 JSON 备份")
                    }
                    if (uiState.exportPreview.isNotBlank()) {
                        FilledTonalButton(
                            onClick = {
                                when (uiState.exportFormat) {
                                    ExportFormat.JSON -> jsonSaveLauncher.launch("accounting-backup.json")
                                    ExportFormat.CSV -> csvSaveLauncher.launch("accounting-transactions.csv")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("保存到文件")
                        }
                        Text(
                            uiState.exportPreview,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                                .verticalScroll(rememberScrollState()),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        item {
            CategoryManagementCard(uiState, viewModel)
        }
        item {
            TagManagementCard(uiState, viewModel)
        }
        item {
            SectionHeader("回收站", "${uiState.trash.size} 条")
        }
        items(uiState.trash, key = { it.transaction.id }) { transaction ->
            TransactionRow(
                item = transaction,
                trailing = {
                    Row {
                        IconButton(onClick = { viewModel.restoreTransaction(transaction.transaction.id) }) {
                            Icon(Icons.Default.Restore, contentDescription = "恢复")
                        }
                        IconButton(onClick = { viewModel.permanentlyDeleteTransaction(transaction.transaction.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "彻底删除")
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionRow(
    item: TransactionWithDetails,
    onLongClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit
) {
    val transaction = item.transaction
    val rowTint = item.category?.let { Color(it.colorArgb) } ?: transactionColor(transaction.type)
    val rowIcon = item.category?.let { categoryIcon(it.iconName) } ?: transactionIcon(transaction.type)
    val title = when (transaction.type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> item.category?.name ?: transactionTitle(item)
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
    val rowModifier = if (onLongClick != null) {
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
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
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
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
                plainAmountLabel(transaction.type, transaction.amountCents),
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
private fun TransactionActionSheet(
    transaction: TransactionWithDetails,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                transactionTitle(transaction),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                amountLabel(transaction.transaction.type, transaction.transaction.amountCents),
                color = transactionColor(transaction.transaction.type),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            FilledTonalButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("编辑")
            }
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("删除")
            }
            Spacer(Modifier.size(12.dp))
        }
    }
}

@Composable
private fun CategoryManagementCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var newCategoryName by remember { mutableStateOf("") }
    var selectedKind by remember { mutableStateOf(CategoryKind.EXPENSE) }
    var categoryToEdit by remember { mutableStateOf<CategoryEntity?>(null) }

    LedgerCard {
            SectionHeader("分类管理", "${uiState.categories.size} 类")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(CategoryKind.EXPENSE, CategoryKind.INCOME).forEach { kind ->
                    FilterChip(
                        selected = selectedKind == kind,
                        onClick = { selectedKind = kind },
                        label = { Text(categoryKindLabel(kind)) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MinimalInputLine(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    placeholder = "新分类名称",
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        viewModel.addCategory(newCategoryName, selectedKind)
                        newCategoryName = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
            uiState.categories.groupBy { it.kind }.forEach { (kind, categories) ->
                Text(
                    categoryKindLabel(kind),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEachIndexed { index, category ->
                        CategoryManagementRow(
                            category = category,
                            canMoveUp = index > 0,
                            canMoveDown = index < categories.lastIndex,
                            onMoveUp = { viewModel.moveCategory(category.id, -1) },
                            onMoveDown = { viewModel.moveCategory(category.id, 1) },
                            onEdit = { categoryToEdit = category },
                            onDelete = { viewModel.deleteCategory(category.id) }
                        )
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
            }
        )
    }
}

@Composable
private fun CategoryManagementRow(
    category: CategoryEntity,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
        Column(modifier = Modifier.weight(1f)) {
            Text(category.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                "排序 ${category.sortOrder}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.Monospace
            )
        }
        IconButton(onClick = onMoveUp, enabled = canMoveUp) {
            Icon(Icons.Default.ArrowUpward, contentDescription = "上移分类")
        }
        IconButton(onClick = onMoveDown, enabled = canMoveDown) {
            Icon(Icons.Default.ArrowDownward, contentDescription = "下移分类")
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "编辑分类")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "归档分类")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEditDialog(
    category: CategoryEntity,
    onDismiss: () -> Unit,
    onSave: (String, String, Long) -> Unit
) {
    var name by remember(category.id) { mutableStateOf(category.name) }
    var iconName by remember(category.id) { mutableStateOf(category.iconName) }
    var colorArgb by remember(category.id) { mutableStateOf(category.colorArgb) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
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
            Text("图标", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryIconOptions(category.kind).forEach { option ->
                    CategoryEditPill(
                        selected = iconName == option,
                        label = categoryIconLabel(option),
                        icon = categoryIcon(option),
                        tint = Color(colorArgb),
                        onClick = { iconName = option }
                    )
                }
            }
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
    }
}

@Composable
private fun CategoryEditPill(
    selected: Boolean,
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(38.dp)
            .clickable(onClick = onClick),
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
private fun TagManagementCard(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    var newTagName by remember { mutableStateOf("") }
    var tagToRename by remember { mutableStateOf<TagEntity?>(null) }

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
                Button(onClick = {
                    if (newTagName.isNotBlank()) {
                        viewModel.addTag(newTagName)
                        newTagName = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.tags.forEach { tag ->
                    AssistChip(
                        onClick = { tagToRename = tag },
                        label = { Text(tag.name) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "删除",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { viewModel.deleteTag(tag.id) }
                            )
                        }
                    )
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
                    FilledTonalButton(
                        onClick = { tagToRename = null },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("取消")
                    }
                    Button(
                        onClick = {
                            tagToRename?.let { viewModel.renameTag(it.id, renamedName) }
                            tagToRename = null
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("保存")
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
private fun SummaryMetric(
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
private fun ExpenseDonutChart(
    rows: List<CategorySummary>,
    totalCents: Long
) {
    LedgerCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            val palette = donutPalette()
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
                        rows.forEachIndexed { index, row ->
                            val sweep = row.amountCents.toFloat() / totalCents.toFloat() * 360f
                            drawArc(
                                color = palette[index % palette.size],
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
                Text("分类占比", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (rows.isEmpty()) {
                    Text(
                        "本期暂无支出",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    rows.take(4).forEachIndexed { index, row ->
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
                                    color = palette[index % palette.size]
                                ) {}
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    row.categoryName ?: "未分类",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                "${categoryPercent(row.amountCents, totalCents)}%",
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

@Composable
private fun CategorySummaryRow(
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
        Modifier.clickable(onClick = onClick)
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
            Text(name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
private fun MonthlyTrendBarChart(
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
private fun TrendLegend(
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
private fun SectionHeader(
    title: String,
    action: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(action, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun InfoCard(text: String) {
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

private fun accountIcon(account: AccountEntity): ImageVector = when (account.iconName) {
    "payments" -> Icons.Default.Payments
    "credit_card" -> Icons.Default.CreditCard
    "chat" -> Icons.AutoMirrored.Filled.Chat
    "account_balance_wallet" -> Icons.Default.AccountBalanceWallet
    "phone_iphone" -> Icons.Default.PhoneIphone
    "directions_bus" -> Icons.Default.DirectionsBus
    "storefront" -> Icons.Default.Storefront
    else -> accountIcon(account.type)
}

private fun accountIcon(type: AccountType): ImageVector = when (type) {
    AccountType.CASH -> Icons.Default.Wallet
    AccountType.BANK_CARD, AccountType.CREDIT -> Icons.Default.CreditCard
    AccountType.THIRD_PARTY_PAYMENT -> Icons.Default.AccountBalanceWallet
    AccountType.STORED_VALUE_CARD -> Icons.Default.Wallet
    AccountType.TRANSIT_CARD -> Icons.Default.DirectionsBus
    AccountType.DIGITAL_BALANCE -> Icons.Default.AccountBalanceWallet
    AccountType.CUSTOM -> Icons.Default.Wallet
}

private fun categoryIcon(iconName: String): ImageVector = when (iconName) {
    "restaurant" -> Icons.Default.Restaurant
    "commute" -> Icons.Default.Commute
    "shopping_bag" -> Icons.Default.ShoppingBag
    "devices" -> Icons.Default.Devices
    "receipt_long" -> Icons.AutoMirrored.Filled.ReceiptLong
    "work" -> Icons.Default.Work
    "redeem" -> Icons.Default.Redeem
    "add_card" -> Icons.Default.AddCard
    else -> Icons.Default.Category
}

private fun accountTypeLabel(type: AccountType): String = when (type) {
    AccountType.CASH -> "现金"
    AccountType.BANK_CARD -> "银行卡"
    AccountType.THIRD_PARTY_PAYMENT -> "第三方支付"
    AccountType.STORED_VALUE_CARD -> "储值卡"
    AccountType.TRANSIT_CARD -> "公交卡"
    AccountType.DIGITAL_BALANCE -> "数字余额"
    AccountType.CREDIT -> "信用账户"
    AccountType.CUSTOM -> "自定义"
}

private fun categoryKindLabel(kind: CategoryKind): String = when (kind) {
    CategoryKind.EXPENSE -> "支出分类"
    CategoryKind.INCOME -> "收入分类"
}

private fun categoryIconOptions(kind: CategoryKind): List<String> = when (kind) {
    CategoryKind.EXPENSE -> listOf("restaurant", "commute", "shopping_bag", "devices", "receipt_long", "category")
    CategoryKind.INCOME -> listOf("work", "redeem", "add_card", "category")
}

private fun categoryIconLabel(iconName: String): String = when (iconName) {
    "restaurant" -> "餐饮"
    "commute" -> "交通"
    "shopping_bag" -> "购物"
    "devices" -> "数码"
    "receipt_long" -> "账单"
    "work" -> "工资"
    "redeem" -> "优惠"
    "add_card" -> "入账"
    else -> "通用"
}

private fun categoryColorOptions(): List<Long> = listOf(
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

private fun transactionLabel(type: TransactionType): String = when (type) {
    TransactionType.EXPENSE -> "支出"
    TransactionType.INCOME -> "收入"
    TransactionType.TRANSFER -> "转账"
    TransactionType.BALANCE_ADJUSTMENT -> "校正"
}

private fun transactionIcon(type: TransactionType): ImageVector = when (type) {
    TransactionType.EXPENSE -> Icons.Default.ArrowUpward
    TransactionType.INCOME -> Icons.Default.ArrowDownward
    TransactionType.TRANSFER -> Icons.Default.SwapHoriz
    TransactionType.BALANCE_ADJUSTMENT -> Icons.Default.AccountBalanceWallet
}

@Composable
private fun transactionColor(type: TransactionType): Color = when (type) {
    TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface
    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
    TransactionType.TRANSFER -> MaterialTheme.colorScheme.primary
    TransactionType.BALANCE_ADJUSTMENT -> MaterialTheme.colorScheme.tertiary
}

private fun transactionTitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    return when (transaction.type) {
        TransactionType.TRANSFER -> "${item.fromAccount?.name ?: "未知账户"} -> ${item.toAccount?.name ?: "未知账户"}"
        TransactionType.BALANCE_ADJUSTMENT -> "${item.account?.name ?: "未知账户"} 余额校正"
        else -> transaction.merchant.ifBlank { item.category?.name ?: transactionLabel(transaction.type) }
    }
}

private fun transactionSubtitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    val account = when (transaction.type) {
        TransactionType.TRANSFER -> "转账"
        else -> item.account?.name ?: "未选账户"
    }
    val category = item.category?.name.orEmpty()
    val note = transaction.note
    return listOf(account, category, note)
        .filter { it.isNotBlank() }
        .joinToString(" · ")
}

private fun amountLabel(type: TransactionType, cents: Long): String {
    val prefix = when (type) {
        TransactionType.EXPENSE -> "-"
        TransactionType.INCOME -> "+"
        TransactionType.TRANSFER -> ""
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+" else ""
    }
    return prefix + Money(cents).format()
}

private fun plainAmountLabel(type: TransactionType, cents: Long): String {
    val plain = Money(cents).formatPlain()
    return when (type) {
        TransactionType.EXPENSE -> "-${plain.removePrefix("-")}"
        TransactionType.INCOME -> "+${plain.removePrefix("-")}"
        TransactionType.TRANSFER -> plain
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+$plain" else plain
    }
}

private fun dateLabel(millis: Long): String {
    return SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(millis))
}

private data class TransactionDayGroup(
    val date: LocalDate,
    val items: List<TransactionWithDetails>,
    val expenseCents: Long,
    val incomeCents: Long
)

private data class MonthlyTrend(
    val month: LocalDate,
    val expenseCents: Long,
    val incomeCents: Long
)

private data class CalendarDay(
    val date: LocalDate,
    val inMonth: Boolean
)

private fun buildMonthlyTrends(transactions: List<TransactionWithDetails>): List<MonthlyTrend> {
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

private fun List<TransactionWithDetails>.groupedByDay(): List<TransactionDayGroup> {
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

private fun weekdayLabel(date: LocalDate): String = when (date.dayOfWeek.value) {
    1 -> "星期一"
    2 -> "星期二"
    3 -> "星期三"
    4 -> "星期四"
    5 -> "星期五"
    6 -> "星期六"
    else -> "星期日"
}

private fun calendarMonthCells(monthStart: LocalDate): List<CalendarDay> {
    val firstDay = monthStart.withDayOfMonth(1)
    val startOffset = firstDay.dayOfWeek.value - 1
    val gridStart = firstDay.minusDays(startOffset.toLong())
    return (0 until 42).map { offset ->
        val date = gridStart.plusDays(offset.toLong())
        CalendarDay(date = date, inMonth = date.monthValue == monthStart.monthValue)
    }
}

private fun localDateFromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

private fun localDateStartMillis(date: LocalDate): Long {
    return date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

private fun isInCurrentMonth(millis: Long): Boolean {
    val date = localDateFromMillis(millis)
    val current = LocalDate.now(ZoneId.systemDefault())
    return date.year == current.year && date.monthValue == current.monthValue
}

private fun categoryPercent(cents: Long, totalCents: Long): Int {
    if (totalCents <= 0) return 0
    return ((cents.toDouble() / totalCents.toDouble()) * 100).toInt()
}

@Composable
private fun donutPalette(): List<Color> = listOf(
    MaterialTheme.colorScheme.primary,
    MaterialTheme.colorScheme.secondary,
    MaterialTheme.colorScheme.tertiary,
    MaterialTheme.colorScheme.onSurfaceVariant,
    MaterialTheme.colorScheme.primary.copy(alpha = 0.68f),
    MaterialTheme.colorScheme.secondary.copy(alpha = 0.68f)
)

private fun handleAmountKey(current: String, key: String): String {
    return when (key) {
        "⌫" -> current.dropLast(1)
        "+", "-", "×", "÷" -> appendOperator(current, key)
        "." -> appendDecimalPoint(current)
        else -> appendDigit(current, key)
    }
}

private fun normalizedAmountInput(input: String): String {
    val normalized = input
        .replace('×', '*')
        .replace('÷', '/')
        .trim()

    return evaluateAmountExpression(normalized)
        ?.stripTrailingZeros()
        ?.toPlainString()
        ?: input
}

private fun hasUnresolvedAmountExpression(input: String): Boolean {
    val trimmed = input.trim()
    if (trimmed.isBlank()) return false
    return trimmed.any { it in setOf('+', '*', '/', '×', '÷') } || trimmed.drop(1).contains("-")
}

private fun appendOperator(current: String, operator: String): String {
    if (current.isBlank()) {
        return if (operator == "-") "-" else current
    }

    val trimmed = current.trimEnd()
    val last = trimmed.lastOrNull()
    val operatorChars = setOf('+', '-', '×', '÷', '*', '/')
    return if (last in operatorChars) {
        trimmed.dropLast(1) + operator
    } else {
        trimmed + operator
    }
}

private fun appendDecimalPoint(current: String): String {
    val lastNumber = current.split("+", "-", "×", "÷", "*", "/").lastOrNull().orEmpty()
    return if (lastNumber.contains(".")) current else current + "."
}

private fun appendDigit(current: String, digit: String): String {
    val lastNumber = current.split("+", "-", "×", "÷", "*", "/").lastOrNull().orEmpty()
    return if (lastNumber == "0") {
        current.dropLast(1) + digit
    } else {
        current + digit
    }
}

private fun evaluateAmountExpression(expression: String): BigDecimal? {
    val tokens = tokenizeExpression(expression) ?: return null
    if (tokens.isEmpty()) return null

    val values = ArrayDeque<BigDecimal>()
    val operators = ArrayDeque<Char>()

    fun applyOperator(): Boolean {
        if (values.size < 2 || operators.isEmpty()) return false
        val right = values.removeLast()
        val left = values.removeLast()
        val result = when (operators.removeLast()) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> if (right.compareTo(BigDecimal.ZERO) == 0) {
                return false
            } else {
                left.divide(right, 8, RoundingMode.HALF_UP)
            }
            else -> return false
        }
        values.addLast(result)
        return true
    }

    tokens.forEach { token ->
        when (token) {
            "+", "-", "*", "/" -> {
                val operator = token.single()
                while (operators.isNotEmpty() && precedence(operators.last()) >= precedence(operator)) {
                    if (!applyOperator()) return null
                }
                operators.addLast(operator)
            }
            else -> values.addLast(token.toBigDecimalOrNull() ?: return null)
        }
    }

    while (operators.isNotEmpty()) {
        if (!applyOperator()) return null
    }

    return values.singleOrNull()?.setScale(2, RoundingMode.HALF_UP)
}

private fun tokenizeExpression(expression: String): List<String>? {
    val tokens = mutableListOf<String>()
    var index = 0

    while (index < expression.length) {
        val char = expression[index]
        when {
            char.isWhitespace() -> index += 1
            char in setOf('+', '*', '/') -> {
                tokens += char.toString()
                index += 1
            }
            char == '-' -> {
                val isUnary = tokens.isEmpty() || tokens.last() in setOf("+", "-", "*", "/")
                if (isUnary) {
                    val start = index
                    index += 1
                    while (index < expression.length && (expression[index].isDigit() || expression[index] == '.')) {
                        index += 1
                    }
                    tokens += expression.substring(start, index)
                } else {
                    tokens += "-"
                    index += 1
                }
            }
            char.isDigit() || char == '.' -> {
                val start = index
                index += 1
                while (index < expression.length && (expression[index].isDigit() || expression[index] == '.')) {
                    index += 1
                }
                tokens += expression.substring(start, index)
            }
            else -> return null
        }
    }

    return tokens
}

private fun precedence(operator: Char): Int = when (operator) {
    '*', '/' -> 2
    '+', '-' -> 1
    else -> 0
}
