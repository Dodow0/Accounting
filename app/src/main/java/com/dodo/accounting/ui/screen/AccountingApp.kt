package com.dodo.accounting.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow as CategorySummary
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.R
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
import java.util.Date

internal val LedgerMint: Color
    @Composable get() = MaterialTheme.colorScheme.primaryContainer

internal val LedgerPanel: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

internal val LedgerDivider: Color
    @Composable get() = MaterialTheme.colorScheme.outlineVariant

internal val LedgerExpensePink: Color
    @Composable get() = MaterialTheme.colorScheme.error

internal val LedgerCardShape = RoundedCornerShape(20.dp)

private const val VOICE_RECOGNITION_MIN_DURATION_MS = 800L
private const val VOICE_RECOGNITION_COMPLETE_SILENCE_MS = 1_200L
private const val VOICE_RECOGNITION_POSSIBLE_SILENCE_MS = 700L

@Composable
internal fun LedgerCard(
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
internal fun LedgerPanelSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickModifier = onClick?.let { Modifier.ledgerPressClickable(onClick = it) } ?: Modifier
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
    viewModel: AccountingViewModel = hiltViewModel(),
    themeMode: ThemeMode = ThemeMode.LIGHT,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val context = LocalContext.current
    val preferenceStore = remember(context) { UiPreferenceStore(context) }
    val storedPreferences = remember(preferenceStore) { preferenceStore.load() }
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

    var entrySheetOpen by remember { mutableStateOf(false) }
    var pendingSettingsRoute by remember { mutableStateOf<String?>(null) }
    var pendingEntryPrefillDraft by remember { mutableStateOf<EntryPrefillDraft?>(null) }
    var voiceEntryRequestSignal by remember { mutableIntStateOf(0) }
    var pendingStatsAccountFilterId by remember { mutableStateOf<Long?>(null) }
    var pendingStatsViewMode by remember { mutableStateOf<StatsInitialViewMode?>(null) }
    var entryDismissRequestSignal by remember { mutableIntStateOf(0) }
    var amountsHidden by remember { mutableStateOf(storedPreferences.amountsHidden) }
    var entryPreferences by remember { mutableStateOf(storedPreferences.entryPreferences) }
    var webDavConfig by remember { mutableStateOf(storedPreferences.webDavConfig) }
    val entrySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = AppTab.entries.firstOrNull { it.route == backStackEntry?.destination?.route } ?: AppTab.Home
    val systemDensity = LocalDensity.current
    val appDensity = remember(systemDensity.density, systemDensity.fontScale) {
        Density(
            density = systemDensity.density,
            fontScale = systemDensity.fontScale.coerceAtMost(1.15f)
        )
    }

    LaunchedEffect(selectedTab) {
        viewModel.setTrendDataEnabled(selectedTab == AppTab.Stats)
    }

    val openManualEntry: () -> Unit = {
        viewModel.cancelEditTransaction()
        pendingEntryPrefillDraft = null
        voiceEntryRequestSignal = 0
        entrySheetOpen = true
        scope.launch { entrySheetState.show() }
    }

    val openVoiceEntry: () -> Unit = {
        if (entryPreferences.voiceEnabled) {
            viewModel.cancelEditTransaction()
            pendingEntryPrefillDraft = null
            voiceEntryRequestSignal += 1
            entrySheetOpen = true
            scope.launch { entrySheetState.show() }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("语音记账已关闭，可在设置中重新开启")
            }
        }
    }

    CompositionLocalProvider(LocalDensity provides appDensity) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.statusBars,
            snackbarHost = {
                SnackbarHost(snackbarHostState) { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = Color(0xFF1A1A1A),
                        contentColor = Color.White,
                        actionColor = Color.White,
                        dismissActionContentColor = Color.White
                    )
                }
            },
            bottomBar = {
                AppBottomBar(
                    selectedTab = selectedTab,
                    onAdd = openManualEntry,
                    onVoice = openVoiceEntry,
                    voiceEnabled = entryPreferences.voiceEnabled,
                    onSelected = { tab ->
                        if (tab != selectedTab) {
                            navController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(AppTab.Home.route) {
                                    saveState = true
                                }
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                val openEditor: (TransactionWithDetails) -> Unit = { transaction ->
                    pendingEntryPrefillDraft = null
                    voiceEntryRequestSignal = 0
                    viewModel.startEditTransaction(transaction)
                    entrySheetOpen = true
                    scope.launch { entrySheetState.show() }
                }
                NavHost(
                    navController = navController,
                    startDestination = AppTab.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(AppTab.Home.route) {
                        HomeScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            onEditTransaction = openEditor,
                            onOpenSearch = {
                                pendingStatsViewMode = StatsInitialViewMode.Flow
                                navController.navigate(AppTab.Stats.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(AppTab.Home.route) {
                                        saveState = true
                                    }
                                }
                            },
                            amountsHidden = amountsHidden
                        )
                    }
                    composable(AppTab.Stats.route) {
                        StatsScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            onEditTransaction = openEditor,
                            amountsHidden = amountsHidden,
                            initialAccountFilterId = pendingStatsAccountFilterId,
                            onInitialAccountFilterConsumed = { pendingStatsAccountFilterId = null },
                            initialViewMode = pendingStatsViewMode,
                            onInitialViewModeConsumed = { pendingStatsViewMode = null }
                        )
                    }
                    composable(AppTab.Assets.route) {
                        AssetsScreen(
                            uiState = uiState,
                            amountsHidden = amountsHidden,
                            onToggleAmountsHidden = {
                                amountsHidden = !amountsHidden
                                preferenceStore.saveAmountsHidden(amountsHidden)
                            },
                            onOpenAccountManagement = {
                                pendingSettingsRoute = MineRoute.Accounts
                                navController.navigate(AppTab.Settings.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(AppTab.Home.route) {
                                        saveState = true
                                    }
                                }
                            },
                            onCreateTransfer = {
                                viewModel.cancelEditTransaction()
                                voiceEntryRequestSignal = 0
                                pendingEntryPrefillDraft = EntryPrefillDraft(type = TransactionType.TRANSFER)
                                entrySheetOpen = true
                                scope.launch { entrySheetState.show() }
                            },
                            onOpenAccountFlow = { accountId ->
                                pendingStatsAccountFilterId = accountId
                                navController.navigate(AppTab.Stats.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(AppTab.Home.route) {
                                        saveState = true
                                    }
                                }
                            }
                        )
                    }
                    composable(AppTab.Settings.route) {
                        MineScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            initialRoute = pendingSettingsRoute,
                            onInitialRouteConsumed = { pendingSettingsRoute = null },
                            amountsHidden = amountsHidden,
                            entryPreferences = entryPreferences,
                            onEntryPreferencesChange = {
                                entryPreferences = it
                                preferenceStore.saveEntryPreferences(it)
                            },
                            themeMode = themeMode,
                            onThemeModeChange = onThemeModeChange,
                            webDavConfig = webDavConfig,
                            onWebDavConfigChange = {
                                webDavConfig = it
                                preferenceStore.saveWebDavConfig(it)
                            }
                        )
                    }
                }
            }
        }

        if (entrySheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    entryDismissRequestSignal += 1
                },
                sheetState = entrySheetState,
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = null
            ) {
                EntrySheetContentV2(
                    uiState = uiState,
                    viewModel = viewModel,
                    prefillDraft = pendingEntryPrefillDraft,
                    voiceEntryRequestSignal = voiceEntryRequestSignal,
                    entryPreferences = entryPreferences,
                    dismissRequestSignal = entryDismissRequestSignal,
                    onPrefillConsumed = { pendingEntryPrefillDraft = null },
                    onVoiceEntryRequestConsumed = { voiceEntryRequestSignal = 0 },
                    onDone = {
                        viewModel.cancelEditTransaction()
                        voiceEntryRequestSignal = 0
                        entrySheetOpen = false
                    }
                )
            }
        }
    }
}

