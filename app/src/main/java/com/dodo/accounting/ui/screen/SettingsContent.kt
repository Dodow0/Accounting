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


private object MineRoute {
    const val Menu = "mine_menu"
    const val Accounts = "mine_accounts"
    const val Categories = "mine_categories"
    const val Budget = "mine_budget"
    const val Data = "mine_data"
    const val Trash = "mine_trash"
}

@Composable
internal fun MineScreen(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel
) {
    val navController = rememberNavController()

    if (uiState.isLoading) {
        FullScreenLoading()
        return
    }

    NavHost(
        navController = navController,
        startDestination = MineRoute.Menu,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(MineRoute.Menu) {
            MineMenu(
                onOpen = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(MineRoute.Accounts) {
            AccountManagementPage(uiState, viewModel, onBack = { navController.popBackStack() })
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
        composable(MineRoute.Trash) {
            TrashPage(uiState, viewModel, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
internal fun MineMenu(
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "设置",
                style = MaterialTheme.typography.titleLarge
            )
        }
        item { MineMenuRow("账户管理", "资产账户与初始余额", Icons.Default.AccountBalanceWallet) { onOpen(MineRoute.Accounts) } }
        item { MineMenuRow("分类管理", "分类、标签、排序和图标颜色", Icons.Default.Category) { onOpen(MineRoute.Categories) } }
        item { MineMenuRow("预算设置", "总预算、分类预算和周期账单", Icons.Default.Assessment) { onOpen(MineRoute.Budget) } }
        item { MineMenuRow("数据导出", "JSON / CSV 备份与恢复", Icons.Default.Download) { onOpen(MineRoute.Data) } }
        item { MineMenuRow("回收站", "恢复或彻底删除流水", Icons.Default.Restore) { onOpen(MineRoute.Trash) } }
    }
}

@Composable
internal fun MineMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    LedgerPanelSurface(onClick = onClick) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
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
internal fun AccountManagementPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    val activeAccountRows = uiState.accounts.filterNot { it.account.isArchived }
    val archivedAccountRows = uiState.accounts.filter { it.account.isArchived }

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
                onDelete = { viewModel.deleteAccount(row.account.id) },
                deleteContentDescription = "归档或移除账户",
                confirmDeleteContentDescription = "确认归档或移除账户"
            )
        }
        if (archivedAccountRows.isNotEmpty()) {
            item { SectionHeader("已归档账户", "${archivedAccountRows.size} 个") }
            items(archivedAccountRows, key = { it.account.id }) { row ->
                AccountRow(
                    row = row,
                    onRestore = { viewModel.restoreAccount(row.account.id) },
                    onDelete = { viewModel.deleteAccount(row.account.id) },
                    deleteContentDescription = "移除归档账户",
                    confirmDeleteContentDescription = "确认移除归档账户"
                )
            }
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
internal fun TrashPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("回收站", onBack) }
        item { SectionHeader("已删除流水", "${uiState.trash.size} 条") }
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
}
