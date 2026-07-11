package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.prefs.EntryPreferences

import android.Manifest
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.indication
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import com.dodo.accounting.domain.model.projectedCategoryBudgetCents
import com.dodo.accounting.domain.util.handleAmountKey
import com.dodo.accounting.domain.util.hasUnresolvedAmountExpression
import com.dodo.accounting.domain.util.normalizedAmountInput
import com.dodo.accounting.domain.voice.EntryPrefillDraft
import com.dodo.accounting.domain.voice.VoiceEntryParseResult
import com.dodo.accounting.domain.voice.VoiceEntryParser
import com.dodo.accounting.ui.viewmodel.EntryUiState
import com.dodo.accounting.ui.viewmodel.EntryViewModel
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

private data class EntryFormSnapshot(
    val type: TransactionType,
    val amount: String,
    val accountId: Long?,
    val fromAccountId: Long?,
    val toAccountId: Long?,
    val categoryId: Long?,
    val merchant: String,
    val note: String,
    val tagIds: Set<Long>,
    val occurredAt: Long
)

internal const val AMOUNT_KEY_SAVE_AND_CONTINUE = "再记一笔"

@Composable
internal fun EntrySheetContentV2(
    uiState: EntryUiState,
    viewModel: EntryViewModel,
    prefillDraft: EntryPrefillDraft? = null,
    voiceEntryRequestSignal: Int = 0,
    entryPreferences: EntryPreferences = EntryPreferences(),
    dismissRequestSignal: Int = 0,
    onPrefillConsumed: () -> Unit = {},
    onVoiceEntryRequestConsumed: () -> Unit = {},
    onDone: (saved: Boolean) -> Unit
) {
    val editing = uiState.editingTransaction
    val isEditingLegacyBalanceAdjustment = editing?.transaction?.type == TransactionType.BALANCE_ADJUSTMENT
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
    var isSubmitting by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDiscardConfirm by remember { mutableStateOf(false) }
    var pendingTagNameToSelect by remember { mutableStateOf<String?>(null) }
    var calculatorVisible by remember { mutableStateOf(true) }
    val contentScrollState = rememberScrollState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val latestUiState by rememberUpdatedState(uiState)
    val voiceEntryParser = remember { VoiceEntryParser() }
    val speechRecognizer = remember(context) {
        runCatching { SpeechRecognizer.createSpeechRecognizer(context) }.getOrNull()
    }
    var voicePanelVisible by remember { mutableStateOf(false) }
    var voiceRawText by remember { mutableStateOf("") }
    var voiceStatusText by remember { mutableStateOf("语音待命") }
    var voiceErrorText by remember { mutableStateOf<String?>(null) }
    var isVoiceListening by remember { mutableStateOf(false) }
    var voiceResult by remember { mutableStateOf<VoiceEntryParseResult?>(null) }
    var shouldStartVoiceAfterPermission by remember { mutableStateOf(false) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            voiceErrorText = null
            shouldStartVoiceAfterPermission = true
        } else {
            isVoiceListening = false
            voiceStatusText = "需要麦克风权限"
            voiceErrorText = "语音记账需要麦克风权限；也可以先手动记账。"
        }
    }

    fun activeAccountIdOrNull(id: Long?, state: EntryUiState = uiState): Long? {
        return state.activeAccounts.firstOrNull { it.id == id }?.id
    }

    fun preferredAccountId(state: EntryUiState = uiState): Long? {
        return activeAccountIdOrNull(entryPreferences.defaultAccountId, state)
            ?: state.activeAccounts.firstOrNull()?.id
    }

    fun transferTargetFor(sourceAccountId: Long?, state: EntryUiState = uiState): Long? {
        return state.activeAccounts.firstOrNull { it.id != sourceAccountId }?.id
    }

    fun applyPrefillDraft(draft: EntryPrefillDraft, state: EntryUiState = uiState) {
        if (editing != null) return
        val defaultAccountId = preferredAccountId(state)
        val draftAccountId = activeAccountIdOrNull(draft.accountId, state)
        val draftFromAccountId = activeAccountIdOrNull(draft.fromAccountId, state) ?: defaultAccountId
        val draftToAccountId = activeAccountIdOrNull(draft.toAccountId, state)
        selectedType = draft.type
        amount = draft.amount
        accountId = draftAccountId ?: defaultAccountId
        fromAccountId = draftFromAccountId
        toAccountId = draftToAccountId
            ?.takeIf { it != draftFromAccountId }
            ?: transferTargetFor(draftFromAccountId, state)
        categoryId = draft.categoryId
        merchant = draft.merchant
        note = draft.note
        selectedTagIds = draft.tagIds
        occurredAt = draft.occurredAt
        formError = null
        calculatorVisible = true
    }

    fun clearForm() {
        val defaultAccountId = preferredAccountId()
        amount = ""
        merchant = ""
        note = ""
        formError = null
        selectedTagIds = emptySet()
        pendingTagNameToSelect = null
        selectedType = entryPreferences.defaultType
        categoryId = null
        occurredAt = if (entryPreferences.useCurrentTime) System.currentTimeMillis() else occurredAt
        accountId = defaultAccountId
        fromAccountId = defaultAccountId
        toAccountId = transferTargetFor(defaultAccountId)
    }

    LaunchedEffect(
        editing?.transaction?.id,
        uiState.activeAccounts.size,
        entryPreferences.defaultType,
        entryPreferences.defaultAccountId,
        entryPreferences.useCurrentTime
    ) {
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
        } else if (amount.isBlank() && accountId == null && fromAccountId == null) {
            clearForm()
        }
    }

    LaunchedEffect(prefillDraft) {
        val draft = prefillDraft ?: return@LaunchedEffect
        applyPrefillDraft(draft)
        onPrefillConsumed()
    }

    LaunchedEffect(uiState.tags, pendingTagNameToSelect) {
        val pendingName = pendingTagNameToSelect ?: return@LaunchedEffect
        val tag = uiState.tags.firstOrNull { it.name == pendingName } ?: return@LaunchedEffect
        selectedTagIds = selectedTagIds + tag.id
        pendingTagNameToSelect = null
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(520)
            cursorVisible = !cursorVisible
        }
    }

    LaunchedEffect(occurredAt) {
        viewModel.setEntryOccurredAt(occurredAt)
    }

    fun startVoiceRecognition() {
        voicePanelVisible = true
        voiceErrorText = null
        voiceResult = null
        if (speechRecognizer == null) {
            isVoiceListening = false
            voiceStatusText = "语音转文字不可用"
            voiceErrorText = "当前设备没有可用的系统语音识别服务"
            return
        }
        if (!hasRecordAudioPermission(context)) {
            isVoiceListening = false
            voiceStatusText = "需要麦克风权限"
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        voiceRawText = ""
        voiceStatusText = "正在启动语音识别..."
        runCatching {
            speechRecognizer.cancel()
            speechRecognizer.startListening(voiceRecognitionIntent())
        }.onFailure { error ->
            isVoiceListening = false
            voiceStatusText = "语音转文字启动失败"
            voiceErrorText = error.localizedMessage?.let { "无法启动系统语音识别：$it" } ?: "无法启动系统语音识别"
        }
    }

    LaunchedEffect(voiceEntryRequestSignal) {
        if (voiceEntryRequestSignal > 0 && editing == null) {
            clearForm()
            voicePanelVisible = true
            startVoiceRecognition()
            onVoiceEntryRequestConsumed()
        }
    }

    LaunchedEffect(shouldStartVoiceAfterPermission) {
        if (shouldStartVoiceAfterPermission) {
            shouldStartVoiceAfterPermission = false
            startVoiceRecognition()
        }
    }

    DisposableEffect(speechRecognizer) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isVoiceListening = true
                voiceStatusText = "正在识别，请说话"
                voiceErrorText = null
            }

            override fun onBeginningOfSpeech() {
                isVoiceListening = true
                voiceStatusText = "正在听..."
            }

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                isVoiceListening = false
                voiceStatusText = "正在整理文字..."
            }

            override fun onError(error: Int) {
                isVoiceListening = false
                val appHasMicPermission = hasRecordAudioPermission(context)
                voiceStatusText = "语音转文字失败"
                voiceErrorText = speechRecognitionErrorMessage(error, appHasMicPermission)
            }

            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                    .trim()
                isVoiceListening = false
                if (text.isBlank()) {
                    voiceStatusText = "未识别到文字"
                    voiceErrorText = "没有听清楚，可以点麦克风再试一次。"
                    return
                }
                val currentUiState = latestUiState
                voiceRawText = text
                val parsed = voiceEntryParser.parseDetailed(text, currentUiState.toVoiceParseContext())
                voiceResult = parsed
                applyPrefillDraft(parsed.draft, currentUiState)
                voiceStatusText = "已识别并填入"
                voiceErrorText = null
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val text = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                    .trim()
                if (text.isNotBlank()) {
                    voiceRawText = text
                    voiceStatusText = "正在转文字..."
                    voiceErrorText = null
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

    val categories = when (selectedType) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }
    val sortedCategories = remember(categories, uiState.recentTransactions, entryPreferences.commonCategoryFirst) {
        if (entryPreferences.commonCategoryFirst) {
            categories.sortedByCommonUsage(uiState.recentTransactions)
        } else {
            categories.sortedWith(compareBy<CategoryEntity> { it.sortOrder }.thenBy { it.createdAt })
        }
    }
    val sortedTags = remember(uiState.tags, uiState.recentTransactions, entryPreferences.tagSuggestionsEnabled) {
        if (entryPreferences.tagSuggestionsEnabled) {
            uiState.tags.sortedTagsByCommonUsage(uiState.recentTransactions)
        } else {
            uiState.tags.sortedWith(compareBy<TagEntity> { it.createdAt }.thenBy { it.name })
        }
    }
    val selectedCategory = categories.firstOrNull { it.id == categoryId }
    val amountColor = transactionColor(selectedType)
    val hasPendingCalculation = hasUnresolvedAmountExpression(amount)
    val pendingCalculationColor = Color(0xFFF59E0B)

    fun currentSnapshot(): EntryFormSnapshot {
        return EntryFormSnapshot(
            type = selectedType,
            amount = amount.trim(),
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId,
            merchant = merchant.trim(),
            note = note.trim(),
            tagIds = selectedTagIds,
            occurredAt = occurredAt
        )
    }

    fun editingSnapshot(transaction: TransactionWithDetails): EntryFormSnapshot {
        return EntryFormSnapshot(
            type = transaction.transaction.type,
            amount = Money(transaction.transaction.amountCents).formatPlain(),
            accountId = transaction.transaction.accountId,
            fromAccountId = transaction.transaction.fromAccountId,
            toAccountId = transaction.transaction.toAccountId,
            categoryId = transaction.transaction.categoryId,
            merchant = transaction.transaction.merchant.trim(),
            note = transaction.transaction.note.trim(),
            tagIds = transaction.tags.map { it.id }.toSet(),
            occurredAt = transaction.transaction.occurredAt
        )
    }

    val hasUnsavedChanges = if (editing != null) {
        currentSnapshot() != editingSnapshot(editing)
    } else {
        amount.isNotBlank() ||
            merchant.isNotBlank() ||
            note.isNotBlank() ||
            categoryId != null ||
            selectedTagIds.isNotEmpty() ||
            selectedType != entryPreferences.defaultType
    }

    fun closeWithoutSaving() {
        showDiscardConfirm = false
        showDeleteConfirm = false
        viewModel.cancelEditTransaction()
        onDone(false)
    }

    fun requestClose() {
        if (isSubmitting) {
            formError = "正在保存，请稍候"
            return
        }
        if (hasUnsavedChanges) {
            showDiscardConfirm = true
        } else {
            closeWithoutSaving()
        }
    }

    BackHandler {
        requestClose()
    }

    var lastHandledDismissRequestSignal by remember { mutableStateOf(dismissRequestSignal) }
    LaunchedEffect(dismissRequestSignal) {
        if (dismissRequestSignal > lastHandledDismissRequestSignal) {
            lastHandledDismissRequestSignal = dismissRequestSignal
            requestClose()
        }
    }

    fun submit(keepOpenAfterSave: Boolean = false) {
        if (isSubmitting) return
        if (isEditingLegacyBalanceAdjustment) {
            formError = "余额校正功能已移除，历史记录仅保留展示"
            return
        }
        val submittedAmount = normalizedAmountInput(amount)
        val validationError = validateEntryDraft(
            type = selectedType,
            amount = submittedAmount,
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId
        )
        if (validationError != null) {
            formError = validationError
            return
        }
        amount = submittedAmount
        formError = null
        isSubmitting = true
        scope.launch {
            val result = if (editing == null) {
                viewModel.addEntryTransaction(
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
            } else {
                viewModel.saveEditedTransactionAwait(
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
            isSubmitting = false
            result
                .onSuccess {
                    val shouldContinue = editing == null && (keepOpenAfterSave || entryPreferences.continueAfterSave)
                    if (shouldContinue) {
                        clearForm()
                    } else {
                        if (editing == null) clearForm()
                        onDone(true)
                    }
                }
                .onFailure { error ->
                    formError = error.message ?: if (editing == null) "记录失败，请稍后再试" else "保存失败，请稍后再试"
                }
        }
    }

    fun confirmAmountOrSubmit() {
        if (hasPendingCalculation) {
            amount = normalizedAmountInput(amount)
            formError = null
        } else {
            submit()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 520.dp, max = 760.dp)
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (entryPreferences.voiceEnabled && editing == null) {
                IconButton(
                    onClick = { startVoiceRecognition() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardVoice,
                        contentDescription = "语音记账",
                        tint = if (isVoiceListening) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(Modifier.size(40.dp))
            }
            CompactAccountPicker(
                label = if (selectedType == TransactionType.TRANSFER) "转出账户" else "账户",
                accounts = uiState.activeAccounts,
                selectedAccountId = if (selectedType == TransactionType.TRANSFER) fromAccountId else accountId,
                onSelected = { selectedId ->
                    if (selectedType == TransactionType.TRANSFER) {
                        fromAccountId = selectedId
                        if (toAccountId == selectedId) {
                            toAccountId = transferTargetFor(selectedId)
                        }
                    } else {
                        accountId = selectedId
                    }
                    formError = null
                },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { requestClose() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (isSubmitting) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
        AnimatedVisibility(
            visible = voicePanelVisible && editing == null,
            enter = fadeIn(animationSpec = tween(durationMillis = 140)),
            exit = fadeOut(animationSpec = tween(durationMillis = 120))
        ) {
            EntryVoiceStatusPanel(
                statusText = voiceStatusText,
                rawText = voiceRawText,
                errorText = voiceErrorText,
                isListening = isVoiceListening,
                result = voiceResult,
                uiState = uiState,
                onRetry = { startVoiceRecognition() },
                onDismiss = {
                    if (isVoiceListening) speechRecognizer?.cancel()
                    isVoiceListening = false
                    voicePanelVisible = false
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(contentScrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, LedgerCardShape)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (isEditingLegacyBalanceAdjustment) {
                    LegacyBalanceAdjustmentNotice()
                } else {
                    TypeSelector(selectedType = selectedType, onTypeSelected = {
                        selectedType = it
                        categoryId = null
                        formError = null
                    })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ledgerPressClickable(onClick = {
                            focusManager.clearFocus()
                            calculatorVisible = true
                        }),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (hasPendingCalculation) {
                            Icon(
                                Icons.Default.Calculate,
                                contentDescription = null,
                                tint = pendingCalculationColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "金额",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (hasPendingCalculation) pendingCalculationColor else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        "¥${amount.ifBlank { "0" }}${if (cursorVisible) "|" else " "}",
                        color = if (hasPendingCalculation) {
                            pendingCalculationColor
                        } else if (selectedType == TransactionType.EXPENSE) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            amountColor
                        },
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
                        editing = editing,
                        occurredAt = occurredAt
                    )
                }

                if (selectedType == TransactionType.TRANSFER) {
                    AccountPickerField(
                        label = "转入账户",
                        accounts = uiState.activeAccounts,
                        selectedAccountId = toAccountId,
                        onSelected = {
                            toAccountId = it
                            formError = null
                        }
                    )
                }
                DateTimeSelector(
                    occurredAt = occurredAt,
                    onChanged = { occurredAt = it }
                )
                TagSelector(
                    tags = sortedTags,
                    selectedTagIds = selectedTagIds,
                    onToggle = { tagId ->
                        selectedTagIds = if (tagId in selectedTagIds) {
                            selectedTagIds - tagId
                        } else {
                            selectedTagIds + tagId
                        }
                    },
                    onAddTag = { name ->
                        val trimmed = name.trim()
                        if (trimmed.isBlank()) return@TagSelector
                        val existingTag = uiState.tags.firstOrNull { it.name == trimmed }
                        if (existingTag != null) {
                            selectedTagIds = selectedTagIds + existingTag.id
                        } else {
                            pendingTagNameToSelect = trimmed
                            viewModel.addTag(trimmed)
                        }
                    }
                )
                MinimalInputLine(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "备注（可选）",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 1,
                    maxLines = 3,
                    onFocusedChange = { focused ->
                        if (focused) calculatorVisible = false
                    }
                )
                if (editing != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .ledgerPressClickable(onClick = { showDeleteConfirm = true }),
                        shape = LedgerCardShape,
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.42f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.22f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "移入回收站",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "可在设置中的回收站恢复或彻底删除",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = calculatorVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 140)),
            exit = fadeOut(animationSpec = tween(durationMillis = 120))
        ) {
            AmountKeypad(
                value = amount,
                onValueChange = {
                    amount = it
                    formError = null
                },
                hasPendingCalculation = hasPendingCalculation,
                onConfirm = { confirmAmountOrSubmit() },
                confirmEnabled = !isSubmitting && !isEditingLegacyBalanceAdjustment,
                onSaveAndContinue = { submit(keepOpenAfterSave = true) },
                saveAndContinueEnabled = editing == null && !isSubmitting && !isEditingLegacyBalanceAdjustment
            )
        }
    }

    if (showDeleteConfirm && editing != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("移入回收站？") },
            text = { Text("这条流水会从首页、统计和资产余额中移除，但仍可在设置的回收站中恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTransaction(editing.transaction.id)
                        viewModel.cancelEditTransaction()
                        showDeleteConfirm = false
                        onDone(false)
                    }
                ) {
                    Text("移入回收站", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("取消")
                }
            }
        )
    }

    if (showDiscardConfirm) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirm = false },
            title = { Text("放弃这次修改？") },
            text = { Text("当前 Sheet 中还有未保存内容，关闭后这些修改不会保留。") },
            confirmButton = {
                TextButton(onClick = ::closeWithoutSaving) {
                    Text("放弃", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardConfirm = false }) {
                    Text("继续编辑")
                }
            }
        )
    }
}

@Composable
private fun LegacyBalanceAdjustmentNotice() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    "历史余额校正",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "该功能已移除，此记录仅保留展示，可关闭或移入回收站。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EntryVoiceStatusPanel(
    statusText: String,
    rawText: String,
    errorText: String?,
    isListening: Boolean,
    result: VoiceEntryParseResult?,
    uiState: EntryUiState,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toneColor = when {
        errorText != null -> MaterialTheme.colorScheme.error
        isListening -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }
    val draft = result?.draft
    val categories = when (draft?.type) {
        TransactionType.INCOME -> uiState.incomeCategories
        TransactionType.EXPENSE -> uiState.expenseCategories
        else -> emptyList()
    }
    val accountName = when (draft?.type) {
        TransactionType.TRANSFER -> {
            val from = uiState.activeAccounts.firstOrNull { it.id == draft.fromAccountId }?.name ?: "转出账户待选"
            val to = uiState.activeAccounts.firstOrNull { it.id == draft.toAccountId }?.name ?: "转入账户待选"
            "$from -> $to"
        }
        null -> ""
        else -> uiState.activeAccounts.firstOrNull { it.id == draft.accountId }?.name ?: "账户待选"
    }
    val categoryName = categories.firstOrNull { it.id == draft?.categoryId }?.name
    val fillSummary = buildList {
        draft?.amount?.takeIf { it.isNotBlank() }?.let { add("金额 $it") }
        accountName.takeIf { it.isNotBlank() }?.let { add(it) }
        categoryName?.let { add(it) }
    }.joinToString(" · ")

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = toneColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, toneColor.copy(alpha = 0.22f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardVoice,
                contentDescription = null,
                tint = toneColor,
                modifier = Modifier.size(20.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    statusText,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "识别原文只用于确认，不会自动保存到备注",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (rawText.isNotBlank()) {
                    Text(
                        "已识别：$rawText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (fillSummary.isNotBlank()) {
                    Text(
                        fillSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = toneColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                errorText?.let { error ->
                    Text(
                        error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            TextButton(onClick = onRetry) {
                Text("重试")
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "收起语音识别",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun EntryActionPill(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
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
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(if (showLabel) 17.dp else 20.dp))
            if (showLabel) {
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
}

@Composable
internal fun MinimalInputLine(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null,
    onFocusedChange: (Boolean) -> Unit = {}
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
                .onFocusChanged {
                    focused = it.isFocused
                    onFocusedChange(it.isFocused)
                },
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

internal fun selectedAccountName(
    accounts: List<AccountEntity>,
    selectedAccountId: Long?
): String {
    return accounts.firstOrNull { it.id == selectedAccountId }?.name ?: "现金"
}

internal fun List<CategoryEntity>.sortedByCommonUsage(
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

internal fun List<TagEntity>.sortedTagsByCommonUsage(
    recentTransactions: List<TransactionWithDetails>
): List<TagEntity> {
    val usageScore = mutableMapOf<Long, Int>()
    recentTransactions.forEachIndexed { index, item ->
        val recencyScore = (recentTransactions.size - index).coerceAtLeast(1)
        item.tags.forEach { tag ->
            usageScore[tag.id] = (usageScore[tag.id] ?: 0) + 100 + recencyScore
        }
    }
    return sortedWith(
        compareByDescending<TagEntity> { usageScore[it.id] ?: 0 }
            .thenBy { it.createdAt }
            .thenBy { it.name }
    )
}

internal fun dateChipLabel(millis: Long): String {
    val date = localDateFromMillis(millis)
    val today = LocalDate.now()
    return when (date) {
        today -> "今天"
        today.minusDays(1) -> "昨天"
        today.minusDays(2) -> "前天"
        else -> date.format(DateTimeFormatter.ofPattern("M月d日", Locale.CHINA))
    }
}

internal fun remindersForDate(
    rules: List<RecurringRuleEntity>,
    date: LocalDate
): List<RecurringRuleEntity> {
    return rules
        .filter { it.isEnabled && localDateFromMillis(it.nextRunAt) == date }
        .sortedBy { it.nextRunAt }
}

internal fun validateEntryDraft(
    type: TransactionType,
    amount: String,
    accountId: Long?,
    fromAccountId: Long?,
    toAccountId: Long?,
    categoryId: Long?
): String? {
    if (hasUnresolvedAmountExpression(amount)) {
        return "请先完成金额计算"
    }
    val cents = Money.parseMajorStrict(amount)?.cents
        ?: return Money.INVALID_AMOUNT_MESSAGE
    if (cents == 0L || (cents < 0 && type != TransactionType.BALANCE_ADJUSTMENT)) {
        return "请输入有效金额"
    }
    return when (type) {
        TransactionType.EXPENSE, TransactionType.INCOME -> when {
            accountId == null -> "请选择账户"
            categoryId == null -> "请选择分类"
            else -> null
        }
        TransactionType.TRANSFER -> when {
            fromAccountId == null -> "请选择转出账户"
            toAccountId == null -> "请选择转入账户"
            fromAccountId == toAccountId -> "转出账户和转入账户不能相同"
            else -> null
        }
        TransactionType.BALANCE_ADJUSTMENT -> if (accountId == null) "请选择账户" else null
    }
}