@Composable
internal fun AppBottomBar(
    selectedTab: AppTab,
    onAdd: () -> Unit,
    onVoice: () -> Unit,
    voiceEnabled: Boolean,
    onSelected: (AppTab) -> Unit
) {
    val tabs = AppTab.entries
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(68.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {}
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            tabs.take(2).forEach { tab ->
                AppBottomBarItem(tab = tab, selected = selectedTab == tab, onSelected = onSelected)
            }
            BottomBarEntryAction(
                onAdd = onAdd,
                onVoice = onVoice,
                voiceEnabled = voiceEnabled,
                modifier = Modifier.weight(1f)
            )
            tabs.drop(2).forEach { tab ->
                AppBottomBarItem(tab = tab, selected = selectedTab == tab, onSelected = onSelected)
            }
        }
    }
}

@Composable
private fun RowScope.AppBottomBarItem(
    tab: AppTab,
    selected: Boolean,
    onSelected: (AppTab) -> Unit
) {
    val label = stringResource(tab.labelRes)
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        modifier = Modifier
            .weight(1f)
            .height(60.dp)
            .clickable { onSelected(tab) }
            .padding(top = 6.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(width = 44.dp, height = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Surface(
                    modifier = Modifier.size(width = 42.dp, height = 28.dp),
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                ) {}
            }
            Icon(
                imageVector = tab.icon,
                contentDescription = label,
                modifier = Modifier.size(21.dp),
                tint = contentColor
            )
        }
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomBarEntryAction(
    onAdd: () -> Unit,
    onVoice: () -> Unit,
    voiceEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = modifier.height(76.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(58.dp)
                .shadow(8.dp, shape)
                .combinedClickable(
                    onClick = onAdd,
                    onLongClick = { if (voiceEnabled) onVoice() else onAdd() }
                ),
            shape = shape,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add_entry),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

internal fun voiceRecognitionIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, VOICE_RECOGNITION_MIN_DURATION_MS)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, VOICE_RECOGNITION_COMPLETE_SILENCE_MS)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, VOICE_RECOGNITION_POSSIBLE_SILENCE_MS)
    }
}

