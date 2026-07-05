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
import androidx.compose.material3.AlertDialog
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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


internal object MineRoute {
    const val Menu = "mine_menu"
    const val Accounts = "mine_accounts"
    const val Categories = "mine_categories"
    const val Budget = "mine_budget"
    const val Data = "mine_data"
    const val EntryPreferences = "mine_entry_preferences"
    const val Appearance = "mine_appearance"
    const val Trash = "mine_trash"
    const val Danger = "mine_danger"
}

@Composable
internal fun MineScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    initialRoute: String? = null,
    onInitialRouteConsumed: () -> Unit = {},
    amountsHidden: Boolean = false,
    onToggleAmountsHidden: () -> Unit = {},
    entryPreferences: EntryPreferences = EntryPreferences(),
    onEntryPreferencesChange: (EntryPreferences) -> Unit = {}
) {
    val navController = rememberNavController()

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    LaunchedEffect(initialRoute) {
        val route = initialRoute ?: return@LaunchedEffect
        navController.navigate(route) {
            launchSingleTop = true
        }
        onInitialRouteConsumed()
    }

    NavHost(
        navController = navController,
        startDestination = MineRoute.Menu,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(MineRoute.Menu) {
            MineMenu(
                uiState = uiState,
                amountsHidden = amountsHidden,
                onToggleAmountsHidden = onToggleAmountsHidden,
                entryPreferences = entryPreferences,
                onOpen = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(MineRoute.Accounts) {
            AccountManagementPage(
                uiState = uiState,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                amountsHidden = amountsHidden
            )
        }
        composable(MineRoute.Categories) {
            CategoryManagementPage(uiState, viewModel, onBack = { navController.popBackStack() })
        }
        composable(MineRoute.Budget) {
            BudgetSettingsPage(uiState, viewModel, onBack = { navController.popBackStack() })
        }
        composable(MineRoute.Data) {
            DataManagementPage(uiState, viewModel, onBack = { navController.popBackStack() })
        }
        composable(MineRoute.EntryPreferences) {
            EntryPreferencesPage(
                uiState = uiState,
                entryPreferences = entryPreferences,
                onEntryPreferencesChange = onEntryPreferencesChange,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MineRoute.Appearance) {
            AppearancePage(
                amountsHidden = amountsHidden,
                onToggleAmountsHidden = onToggleAmountsHidden,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MineRoute.Trash) {
            TrashPage(uiState, viewModel, onBack = { navController.popBackStack() }, amountsHidden = amountsHidden)
        }
        composable(MineRoute.Danger) {
            DangerOperationsPage(uiState, viewModel, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
internal fun MineMenu(
    uiState: AccountingUiState,
    amountsHidden: Boolean = false,
    onToggleAmountsHidden: () -> Unit = {},
    entryPreferences: EntryPreferences = EntryPreferences(),
    onOpen: (String) -> Unit
) {
    val defaultAccountName = uiState.activeAccounts
        .firstOrNull { it.id == entryPreferences.defaultAccountId }
        ?.name
        ?: "首个账户"
    val groups = listOf(
        SettingsMenuGroup(
            title = "基础数据",
            entries = listOf(
                SettingsMenuEntry("账户管理", "资产账户、信用卡和初始余额", Icons.Default.AccountBalanceWallet, MineRoute.Accounts),
                SettingsMenuEntry("分类与标签", "分类图标、颜色和固定标签库", Icons.Default.Category, MineRoute.Categories)
            )
        ),
        SettingsMenuGroup(
            title = "备份恢复",
            entries = listOf(
                SettingsMenuEntry("导入导出", "JSON 完整备份，CSV 流水导出", Icons.Default.Download, MineRoute.Data),
                SettingsMenuEntry("WebDAV 备份", "后续支持，不使用云账号", Icons.Default.Devices, null, "后续")
            )
        ),
        SettingsMenuGroup(
            title = "记账偏好",
            entries = listOf(
                SettingsMenuEntry(
                    "快速记账默认项",
                    "${transactionLabel(entryPreferences.defaultType)} · $defaultAccountName",
                    Icons.Default.Edit,
                    MineRoute.EntryPreferences
                ),
                SettingsMenuEntry(
                    "智能推荐",
                    "常用分类置顶与标签推荐",
                    Icons.Default.History,
                    MineRoute.EntryPreferences,
                    if (entryPreferences.commonCategoryFirst || entryPreferences.tagSuggestionsEnabled) "已开启" else "已关闭"
                )
            )
        ),
        SettingsMenuGroup(
            title = "外观",
            entries = listOf(
                SettingsMenuEntry(
                    title = "金额隐私",
                    subtitle = "隐藏首页、统计、资产和回收站金额",
                    icon = Icons.Default.PhoneIphone,
                    route = MineRoute.Appearance,
                    badge = if (amountsHidden) "已隐藏" else "浅色"
                )
            )
        ),
        SettingsMenuGroup(
            title = "数据安全",
            entries = listOf(
                SettingsMenuEntry("回收站", "恢复或彻底删除流水", Icons.Default.Restore, MineRoute.Trash, "${uiState.trash.size} 条"),
                SettingsMenuEntry("危险操作", "清空流水等操作需要强确认", Icons.Default.Delete, MineRoute.Danger)
            )
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SettingsHeaderCard(uiState)
        }
        item {
            LocalDataOverviewCard(
                uiState = uiState,
                amountsHidden = amountsHidden,
                onOpenData = { onOpen(MineRoute.Data) }
            )
        }
        groups.forEach { group ->
            item { SettingsSectionTitle(group.title) }
            items(group.entries, key = { "${group.title}-${it.title}" }) { entry ->
                MineMenuRow(
                    title = entry.title,
                    subtitle = entry.subtitle,
                    icon = entry.icon,
                    badge = entry.badge,
                    checked = entry.checked,
                    enabled = entry.route != null || entry.onClick != null,
                    onClick = {
                        entry.onClick?.invoke() ?: entry.route?.let(onOpen)
                    }
                )
            }
        }
        item {
            SettingsTrustNote()
        }
    }
}

private data class SettingsMenuGroup(
    val title: String,
    val entries: List<SettingsMenuEntry>
)

private data class SettingsMenuEntry(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String?,
    val badge: String? = null,
    val checked: Boolean? = null,
    val onClick: (() -> Unit)? = null
)

@Composable
private fun SettingsHeaderCard(uiState: AccountingUiState) {
    LedgerCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("设置", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "本地优先，不使用云账号",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SettingsStatusPill("本机数据")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsDataMetric(
                label = "账户",
                value = uiState.accounts.size.toString(),
                modifier = Modifier.weight(1f)
            )
            SettingsDataMetric(
                label = "分类",
                value = uiState.categories.size.toString(),
                modifier = Modifier.weight(1f)
            )
            SettingsDataMetric(
                label = "标签",
                value = uiState.tags.size.toString(),
                modifier = Modifier.weight(1f)
            )
            SettingsDataMetric(
                label = "回收站",
                value = uiState.trash.size.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LocalDataOverviewCard(
    uiState: AccountingUiState,
    amountsHidden: Boolean = false,
    onOpenData: () -> Unit
) {
    val monthExpense = uiState.summary?.totals?.expenseCents ?: 0L
    val monthIncome = uiState.summary?.totals?.incomeCents ?: 0L
    LedgerPanelSurface(onClick = onOpenData) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("本地数据", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        "流水和基础数据保存在本机，导入导出从这里处理",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsDataMetric(
                    label = "近期流水",
                    value = uiState.recentTransactions.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                SettingsDataMetric(
                    label = "本期支出",
                    value = privacyAmountLabel(monthExpense, amountsHidden),
                    modifier = Modifier.weight(1f)
                )
                SettingsDataMetric(
                    label = "本期收入",
                    value = privacyAmountLabel(monthIncome, amountsHidden),
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                if (uiState.exportPreview.isBlank()) "最近备份：尚未在本机生成备份文件" else "最近备份：已生成 ${uiState.exportFormat.name} 预览，记得保存到文件",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsDataMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.heightIn(min = 54.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleSmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, start = 2.dp)
    )
}

@Composable
private fun SettingsStatusPill(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = LedgerMint,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SettingsTrustNote() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = LedgerCardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Text(
            "数据导入、覆盖恢复和危险操作会保留明确确认。WebDAV 作为后续备份能力，不引入账号系统。",
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
internal fun MineMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String? = null,
    checked: Boolean? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    LedgerPanelSurface(onClick = if (enabled) onClick else null) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (enabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (checked != null) {
                Switch(
                    checked = checked,
                    onCheckedChange = { onClick() }
                )
            } else if (badge != null) {
                SettingsStatusPill(badge)
            } else {
                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
internal fun MineBackHeader(
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
        Text(title, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
internal fun EntryPreferencesPage(
    uiState: AccountingUiState,
    entryPreferences: EntryPreferences,
    onEntryPreferencesChange: (EntryPreferences) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("记账偏好", onBack) }
        item {
            LedgerPanelSurface {
                Text(
                    "这些偏好只影响新建流水的默认值和推荐顺序，不会修改历史流水。后续接入备份配置时再做持久化。",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("快速记账默认项", "打开记账 Sheet 时生效")
                EntryPreferenceTypeSelector(
                    selectedType = entryPreferences.defaultType,
                    onSelected = { type ->
                        onEntryPreferencesChange(entryPreferences.copy(defaultType = type))
                    }
                )
                EntryPreferenceAccountSelector(
                    accounts = uiState.activeAccounts,
                    selectedAccountId = entryPreferences.defaultAccountId,
                    onSelected = { accountId ->
                        onEntryPreferencesChange(entryPreferences.copy(defaultAccountId = accountId))
                    }
                )
                EntryPreferenceSwitchRow(
                    title = "默认时间为当前时间",
                    subtitle = "开启后每次新建都会使用当前时间",
                    checked = entryPreferences.useCurrentTime,
                    onCheckedChange = { checked ->
                        onEntryPreferencesChange(entryPreferences.copy(useCurrentTime = checked))
                    }
                )
                EntryPreferenceSwitchRow(
                    title = "保存后继续记一笔",
                    subtitle = "适合连续补录，保存成功后 Sheet 不关闭",
                    checked = entryPreferences.continueAfterSave,
                    onCheckedChange = { checked ->
                        onEntryPreferencesChange(entryPreferences.copy(continueAfterSave = checked))
                    }
                )
                EntryPreferenceSwitchRow(
                    title = "启用语音记账",
                    subtitle = "关闭后首页语音入口会保留但不打开识别",
                    checked = entryPreferences.voiceEnabled,
                    onCheckedChange = { checked ->
                        onEntryPreferencesChange(entryPreferences.copy(voiceEnabled = checked))
                    }
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("智能推荐", "轻量、可解释")
                EntryPreferenceSwitchRow(
                    title = "常用分类优先",
                    subtitle = "按近期高频使用把分类放到前面",
                    checked = entryPreferences.commonCategoryFirst,
                    onCheckedChange = { checked ->
                        onEntryPreferencesChange(entryPreferences.copy(commonCategoryFirst = checked))
                    }
                )
                EntryPreferenceSwitchRow(
                    title = "标签推荐",
                    subtitle = "按历史常用和最近使用调整标签顺序",
                    checked = entryPreferences.tagSuggestionsEnabled,
                    onCheckedChange = { checked ->
                        onEntryPreferencesChange(entryPreferences.copy(tagSuggestionsEnabled = checked))
                    }
                )
            }
        }
        item {
            LedgerActionButton(
                label = "恢复默认偏好",
                icon = Icons.Default.Restore,
                onClick = { onEntryPreferencesChange(EntryPreferences()) },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                borderColor = LedgerDivider
            )
        }
    }
}

@Composable
private fun EntryPreferenceTypeSelector(
    selectedType: TransactionType,
    onSelected: (TransactionType) -> Unit
) {
    val options = listOf(
        TransactionType.EXPENSE,
        TransactionType.INCOME,
        TransactionType.TRANSFER
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
            options.forEach { type ->
                val selected = selectedType == type
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .ledgerPressClickable(onClick = { onSelected(type) }),
                    shape = RoundedCornerShape(7.dp),
                    color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            transactionLabel(type),
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall,
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
private fun EntryPreferenceAccountSelector(
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onSelected: (Long?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "默认账户",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EntryPreferencePill(
                label = "首个账户",
                selected = selectedAccountId == null,
                onClick = { onSelected(null) }
            )
            accounts.forEach { account ->
                EntryPreferencePill(
                    label = account.name,
                    selected = selectedAccountId == account.id,
                    onClick = { onSelected(account.id) }
                )
            }
        }
        if (accounts.isEmpty()) {
            Text(
                "当前还没有可用账户，记账时会提示先选择或创建账户。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EntryPreferencePill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .ledgerPressClickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) LedgerMint else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.38f) else LedgerDivider
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
private fun EntryPreferenceSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    LedgerPanelSurface(onClick = { onCheckedChange(!checked) }) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
internal fun AppearancePage(
    amountsHidden: Boolean,
    onToggleAmountsHidden: () -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("外观", onBack) }
        item {
            LedgerPanelSurface {
                Text(
                    "第一版保持浅色模式和低饱和视觉风格；深色模式后续接入主题状态后再开放。",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("主题", "当前：浅色")
                AppearanceStatusRow(
                    title = "浅色模式",
                    subtitle = "使用灰白背景、白色内容面和低饱和强调色",
                    selected = true
                )
                AppearanceStatusRow(
                    title = "深色模式",
                    subtitle = "后续支持，不在当前版本展示假开关",
                    selected = false
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("金额隐私", if (amountsHidden) "已隐藏" else "正常显示")
                EntryPreferenceSwitchRow(
                    title = "隐藏主金额",
                    subtitle = "首页、统计、资产和回收站中的主要金额会显示为 •••",
                    checked = amountsHidden,
                    onCheckedChange = { onToggleAmountsHidden() }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsDataMetric(
                        label = "显示效果",
                        value = privacyAmountLabel(3268050, amountsHidden),
                        modifier = Modifier.weight(1f)
                    )
                    SettingsDataMetric(
                        label = "影响范围",
                        value = "4 页",
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    "金额隐私只影响界面显示，不修改本地流水、统计结果或导出内容。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            LedgerActionButton(
                label = "恢复默认外观",
                icon = Icons.Default.Restore,
                onClick = {
                    if (amountsHidden) {
                        onToggleAmountsHidden()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                borderColor = LedgerDivider,
                enabled = amountsHidden
            )
        }
    }
}

@Composable
private fun AppearanceStatusRow(
    title: String,
    subtitle: String,
    selected: Boolean
) {
    LedgerPanelSurface {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (selected) LedgerMint else MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.28f) else LedgerDivider)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.PhoneIphone,
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SettingsStatusPill(if (selected) "已启用" else "后续")
        }
    }
}

@Composable
internal fun AccountManagementPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit,
    amountsHidden: Boolean = false
) {
    val activeAccountRows = uiState.accounts.filterNot { it.account.isArchived }
    val archivedAccountRows = uiState.accounts.filter { it.account.isArchived }
    var accountToEdit by remember { mutableStateOf<AccountBalanceRow?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("账户管理", onBack) }
        item { AddAccountCard(viewModel) }
        item { SectionHeader("资产账户", "${activeAccountRows.size} 个") }
        items(activeAccountRows, key = { it.account.id }) { row ->
            AccountRow(
                row = row,
                onClick = { accountToEdit = row },
                onDelete = { viewModel.deleteAccount(row.account.id) },
                amountsHidden = amountsHidden,
                deleteContentDescription = "归档或移除账户",
                confirmDeleteContentDescription = "确认归档或移除账户"
            )
        }
        if (archivedAccountRows.isNotEmpty()) {
            item { SectionHeader("已归档账户", "${archivedAccountRows.size} 个") }
            items(archivedAccountRows, key = { it.account.id }) { row ->
                AccountRow(
                    row = row,
                    onClick = { accountToEdit = row },
                    onRestore = { viewModel.restoreAccount(row.account.id) },
                    onDelete = { viewModel.deleteAccount(row.account.id) },
                    amountsHidden = amountsHidden,
                    deleteContentDescription = "移除归档账户",
                    confirmDeleteContentDescription = "确认移除归档账户"
                )
            }
        }
    }

    accountToEdit?.let { row ->
        AccountEditSheet(
            row = row,
            amountsHidden = amountsHidden,
            onDismiss = { accountToEdit = null },
            onSave = { name, type, initialBalance, iconName, colorArgb ->
                viewModel.updateAccount(
                    id = row.account.id,
                    name = name,
                    type = type,
                    initialBalance = initialBalance,
                    iconName = iconName,
                    colorArgb = colorArgb
                )
                accountToEdit = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountEditSheet(
    row: AccountBalanceRow,
    amountsHidden: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, AccountType, String, String, Long) -> Unit
) {
    var name by remember(row.account.id) { mutableStateOf(row.account.name) }
    var type by remember(row.account.id) { mutableStateOf(row.account.type) }
    var initialBalance by remember(row.account.id) {
        mutableStateOf(Money(row.account.initialBalanceCents).formatPlain())
    }
    var iconName by remember(row.account.id) { mutableStateOf(row.account.iconName) }
    var colorArgb by remember(row.account.id) { mutableStateOf(row.account.colorArgb) }
    var iconQuery by remember(row.account.id) { mutableStateOf("") }
    val canSave = name.isNotBlank() && Money.parseMajorStrict(initialBalance) != null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding()
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
                Text("编辑账户", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = { onSave(name.trim(), type, initialBalance, iconName, colorArgb) },
                    enabled = canSave
                ) {
                    Text("保存", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
                border = BorderStroke(1.dp, LedgerDivider)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(colorArgb).copy(alpha = 0.14f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(accountIcon(iconName, type), contentDescription = null, tint = Color(colorArgb))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            name.ifBlank { "账户名称" },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            "${accountTypeLabel(type)} · ${accountIconLabel(iconName)} · ${privacyAmountLabel(row.balanceCents, amountsHidden)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "账户名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text("账户类型", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountType.entries.forEach { accountType ->
                    LedgerChoiceChip(
                        selected = type == accountType,
                        label = accountTypeLabel(accountType),
                        onClick = { type = accountType }
                    )
                }
            }
            MinimalInputLine(
                value = initialBalance,
                onValueChange = { initialBalance = it },
                placeholder = "初始余额",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text(
                "调整初始余额会同步影响该账户当前余额。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AccountIconPicker(
                selectedIconName = iconName,
                query = iconQuery,
                tint = Color(colorArgb),
                onQueryChange = { iconQuery = it },
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
private fun AccountIconPicker(
    selectedIconName: String,
    query: String,
    tint: Color,
    onQueryChange: (String) -> Unit,
    onSelected: (String) -> Unit
) {
    val options = remember(query) { accountIconOptions(query) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("图标", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MinimalInputLine(
            value = query,
            onValueChange = onQueryChange,
            placeholder = "搜索图标：钱包、银行卡、交通卡...",
            modifier = Modifier.fillMaxWidth()
        )
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
        if (options.isEmpty()) {
            Text(
                "没有匹配的图标",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun CategoryManagementPage(
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
internal fun BudgetSettingsPage(
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
internal fun DataManagementPage(
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
    val importPreview = uiState.pendingImportPreview

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
                        LedgerActionButton(
                            label = "生成 JSON",
                            icon = Icons.Default.Download,
                            onClick = { viewModel.export(ExportFormat.JSON) },
                            modifier = Modifier.weight(1f)
                        )
                        LedgerActionButton(
                            label = "生成 CSV",
                            icon = Icons.Default.Download,
                            onClick = { viewModel.export(ExportFormat.CSV) },
                            modifier = Modifier.weight(1f),
                            containerColor = LedgerMint,
                            contentColor = MaterialTheme.colorScheme.primary,
                            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                        )
                    }
                    LedgerActionButton(
                        label = "导入 JSON 备份",
                        icon = Icons.Default.Upload,
                        onClick = { jsonImportLauncher.launch(arrayOf("application/json", "text/*")) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = LedgerMint,
                        contentColor = MaterialTheme.colorScheme.primary,
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                    )
                    if (uiState.exportPreview.isNotBlank()) {
                        LedgerActionButton(
                            label = "保存到文件",
                            icon = Icons.Default.Download,
                            onClick = {
                                when (uiState.exportFormat) {
                                    ExportFormat.JSON -> jsonSaveLauncher.launch("accounting-backup.json")
                                    ExportFormat.CSV -> csvSaveLauncher.launch("accounting-transactions.csv")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
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

    if (importPreview != null) {
        val exportedAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            .format(Date(importPreview.exportedAt))
        AlertDialog(
            onDismissRequest = { viewModel.cancelImportJson() },
            title = {
                Text("确认导入 JSON 备份")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("导出时间：$exportedAt")
                    Text("备份版本：${importPreview.schemaVersion}")
                    Text("账户：${importPreview.accountCount} 个")
                    Text("分类：${importPreview.categoryCount} 个")
                    Text("标签：${importPreview.tagCount} 个")
                    Text("流水：${importPreview.transactionCount} 条")
                    Text("预算：${importPreview.budgetCount} 条")
                    Text("周期规则：${importPreview.recurringRuleCount} 条")
                    Text(
                        "导入会替换当前全部本地数据，无法自动撤销。",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmImportJson() }) {
                    Text("确认导入", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelImportJson() }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
internal fun DangerOperationsPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    var clearTransactionsRequested by remember { mutableStateOf(false) }
    var confirmText by remember { mutableStateOf("") }
    val canConfirmClear = confirmText.trim() == "清空流水"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("危险操作", onBack) }
        item {
            LedgerPanelSurface {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "这些操作会明显改变本地数据，请先确认已经导出备份。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "第一版只提供可恢复的清空流水：所有未删除流水会移入回收站，不会立即永久删除。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item {
            LedgerCard {
                SectionHeader("清空所有流水", "移入回收站")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsDataMetric(
                        label = "近期流水",
                        value = uiState.recentTransactions.size.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    SettingsDataMetric(
                        label = "回收站",
                        value = uiState.trash.size.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    "执行后首页、统计和资产余额会排除这些流水；可在回收站逐条恢复或彻底删除。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LedgerActionButton(
                    label = "清空所有流水",
                    icon = Icons.Default.Delete,
                    onClick = { clearTransactionsRequested = true },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.10f),
                    contentColor = MaterialTheme.colorScheme.error,
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.24f),
                    enabled = uiState.recentTransactions.isNotEmpty()
                )
            }
        }
    }

    if (clearTransactionsRequested) {
        AlertDialog(
            onDismissRequest = {
                clearTransactionsRequested = false
                confirmText = ""
            },
            title = { Text("清空所有流水？") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("所有未删除流水会移入回收站，并从首页、统计和资产余额中移除。")
                    Text(
                        "如需继续，请输入：清空流水",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    MinimalInputLine(
                        value = confirmText,
                        onValueChange = { confirmText = it },
                        placeholder = "输入确认文字",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = canConfirmClear,
                    onClick = {
                        viewModel.moveAllTransactionsToTrash()
                        clearTransactionsRequested = false
                        confirmText = ""
                    }
                ) {
                    Text("移入回收站", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        clearTransactionsRequested = false
                        confirmText = ""
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
internal fun TrashPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit,
    amountsHidden: Boolean = false
) {
    var permanentDeleteTarget by remember { mutableStateOf<TransactionWithDetails?>(null) }
    var clearTrashRequested by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("回收站", onBack) }
        item {
            LedgerPanelSurface {
                Text(
                    "删除的流水不参与首页、统计和资产余额。恢复后会回到原来的时间、分类、账户和标签。",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item { SectionHeader("已删除流水", "${uiState.trash.size} 条") }
        if (uiState.trash.isNotEmpty()) {
            item {
                LedgerActionButton(
                    label = "清空回收站",
                    icon = Icons.Default.Delete,
                    onClick = { clearTrashRequested = true },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.10f),
                    contentColor = MaterialTheme.colorScheme.error,
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.24f)
                )
            }
        }
        items(uiState.trash, key = { it.transaction.id }) { transaction ->
            LedgerCard {
                TransactionRow(
                    item = transaction,
                    amountsHidden = amountsHidden,
                    trailing = {
                        Row {
                            IconButton(onClick = { viewModel.restoreTransaction(transaction.transaction.id) }) {
                                Icon(Icons.Default.Restore, contentDescription = "恢复")
                            }
                            IconButton(onClick = { permanentDeleteTarget = transaction }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "彻底删除",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.64f),
                    border = BorderStroke(1.dp, LedgerDivider)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            "删除时间：${settingsDateTimeLabel(transaction.transaction.deletedAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "原流水时间：${settingsDateTimeLabel(transaction.transaction.occurredAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        item {
            if (uiState.trash.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Restore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.54f),
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            "回收站是空的",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

    permanentDeleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { permanentDeleteTarget = null },
            title = { Text("彻底删除这条流水？") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("彻底删除后无法从回收站恢复。")
                    Text(
                        "原流水时间：${settingsDateTimeLabel(target.transaction.occurredAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "金额：${privacyAmountLabel(target.transaction.amountCents, amountsHidden)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.permanentlyDeleteTransaction(target.transaction.id)
                        permanentDeleteTarget = null
                    }
                ) {
                    Text("彻底删除", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { permanentDeleteTarget = null }) {
                    Text("取消")
                }
            }
        )
    }
    if (clearTrashRequested) {
        AlertDialog(
            onDismissRequest = { clearTrashRequested = false },
            title = { Text("清空回收站？") },
            text = {
                Text("将彻底删除回收站中的 ${uiState.trash.size} 条流水，删除后无法恢复。")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearTrash()
                        clearTrashRequested = false
                    }
                ) {
                    Text("清空", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { clearTrashRequested = false }) {
                    Text("取消")
                }
            }
        )
    }
}

private fun settingsDateTimeLabel(millis: Long?): String {
    return if (millis == null) {
        "--"
    } else {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(Date(millis))
    }
}
