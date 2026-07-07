package com.dodo.accounting.ui.screen

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Mic
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.dodo.accounting.data.local.entity.AccountType
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
import kotlinx.coroutines.withTimeoutOrNull
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

private const val VOICE_RECOGNITION_MAX_DURATION_MS = 20_000L

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
    var voiceSheetOpen by remember { mutableStateOf(false) }
    var pendingSettingsRoute by remember { mutableStateOf<String?>(null) }
    var pendingEntryPrefillDraft by remember { mutableStateOf<EntryPrefillDraft?>(null) }
    var pendingVoiceDraftQueue by remember { mutableStateOf<List<EntryPrefillDraft>>(emptyList()) }
    var openEntryAfterVoiceDismiss by remember { mutableStateOf(false) }
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

    LaunchedEffect(voiceSheetOpen, openEntryAfterVoiceDismiss) {
        if (!voiceSheetOpen && openEntryAfterVoiceDismiss) {
            delay(260)
            entrySheetOpen = true
            delay(50)
            entrySheetState.show()
            openEntryAfterVoiceDismiss = false
        }
    }

    val openManualEntry: () -> Unit = {
        viewModel.cancelEditTransaction()
        pendingEntryPrefillDraft = null
        entrySheetOpen = true
        scope.launch { entrySheetState.show() }
    }

    val openVoiceEntry: () -> Unit = {
        if (entryPreferences.voiceEnabled) {
            voiceSheetOpen = true
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
            },
            floatingActionButton = {
                if (selectedTab == AppTab.Home) {
                    HomeFloatingEntryButtons(
                        onAdd = openManualEntry,
                        onVoice = openVoiceEntry,
                        voiceEnabled = entryPreferences.voiceEnabled
                    )
                }
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
                    entryPreferences = entryPreferences,
                    dismissRequestSignal = entryDismissRequestSignal,
                    onPrefillConsumed = { pendingEntryPrefillDraft = null },
                    onDone = { saved ->
                        viewModel.cancelEditTransaction()
                        val nextDraft = if (saved) pendingVoiceDraftQueue.firstOrNull() else null
                        if (nextDraft != null) {
                            pendingVoiceDraftQueue = pendingVoiceDraftQueue.drop(1)
                            pendingEntryPrefillDraft = nextDraft
                            entrySheetOpen = true
                            scope.launch { entrySheetState.show() }
                        } else {
                            if (!saved) pendingVoiceDraftQueue = emptyList()
                            entrySheetOpen = false
                        }
                    }
                )
            }
        }
        if (voiceSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { voiceSheetOpen = false },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                dragHandle = null
            ) {
                VoiceEntrySheet(
                    uiState = uiState,
                    onDismiss = { voiceSheetOpen = false },
                    onRecognizedDrafts = { drafts ->
                        val firstDraft = drafts.firstOrNull()
                        if (firstDraft != null) {
                            viewModel.cancelEditTransaction()
                            pendingVoiceDraftQueue = drafts.drop(1)
                            pendingEntryPrefillDraft = firstDraft
                            openEntryAfterVoiceDismiss = true
                            voiceSheetOpen = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
internal fun AppBottomBar(
    selectedTab: AppTab,
    onSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        AppTab.entries.forEach { tab ->
            val label = stringResource(tab.labelRes)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = selectedTab == tab,
                onClick = { onSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
internal fun HomeFloatingEntryButtons(
    onAdd: () -> Unit,
    onVoice: () -> Unit,
    voiceEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    Column(
        modifier = modifier
            .shadow(6.dp, shape)
            .background(MaterialTheme.colorScheme.primary, shape)
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onAdd,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.action_add_entry),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .width(24.dp)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.20f)
        )

        IconButton(
            onClick = onVoice,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = stringResource(R.string.action_voice_entry),
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = if (voiceEnabled) 1f else 0.48f)
            )
        }
    }
}

@Composable
internal fun VoiceEntrySheet(
    uiState: AccountingUiState,
    onDismiss: () -> Unit,
    onRecognizedDrafts: (List<EntryPrefillDraft>) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var recognizedText by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf("等待语音转文字") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isListening by remember { mutableStateOf(false) }
    var voiceLevel by remember { mutableFloatStateOf(0f) }
    var continuousMode by remember { mutableStateOf(false) }
    var voiceResults by remember { mutableStateOf<List<VoiceEntryParseResult>>(emptyList()) }
    var hasMicPermission by remember { mutableStateOf(hasRecordAudioPermission(context)) }
    var showSpeechServicePermissionGuide by remember { mutableStateOf(false) }
    val speechServicePackage = remember(context) { defaultSpeechRecognitionServicePackage(context) }
    val canQuerySpeechService = remember(context) { SpeechRecognizer.isRecognitionAvailable(context) }
    val voiceEntryParser = remember { VoiceEntryParser() }
    val speechRecognizer = remember(context) {
        runCatching { SpeechRecognizer.createSpeechRecognizer(context) }.getOrNull()
    }
    val glowSize by animateFloatAsState(
        targetValue = if (isListening) 78f + voiceLevel * 24f else 72f,
        animationSpec = tween(durationMillis = 160),
        label = "voiceGlowSize"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isListening) 0.10f + voiceLevel * 0.12f else 0.06f,
        animationSpec = tween(durationMillis = 160),
        label = "voiceGlowAlpha"
    )
    val voiceGuideExamples = remember(uiState.activeAccounts, uiState.recentTransactions) {
        voiceEntryParser.guideExamples(uiState)
    }
    fun parseRecognizedText(text: String): List<VoiceEntryParseResult> {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return emptyList()
        return if (continuousMode) {
            voiceEntryParser.parseMany(trimmed, uiState)
        } else {
            listOf(voiceEntryParser.parseDetailed(trimmed, uiState))
        }
    }

    fun submitResults(results: List<VoiceEntryParseResult>) {
        val text = recognizedText.trim()
        if (text.isBlank() && results.isEmpty()) {
            errorText = "请先完成语音转文字"
            return
        }
        val sourceResults = results.ifEmpty { parseRecognizedText(text) }
        val usableDrafts = sourceResults
            .let { if (continuousMode) it else it.take(1) }
            .map { it.draft }
        if (usableDrafts.isEmpty()) {
            errorText = "请先完成语音转文字"
            return
        }
        errorText = null
        onRecognizedDrafts(usableDrafts)
    }

    fun startListening(permissionGranted: Boolean = hasRecordAudioPermission(context)) {
        errorText = null
        showSpeechServicePermissionGuide = false
        if (speechRecognizer == null) {
            statusText = "语音转文字不可用"
            errorText = "当前设备没有可用的系统语音识别服务"
            return
        }
        hasMicPermission = permissionGranted
        if (!permissionGranted) {
            statusText = "需要麦克风权限"
            errorText = "请允许麦克风权限后再开始语音转文字"
            return
        }
        statusText = "正在启动系统语音识别..."
        runCatching {
            speechRecognizer.cancel()
            speechRecognizer.startListening(voiceRecognitionIntent())
        }.onFailure { error ->
            isListening = false
            voiceLevel = 0f
            statusText = "语音转文字启动失败"
            errorText = error.localizedMessage?.let { "无法启动系统语音识别：$it" } ?: "无法启动系统语音识别"
        }
    }

    fun stopListening(nextStatus: String = "正在整理文字...") {
        speechRecognizer?.stopListening()
        isListening = false
        voiceLevel = 0f
        statusText = nextStatus
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
        if (granted) {
            statusText = "已允许麦克风权限，请按住说话"
            errorText = null
        } else {
            isListening = false
            voiceLevel = 0f
            statusText = "需要麦克风权限"
            errorText = "语音转文字需要麦克风权限；你也可以先手动输入文字。"
        }
    }

    fun requestStartListening(): Boolean {
        if (speechRecognizer == null) {
            statusText = "语音转文字不可用"
            errorText = "当前设备没有可用的系统语音识别服务"
            return false
        }
        val permissionGranted = hasRecordAudioPermission(context)
        hasMicPermission = permissionGranted
        if (!permissionGranted) {
            statusText = "需要麦克风权限"
            errorText = "请允许麦克风权限后，重新开始语音转文字。"
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return false
        }
        recognizedText = ""
        if (!continuousMode) voiceResults = emptyList()
        startListening(permissionGranted = true)
        return true
    }

    androidx.compose.runtime.DisposableEffect(speechRecognizer) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                voiceLevel = 0.15f
                statusText = "正在听，请说话"
                errorText = null
            }

            override fun onBeginningOfSpeech() {
                isListening = true
                statusText = "正在识别..."
            }

            override fun onRmsChanged(rmsdB: Float) {
                voiceLevel = (rmsdB.coerceIn(0f, 12f) / 12f)
            }

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                isListening = false
                voiceLevel = 0f
                statusText = "正在整理文字..."
            }

            override fun onError(error: Int) {
                isListening = false
                voiceLevel = 0f
                val appHasMicPermission = hasRecordAudioPermission(context)
                hasMicPermission = appHasMicPermission
                showSpeechServicePermissionGuide =
                    error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS && appHasMicPermission
                statusText = "语音转文字失败"
                errorText = speechRecognitionErrorMessage(error, appHasMicPermission)
            }

            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                isListening = false
                voiceLevel = 0f
                if (text.isBlank()) {
                    statusText = "未识别到文字"
                    errorText = "没有听清楚，再试一次，或先手动输入文字。"
                } else {
                    recognizedText = text
                    val parsed = parseRecognizedText(text)
                    val nextResults = if (continuousMode) voiceResults + parsed else parsed
                    voiceResults = nextResults
                    statusText = "已转成文字"
                    errorText = null
                    submitResults(nextResults)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val text = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                if (text.isNotBlank()) {
                    recognizedText = text
                    if (!continuousMode) {
                        voiceResults = parseRecognizedText(text)
                    }
                    statusText = "正在转文字..."
                    errorText = null
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        }

        speechRecognizer?.setRecognitionListener(listener)
        onDispose {
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 42.dp, height = 5.dp),
            shape = RoundedCornerShape(999.dp),
            color = LedgerDivider
        ) {}
        Text("语音转文字", style = MaterialTheme.typography.titleLarge)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(glowSize.dp),
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
            ) {}
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .pointerInput(speechRecognizer, hasMicPermission, continuousMode) {
                        if (speechRecognizer == null) return@pointerInput
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            val started = requestStartListening()
                            if (!started) return@awaitEachGesture
                            val releasedBeforeTimeout = withTimeoutOrNull(VOICE_RECOGNITION_MAX_DURATION_MS) {
                                do {
                                    val event = awaitPointerEvent()
                                    val stillPressed = event.changes.any { it.pressed }
                                } while (stillPressed)
                                true
                            } ?: false
                            if (releasedBeforeTimeout) {
                                stopListening()
                            } else {
                                stopListening("已达到最长识别时长，正在整理文字...")
                            }
                        }
                    },
                shape = RoundedCornerShape(999.dp),
                color = if (isListening) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.KeyboardVoice,
                        contentDescription = null,
                        tint = if (isListening) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
        }
        Text(
            when {
                speechRecognizer == null -> "无法创建系统语音识别器"
                !canQuerySpeechService -> "正在尝试调用系统语音识别服务"
                isListening -> "正在听，松手结束"
                else -> "$statusText · 长按麦克风开始，最长 ${VOICE_RECOGNITION_MAX_DURATION_MS / 1000} 秒"
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        VoiceGuideExamples(
            examples = voiceGuideExamples,
            onExampleSelected = { example ->
                recognizedText = example
                voiceResults = parseRecognizedText(example)
                errorText = null
                statusText = "已填入示例文字"
            }
        )
        VoiceContinuousModeRow(
            enabled = continuousMode,
            onToggle = {
                val nextContinuousMode = !continuousMode
                continuousMode = nextContinuousMode
                if (!nextContinuousMode && voiceResults.size > 1) {
                    voiceResults = recognizedText
                        .takeIf { it.isNotBlank() }
                        ?.let { listOf(voiceEntryParser.parseDetailed(it.trim(), uiState)) }
                        .orEmpty()
                }
            },
            queueSize = voiceResults.size
        )
        MinimalInputLine(
            value = recognizedText,
            onValueChange = {
                recognizedText = it
                voiceResults = parseRecognizedText(it)
                errorText = null
                statusText = if (it.isBlank()) "等待语音转文字" else "已转成文字"
            },
            placeholder = "识别文本",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            maxLines = 4
        )
        errorText?.let { error ->
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (showSpeechServicePermissionGuide) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, LedgerDivider)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "系统语音引擎需要麦克风权限",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        buildString {
                            append("请在打开的系统设置页进入“权限”，允许麦克风权限，返回后再试。")
                            speechServicePackage?.let { append(" 当前服务：$it") }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LedgerActionButton(
                        label = "打开语音引擎设置",
                        icon = Icons.Default.Settings,
                        onClick = { openSpeechRecognitionServiceSettings(context, speechServicePackage) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            LedgerActionButton(
                label = "清空",
                onClick = {
                    recognizedText = ""
                    voiceResults = emptyList()
                    errorText = null
                    statusText = "等待语音转文字"
                },
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                borderColor = LedgerDivider,
                enabled = recognizedText.isNotBlank()
            )
            LedgerActionButton(
                label = when {
                    voiceResults.size > 1 -> "按顺序确认 ${voiceResults.size} 笔"
                    voiceResults.isNotEmpty() -> "进入记账页面"
                    else -> "解析并进入"
                },
                icon = Icons.Default.Edit,
                onClick = { submitResults(voiceResults) },
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                enabled = recognizedText.isNotBlank() || voiceResults.isNotEmpty()
            )
        }
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("稍后再说")
        }
    }
}

@Composable
private fun VoiceGuideExamples(
    examples: List<String>,
    onExampleSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "你可以这样说",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            examples.forEach { example ->
                AssistChip(
                    onClick = { onExampleSelected(example) },
                    label = {
                        Text(
                            example,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun VoiceContinuousModeRow(
    enabled: Boolean,
    onToggle: () -> Unit,
    queueSize: Int
) {
    LedgerPanelSurface(onClick = onToggle) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "连续语音记账",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    if (enabled) "每次识别会追加到待确认队列，当前 $queueSize 笔" else "关闭时每次识别只保留最新结果",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = enabled, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
private fun VoiceResultQueue(
    results: List<VoiceEntryParseResult>,
    uiState: AccountingUiState,
    onUseOne: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (results.size > 1) {
            Text(
                "已拆出 ${results.size} 笔，保存一笔后会继续打开下一笔确认",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        results.forEachIndexed { index, result ->
            VoiceStructuredPreview(
                result = result,
                uiState = uiState,
                title = if (results.size > 1) "第 ${index + 1} 笔" else "本地解析预览",
                actionLabel = if (results.size > 1) "确认此笔" else "进入记账页面",
                onAction = { onUseOne(index) }
            )
        }
    }
}

@Composable
private fun VoiceStructuredPreview(
    result: VoiceEntryParseResult,
    uiState: AccountingUiState,
    title: String = "本地解析预览",
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val draft = result.draft
    val accountName = when (draft.type) {
        TransactionType.TRANSFER -> {
            val from = uiState.activeAccounts.firstOrNull { it.id == draft.fromAccountId }?.name ?: "未识别"
            val to = uiState.activeAccounts.firstOrNull { it.id == draft.toAccountId }?.name ?: "未识别"
            "$from -> $to"
        }
        else -> uiState.activeAccounts.firstOrNull { it.id == draft.accountId }?.name ?: "未识别"
    }
    val categories = when (draft.type) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }
    val categoryName = categories.firstOrNull { it.id == draft.categoryId }?.name ?: "未识别"
    val typeLabel = when (draft.type) {
        TransactionType.INCOME -> "收入"
        TransactionType.EXPENSE -> "支出"
        TransactionType.TRANSFER -> "转账"
        TransactionType.BALANCE_ADJUSTMENT -> "余额调整"
    }
    val accountLine = when (draft.type) {
        TransactionType.TRANSFER -> "账户：$accountName"
        TransactionType.BALANCE_ADJUSTMENT -> "账户：$accountName"
        else -> "账户：$accountName  分类：$categoryName"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, LedgerDivider)
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
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                VoiceConfidencePill(result.confidence)
            }
            Text(
                "类型：$typeLabel  金额：${draft.amount.ifBlank { "未识别" }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                accountLine,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (draft.merchant.isNotBlank()) {
                Text(
                    "商户：${draft.merchant}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            result.learnedHint?.let { hint ->
                Text(
                    hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                result.fields.forEach { field ->
                    VoiceParseFieldRow(field)
                }
            }
            if (actionLabel != null && onAction != null) {
                LedgerActionButton(
                    label = actionLabel,
                    icon = Icons.Default.Edit,
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                    enabled = draft.amount.isNotBlank()
                )
            }
        }
    }
}

@Composable
private fun VoiceConfidencePill(confidence: VoiceParseConfidence) {
    val color = when (confidence) {
        VoiceParseConfidence.HIGH -> MaterialTheme.colorScheme.primary
        VoiceParseConfidence.MEDIUM -> MaterialTheme.colorScheme.tertiary
        VoiceParseConfidence.LOW -> MaterialTheme.colorScheme.error
    }
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = color.copy(alpha = 0.11f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.24f))
    ) {
        Text(
            "置信度：${confidence.label}",
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun VoiceParseFieldRow(field: VoiceParseField) {
    val color = when (field.status) {
        VoiceParseFieldStatus.CONFIDENT -> MaterialTheme.colorScheme.primary
        VoiceParseFieldStatus.NEEDS_CONFIRM -> MaterialTheme.colorScheme.tertiary
        VoiceParseFieldStatus.MISSING -> MaterialTheme.colorScheme.error
    }
    val statusLabel = when (field.status) {
        VoiceParseFieldStatus.CONFIDENT -> "已识别"
        VoiceParseFieldStatus.NEEDS_CONFIRM -> "待确认"
        VoiceParseFieldStatus.MISSING -> "缺失"
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, color.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${field.label}：${field.value}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    field.message,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                statusLabel,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun voiceRecognitionIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, VOICE_RECOGNITION_MAX_DURATION_MS)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, VOICE_RECOGNITION_MAX_DURATION_MS)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 4_000L)
    }
}

private fun hasRecordAudioPermission(context: Context): Boolean {
    return androidx.core.content.ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}

private fun defaultSpeechRecognitionServicePackage(context: Context): String? {
    val serviceName = Settings.Secure.getString(
        context.contentResolver,
        "voice_recognition_service"
    )
    return serviceName
        ?.let { ComponentName.unflattenFromString(it) }
        ?.packageName
}

private fun openSpeechRecognitionServiceSettings(context: Context, packageName: String?) {
    val intent = if (packageName != null) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
    } else {
        Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
    }

    runCatching { context.startActivity(intent) }
        .onFailure { context.startActivity(Intent(Settings.ACTION_SETTINGS)) }
}

private fun speechRecognitionErrorMessage(error: Int, appHasMicPermission: Boolean): String = when (error) {
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