internal fun hasRecordAudioPermission(context: Context): Boolean {
    return androidx.core.content.ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}

internal fun speechRecognitionErrorMessage(error: Int, appHasMicPermission: Boolean): String = when (error) {
    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "网络超时"
    SpeechRecognizer.ERROR_NETWORK -> "网络连接错误"
    SpeechRecognizer.ERROR_AUDIO -> "音频录制错误"
    SpeechRecognizer.ERROR_SERVER -> "服务器错误"
    SpeechRecognizer.ERROR_CLIENT -> "客户端错误"
    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "未检测到语音输入"
    SpeechRecognizer.ERROR_NO_MATCH -> "未匹配到任何结果"
    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "语音识别服务忙，请稍候"
    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
        if (appHasMicPermission) {
            "本应用麦克风权限已允许，但系统语音识别服务拒绝录音。请给小爱语音引擎或当前语音识别服务开启麦克风权限。"
        } else {
            "本应用缺少麦克风权限，请允许后重试。"
        }
    }
    else -> "未知错误 ($error)"
}

internal enum class AppTab(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector
) {
    Home("home", R.string.tab_home, Icons.Default.Home),
    Stats("stats", R.string.tab_stats, Icons.Default.PieChart),
    Assets("assets", R.string.tab_assets, Icons.Default.AccountBalanceWallet),
    Settings("settings", R.string.tab_settings, Icons.Default.Settings)
}

internal enum class LedgerViewMode(val label: String) {
    List("列表"),
    Calendar("日历")
}

internal enum class DateTimePickerMode {
    Date,
    Time
}
