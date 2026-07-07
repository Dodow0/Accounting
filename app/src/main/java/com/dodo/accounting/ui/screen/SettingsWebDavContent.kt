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


@Composable
internal fun WebDavBackupPage(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    config: WebDavConfig,
    onConfigChange: (WebDavConfig) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var url by remember(config.url) { mutableStateOf(config.url) }
    var username by remember(config.username) { mutableStateOf(config.username) }
    var password by remember(config.password) { mutableStateOf(config.password) }
    var statusText by remember { mutableStateOf<String?>(null) }
    var isWorking by remember { mutableStateOf(false) }
    val draftConfig = WebDavConfig(url = url, username = username, password = password)

    fun saveConfig(): WebDavConfig {
        val nextConfig = draftConfig.copy(url = draftConfig.url.trim(), username = draftConfig.username.trim())
        onConfigChange(nextConfig)
        statusText = "WebDAV 配置已保存"
        return nextConfig
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MineBackHeader("WebDAV 备份", onBack) }
        item {
            LedgerPanelSurface {
                Text(
                    "地址可以填写目录或完整 JSON 文件地址；填写目录时会使用 accounting-backup.json。上传前会生成完整 JSON 备份。",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("连接信息", if (config.isConfigured) "已配置" else "未配置")
                MinimalInputLine(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = "https://example.com/dav/accounting/",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                MinimalInputLine(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "用户名（可选）",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                MinimalInputLine(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "密码或应用专用密码（可选）",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                LedgerActionButton(
                    label = "保存配置",
                    icon = Icons.Default.Check,
                    onClick = { saveConfig() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = url.trim().isNotBlank()
                )
            }
        }
        item {
            LedgerCard {
                SectionHeader("远程备份", "JSON")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LedgerActionButton(
                        label = "上传",
                        icon = Icons.Default.Upload,
                        onClick = {
                            scope.launch {
                                isWorking = true
                                statusText = "正在上传到 WebDAV..."
                                val nextConfig = saveConfig()
                                runCatching {
                                    val content = viewModel.exportJsonContent().getOrThrow()
                                    WebDavBackupClient(nextConfig).uploadJson(content)
                                }
                                    .onSuccess { statusText = "WebDAV 备份已上传" }
                                    .onFailure { statusText = it.message ?: "WebDAV 上传失败" }
                                isWorking = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isWorking && url.trim().isNotBlank()
                    )
                    LedgerActionButton(
                        label = "下载",
                        icon = Icons.Default.Download,
                        onClick = {
                            scope.launch {
                                isWorking = true
                                statusText = "正在从 WebDAV 下载..."
                                val nextConfig = saveConfig()
                                runCatching {
                                    WebDavBackupClient(nextConfig).downloadJson()
                                }
                                    .onSuccess { content ->
                                        viewModel.importJson(content)
                                        statusText = "已读取 WebDAV 备份，请确认导入"
                                    }
                                    .onFailure { statusText = it.message ?: "WebDAV 下载失败" }
                                isWorking = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        containerColor = LedgerMint,
                        contentColor = MaterialTheme.colorScheme.primary,
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                        enabled = !isWorking && url.trim().isNotBlank()
                    )
                }
                statusText?.let { text ->
                    Text(
                        text,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (text.contains("失败")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    ImportPreviewDialog(uiState = uiState, viewModel = viewModel)
}
