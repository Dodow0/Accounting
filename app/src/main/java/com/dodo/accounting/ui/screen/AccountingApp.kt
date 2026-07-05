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
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
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
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

internal val LedgerMint: Color
    @Composable get() = colorResource(R.color.ledger_mint)

internal val LedgerPanel: Color
    @Composable get() = colorResource(R.color.ledger_panel)

internal val LedgerDivider: Color
    @Composable get() = colorResource(R.color.ledger_divider)

internal val LedgerExpensePink: Color
    @Composable get() = colorResource(R.color.ledger_expense_pink)

internal val LedgerCardShape = RoundedCornerShape(20.dp)

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

    var entrySheetOpen by remember { mutableStateOf(false) }
    var voiceSheetOpen by remember { mutableStateOf(false) }
    var pendingSettingsRoute by remember { mutableStateOf<String?>(null) }
    var pendingEntryPrefillDraft by remember { mutableStateOf<EntryPrefillDraft?>(null) }
    var pendingStatsAccountFilterId by remember { mutableStateOf<Long?>(null) }
    var entryDismissRequestSignal by remember { mutableIntStateOf(0) }
    var amountsHidden by remember { mutableStateOf(false) }
    var entryPreferences by remember { mutableStateOf(EntryPreferences()) }
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
                            onOpenSearch = {},
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
                            onInitialAccountFilterConsumed = { pendingStatsAccountFilterId = null }
                        )
                    }
                    composable(AppTab.Assets.route) {
                        AssetsScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            onEditTransaction = openEditor,
                            amountsHidden = amountsHidden,
                            onToggleAmountsHidden = { amountsHidden = !amountsHidden },
                            onCreateTransaction = { draft ->
                                viewModel.cancelEditTransaction()
                                pendingEntryPrefillDraft = draft
                                entrySheetOpen = true
                                scope.launch { entrySheetState.show() }
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
                            onToggleAmountsHidden = { amountsHidden = !amountsHidden },
                            entryPreferences = entryPreferences,
                            onEntryPreferencesChange = { entryPreferences = it }
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
                    onDone = {
                        entrySheetOpen = false
                        viewModel.cancelEditTransaction()
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
                    onUseManualEntry = {
                        voiceSheetOpen = false
                        pendingEntryPrefillDraft = null
                        viewModel.cancelEditTransaction()
                        entrySheetOpen = true
                        scope.launch { entrySheetState.show() }
                    },
                    onRecognizedDraft = { draft ->
                        voiceSheetOpen = false
                        viewModel.cancelEditTransaction()
                        pendingEntryPrefillDraft = draft
                        entrySheetOpen = true
                        scope.launch { entrySheetState.show() }
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
    onUseManualEntry: () -> Unit,
    onRecognizedDraft: (EntryPrefillDraft) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewConfiguration = LocalViewConfiguration.current
    var recognizedText by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf("等待语音转文字") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isListening by remember { mutableStateOf(false) }
    var voiceLevel by remember { mutableFloatStateOf(0f) }
    var hasMicPermission by remember { mutableStateOf(hasRecordAudioPermission(context)) }
    var showSpeechServicePermissionGuide by remember { mutableStateOf(false) }
    val speechServicePackage = remember(context) { defaultSpeechRecognitionServicePackage(context) }
    val canQuerySpeechService = remember(context) { SpeechRecognizer.isRecognitionAvailable(context) }
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
    val voiceGuideExamples = remember(uiState.activeAccounts) { voiceGuideExamples(uiState) }
    val previewDraft = remember(
        recognizedText,
        uiState.activeAccounts,
        uiState.incomeCategories,
        uiState.expenseCategories,
        uiState.tags
    ) {
        recognizedText.trim()
            .takeIf { it.isNotBlank() }
            ?.toEntryPrefillDraft(uiState)
    }

    fun createDraftFromText() {
        val text = recognizedText.trim()
        if (text.isBlank()) {
            errorText = "请先完成语音转文字"
            return
        }
        val draft = text.toEntryPrefillDraft(uiState)
        if (draft.amount.isBlank()) {
            errorText = "没有识别到金额，可以补充文字，比如“午饭花了28”"
            return
        }
        errorText = null
        onRecognizedDraft(draft)
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

    fun stopListening() {
        speechRecognizer?.stopListening()
        isListening = false
        voiceLevel = 0f
        statusText = "正在整理文字..."
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
                    statusText = "已转成文字"
                    errorText = null
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val text = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                if (text.isNotBlank()) {
                    recognizedText = text
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
                    .pointerInput(speechRecognizer, hasMicPermission) {
                        if (speechRecognizer == null) return@pointerInput
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val releasedBeforeLongPress = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                                var released = false
                                while (!released) {
                                    val event = awaitPointerEvent()
                                    if (event.changes.none { it.id == down.id && it.pressed }) {
                                        released = true
                                    }
                                }
                                released
                            } ?: false
                            if (!releasedBeforeLongPress) {
                                errorText = null
                                if (!hasMicPermission) {
                                    statusText = "需要麦克风权限"
                                    errorText = "请允许麦克风权限后，重新按住说话。"
                                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    recognizedText = ""
                                    startListening()
                                }
                                while (true) {
                                    val event = awaitPointerEvent()
                                    if (event.changes.none { it.id == down.id && it.pressed }) {
                                        break
                                    }
                                }
                                stopListening()
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
                isListening -> "按住说话，松手结束"
                else -> "$statusText · 长按麦克风开始"
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        VoiceGuideExamples(
            examples = voiceGuideExamples,
            onExampleSelected = { example ->
                recognizedText = example
                errorText = null
                statusText = "已填入示例文字"
            }
        )
        MinimalInputLine(
            value = recognizedText,
            onValueChange = {
                recognizedText = it
                errorText = null
                statusText = if (it.isBlank()) "等待语音转文字" else "已转成文字"
            },
            placeholder = "识别文本",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            maxLines = 4
        )
        previewDraft?.let { draft ->
            VoiceStructuredPreview(
                draft = draft,
                uiState = uiState
            )
        }
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
                label = "用这段文字记账",
                icon = Icons.Default.Edit,
                onClick = { createDraftFromText() },
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                enabled = recognizedText.isNotBlank()
            )
        }
        LedgerActionButton(
            label = "改为手动输入",
            icon = Icons.Default.Edit,
            onClick = onUseManualEntry,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            borderColor = LedgerDivider
        )
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
private fun VoiceStructuredPreview(
    draft: EntryPrefillDraft,
    uiState: AccountingUiState
) {
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "本地解析预览",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "类型：$typeLabel  金额：${draft.amount.ifBlank { "未识别" }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "账户：$accountName  分类：$categoryName",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun String.toEntryPrefillDraft(uiState: AccountingUiState): EntryPrefillDraft {
    val text = trim()
    val type = when {
        transferVoiceKeywords.any { text.contains(it, ignoreCase = true) } -> TransactionType.TRANSFER
        incomeVoiceKeywords.any { text.contains(it, ignoreCase = true) } -> TransactionType.INCOME
        else -> TransactionType.EXPENSE
    }
    val categories = when (type) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }
    val category = findVoiceCategory(text, categories, type)
    val account = if (type == TransactionType.TRANSFER) null else findVoiceAccount(text, uiState.activeAccounts)
    val fromAccount = if (type == TransactionType.TRANSFER) {
        findTransferSourceAccount(text, uiState.activeAccounts)
    } else {
        null
    }
    val toAccount = if (type == TransactionType.TRANSFER) {
        findTransferTargetAccount(text, uiState.activeAccounts, fromAccount)
    } else {
        null
    }
    val tagIds = uiState.tags
        .filter { text.contains(it.name, ignoreCase = true) }
        .map { it.id }
        .toSet()

    return EntryPrefillDraft(
        type = type,
        amount = extractVoiceAmount(text),
        accountId = account?.id,
        fromAccountId = fromAccount?.id,
        toAccountId = toAccount?.id,
        categoryId = category?.id,
        note = text,
        tagIds = tagIds,
        occurredAt = voiceOccurredAt(text)
    )
}

private val transferVoiceKeywords = listOf("转账", "转到", "转入", "转出", "从")

private fun voiceGuideExamples(uiState: AccountingUiState): List<String> {
    val preferredAccount = uiState.activeAccounts.firstOrNull()?.name ?: "支付宝"
    val secondAccount = uiState.activeAccounts.drop(1).firstOrNull()?.name ?: "微信"
    return listOf(
        "今天 午饭 二十八 $preferredAccount",
        "昨天 打车 36 $secondAccount",
        "工资到账 八千 $preferredAccount",
        "从$preferredAccount 转到 $secondAccount 五十"
    )
}

private val incomeVoiceKeywords = listOf("收入", "工资", "到账", "入账", "收到", "报销", "奖金", "退款", "兼职", "收益", "红包")

internal fun extractVoiceAmount(text: String): String {
    val numericMatches = Regex("""\d+(?:\.\d{1,2})?""")
        .findAll(text.replace(',', '.').replace('，', '.'))
        .map { it.value }
        .toList()
    numericMatches.lastOrNull()?.let { return it }

    return Regex("""[零〇一二两俩三四五六七八九十百千万亿点块元圆毛角分]+""")
        .findAll(text)
        .map { it.value }
        .filter { segment ->
            segment.length > 1 || segment.any { it in "十百千万亿点块元圆毛角分" }
        }
        .mapNotNull { parseChineseAmount(it) }
        .lastOrNull()
        ?.stripTrailingZeros()
        ?.toPlainString()
        .orEmpty()
}

private fun parseChineseAmount(raw: String): BigDecimal? {
    val text = raw
        .replace("人民币", "")
        .replace("块钱", "块")
        .replace('圆', '元')
        .replace('俩', '两')
        .trim()
    if (text.isBlank()) return null

    val currencyIndex = text.indexOfFirst { it in "块元" }
    if (currencyIndex >= 0) {
        val integerPart = text.substring(0, currencyIndex).ifBlank { "零" }
        val fractionPart = text.substring(currencyIndex + 1)
        val integer = parseChineseInteger(integerPart) ?: return null
        return BigDecimal.valueOf(integer).add(parseChineseFraction(fractionPart))
    }

    if (text.any { it in "毛角分" }) {
        return parseChineseFraction(text)
    }

    val pointIndex = text.indexOf('点')
    if (pointIndex >= 0) {
        val integer = parseChineseInteger(text.substring(0, pointIndex).ifBlank { "零" }) ?: return null
        val decimalDigits = text.substring(pointIndex + 1)
            .mapNotNull { chineseDigitValue(it) }
            .joinToString("")
        if (decimalDigits.isBlank()) return BigDecimal.valueOf(integer)
        return BigDecimal("${integer}.${decimalDigits.take(2)}")
    }

    return parseChineseInteger(text)?.let { BigDecimal.valueOf(it) }
}

private fun parseChineseFraction(text: String): BigDecimal {
    if (text.isBlank()) return BigDecimal.ZERO
    val normalized = text.replace('毛', '角')
    val pureDigits = normalized.filter { chineseDigitValue(it) != null }
    if ('角' !in normalized && '分' !in normalized && pureDigits.length == 1) {
        return BigDecimal("0.${chineseDigitValue(pureDigits.first()) ?: 0}")
    }
    val jiao = normalized.substringBefore("角", missingDelimiterValue = "")
        .lastOrNull()
        ?.let { chineseDigitValue(it) }
    val fenSource = when {
        "分" in normalized -> normalized.substringBefore("分").substringAfterLast("角")
        "角" in normalized -> normalized.substringAfter("角")
        else -> normalized
    }
    val fenDigits = fenSource.mapNotNull { chineseDigitValue(it) }
    val tenths = jiao ?: fenDigits.getOrNull(0)
    val hundredths = if (jiao != null) fenDigits.getOrNull(0) else fenDigits.getOrNull(1)
    val decimal = buildString {
        append(tenths ?: 0)
        append(hundredths ?: 0)
    }
    return BigDecimal("0.$decimal")
}

private fun parseChineseInteger(text: String): Long? {
    if (text.isBlank()) return 0L
    var result = 0L
    var section = 0L
    var number = 0L
    text.forEach { char ->
        when (char) {
            '零', '〇' -> number = 0L
            '十' -> {
                section += (if (number == 0L) 1L else number) * 10L
                number = 0L
            }
            '百' -> {
                section += (if (number == 0L) 1L else number) * 100L
                number = 0L
            }
            '千' -> {
                section += (if (number == 0L) 1L else number) * 1000L
                number = 0L
            }
            '万' -> {
                result += (section + number) * 10_000L
                section = 0L
                number = 0L
            }
            '亿' -> {
                result += (section + number) * 100_000_000L
                section = 0L
                number = 0L
            }
            else -> {
                val digit = chineseDigitValue(char) ?: return null
                number = digit.toLong()
            }
        }
    }
    return result + section + number
}

private fun chineseDigitValue(char: Char): Int? = when (char) {
    '零', '〇' -> 0
    '一' -> 1
    '二', '两' -> 2
    '三' -> 3
    '四' -> 4
    '五' -> 5
    '六' -> 6
    '七' -> 7
    '八' -> 8
    '九' -> 9
    else -> null
}

private fun findVoiceAccount(text: String, accounts: List<AccountEntity>): AccountEntity? {
    return accounts.firstOrNull { account ->
        textMatchesAccount(text, account)
    } ?: accounts.firstOrNull()
}

private fun findTransferSourceAccount(text: String, accounts: List<AccountEntity>): AccountEntity? {
    val fromSegment = text
        .substringAfter("从", missingDelimiterValue = text)
        .substringBefore("转到")
        .substringBefore("转入")
        .substringBefore("到")
        .substringBefore("转")
    return accounts.firstOrNull { textMatchesAccount(fromSegment, it) }
        ?: accounts.firstOrNull()
}

private fun findTransferTargetAccount(
    text: String,
    accounts: List<AccountEntity>,
    sourceAccount: AccountEntity?
): AccountEntity? {
    val targetSegment = when {
        "转到" in text -> text.substringAfter("转到")
        "转入" in text -> text.substringAfter("转入")
        "到" in text -> text.substringAfterLast("到")
        else -> text
    }
    return accounts.firstOrNull { account ->
        account.id != sourceAccount?.id && textMatchesAccount(targetSegment, account)
    } ?: accounts.firstOrNull { it.id != sourceAccount?.id }
}

private fun textMatchesAccount(text: String, account: AccountEntity): Boolean {
    return text.contains(account.name, ignoreCase = true) ||
        accountVoiceAliases(account.type).any { text.contains(it, ignoreCase = true) }
}

private fun accountVoiceAliases(type: AccountType): List<String> = when (type) {
    AccountType.CASH -> listOf("现金")
    AccountType.BANK_CARD -> listOf("银行卡", "储蓄卡", "卡里")
    AccountType.THIRD_PARTY_PAYMENT -> listOf("微信", "支付宝", "支付")
    AccountType.STORED_VALUE_CARD -> listOf("储值卡")
    AccountType.TRANSIT_CARD -> listOf("公交卡", "交通卡")
    AccountType.DIGITAL_BALANCE -> listOf("余额")
    AccountType.CREDIT -> listOf("信用卡", "花呗", "白条")
    AccountType.CUSTOM -> emptyList()
}

private fun findVoiceCategory(
    text: String,
    categories: List<CategoryEntity>,
    type: TransactionType
): CategoryEntity? {
    categories.firstOrNull { text.contains(it.name, ignoreCase = true) }?.let { return it }
    val hints = if (type == TransactionType.INCOME) incomeCategoryHints else expenseCategoryHints
    return categories.firstOrNull { category ->
        hints[category.iconName].orEmpty().any { text.contains(it, ignoreCase = true) }
    }
}

private val expenseCategoryHints = mapOf(
    "restaurant" to listOf("饭", "餐", "午饭", "晚饭", "早餐", "外卖", "咖啡", "奶茶", "饮品", "吃"),
    "commute" to listOf("打车", "地铁", "公交", "交通", "车费", "通勤"),
    "directions_bus" to listOf("公交", "地铁", "车票", "交通卡"),
    "shopping_bag" to listOf("购物", "买", "超市", "衣服"),
    "storefront" to listOf("门店", "超市", "便利店"),
    "devices" to listOf("数码", "手机", "电脑", "耳机"),
    "phone_iphone" to listOf("话费", "手机", "流量"),
    "home" to listOf("房租", "水电", "居家", "物业"),
    "credit_card" to listOf("信用卡", "还款"),
    "receipt_long" to listOf("账单", "缴费")
)

private val incomeCategoryHints = mapOf(
    "work" to listOf("工资", "薪水", "薪资"),
    "redeem" to listOf("红包", "奖励"),
    "add_card" to listOf("入账", "到账"),
    "payments" to listOf("报销", "退款", "收到"),
    "wallet" to listOf("兼职", "收入"),
    "assessment" to listOf("收益", "理财", "分红")
)

private fun voiceOccurredAt(text: String): Long {
    val calendar = Calendar.getInstance()
    when {
        text.contains("前天") -> calendar.add(Calendar.DAY_OF_YEAR, -2)
        text.contains("昨天") -> calendar.add(Calendar.DAY_OF_YEAR, -1)
    }
    return calendar.timeInMillis
}

private fun voiceRecognitionIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
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
