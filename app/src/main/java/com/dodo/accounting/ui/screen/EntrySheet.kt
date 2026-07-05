package com.dodo.accounting.ui.screen

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
import com.dodo.accounting.domain.model.projectedCategoryBudgetCents
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

internal data class EntryPrefillDraft(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val accountId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val categoryId: Long? = null,
    val note: String = "",
    val tagIds: Set<Long> = emptySet(),
    val occurredAt: Long = System.currentTimeMillis()
)

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

private const val AMOUNT_KEY_SAVE_AND_CONTINUE = "再记一笔"

@Composable
internal fun EntrySheetContentV2(
    uiState: AccountingUiState,
    viewModel: AccountingViewModel,
    prefillDraft: EntryPrefillDraft? = null,
    entryPreferences: EntryPreferences = EntryPreferences(),
    dismissRequestSignal: Int = 0,
    onPrefillConsumed: () -> Unit = {},
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
    var isSubmitting by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDiscardConfirm by remember { mutableStateOf(false) }
    var pendingTagNameToSelect by remember { mutableStateOf<String?>(null) }
    var calculatorVisible by remember { mutableStateOf(true) }
    val contentScrollState = rememberScrollState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val scope = rememberCoroutineScope()

    fun activeAccountIdOrNull(id: Long?): Long? {
        return uiState.activeAccounts.firstOrNull { it.id == id }?.id
    }

    fun preferredAccountId(): Long? {
        return activeAccountIdOrNull(entryPreferences.defaultAccountId)
            ?: uiState.activeAccounts.firstOrNull()?.id
    }

    fun transferTargetFor(sourceAccountId: Long?): Long? {
        return uiState.activeAccounts.firstOrNull { it.id != sourceAccountId }?.id
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
        if (editing == null) {
            val defaultAccountId = preferredAccountId()
            val draftAccountId = activeAccountIdOrNull(draft.accountId)
            val draftFromAccountId = activeAccountIdOrNull(draft.fromAccountId) ?: defaultAccountId
            val draftToAccountId = activeAccountIdOrNull(draft.toAccountId)
            selectedType = draft.type
            amount = draft.amount
            accountId = draftAccountId ?: defaultAccountId
            fromAccountId = draftFromAccountId
            toAccountId = draftToAccountId
                ?.takeIf { it != draftFromAccountId }
                ?: transferTargetFor(draftFromAccountId)
            categoryId = draft.categoryId
            merchant = ""
            note = draft.note
            selectedTagIds = draft.tagIds
            occurredAt = draft.occurredAt
            formError = null
        }
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
        onDone()
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
                        onDone()
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
            Spacer(Modifier.size(40.dp))
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
                TypeSelector(selectedType = selectedType, onTypeSelected = {
                    selectedType = it
                    categoryId = null
                    formError = null
                })

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
                confirmEnabled = !isSubmitting,
                onSaveAndContinue = { submit(keepOpenAfterSave = true) },
                saveAndContinueEnabled = editing == null && !isSubmitting
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
                        onDone()
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

private sealed interface EntryCategoryGridItem {
    data class Category(val category: CategoryEntity) : EntryCategoryGridItem
    data object More : EntryCategoryGridItem
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EntryCategoryGrid(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onSelected: (Long) -> Unit
) {
    var showAllCategories by remember { mutableStateOf(false) }
    val hasMore = categories.size > 10
    val visibleItems = buildList {
        val visibleCategories = if (hasMore) categories.take(9) else categories.take(10)
        visibleCategories.forEach { add(EntryCategoryGridItem.Category(it)) }
        if (hasMore) add(EntryCategoryGridItem.More)
    }
    val rows = visibleItems.chunked(5)

    if (showAllCategories) {
        ModalBottomSheet(
            onDismissRequest = { showAllCategories = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "选择分类",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        EntryCategoryTile(
                            category = category,
                            selected = category.id == selectedCategoryId,
                            onClick = {
                                onSelected(category.id)
                                showAllCategories = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { item ->
                    when (item) {
                        is EntryCategoryGridItem.Category -> EntryCategoryTile(
                            category = item.category,
                            selected = item.category.id == selectedCategoryId,
                            onClick = { onSelected(item.category.id) },
                            modifier = Modifier.weight(1f)
                        )
                        EntryCategoryGridItem.More -> EntryMoreCategoryTile(
                            onClick = { showAllCategories = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                repeat(5 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
internal fun EntryCategoryTile(
    category: CategoryEntity,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = Color(category.colorArgb)
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier = modifier
            .height(88.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) LedgerMint else Color.Transparent,
        border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .indication(interactionSource, LocalIndication.current),
                shape = RoundedCornerShape(8.dp),
                color = if (selected) MaterialTheme.colorScheme.surface else tint.copy(alpha = 0.11f),
                border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.62f)) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        categoryIcon(category.iconName),
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.primary else tint,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.height(13.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
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
}

@Composable
internal fun EntryMoreCategoryTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(88.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.64f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.height(13.dp))
            Text(
                "更多...",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
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

@Composable
internal fun CategoryBudgetHint(
    uiState: AccountingUiState,
    category: CategoryEntity,
    amount: String,
    editing: TransactionWithDetails?,
    occurredAt: Long
) {
    val budget = uiState.categoryBudgets.firstOrNull { it.categoryId == category.id }
    val projectedCents = projectedCategoryBudgetCents(
        categoryId = category.id,
        amount = amount,
        occurredAt = occurredAt,
        expenseByCategory = uiState.entryMonthExpenseByCategory,
        editingTransaction = editing?.transaction
    )
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
internal fun DateTimeSelector(
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
            containerColor = MaterialTheme.colorScheme.background
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
internal fun DateTimeWheelSheet(
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

    LedgerCard(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
internal fun WheelPickerColumn(
    values: List<Int>,
    selected: Int,
    label: (Int) -> String,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(values, selected) {
        val selectedIndex = values.indexOf(selected)
        if (selectedIndex >= 0) {
            listState.scrollToItem((selectedIndex - 1).coerceAtLeast(0))
        }
    }

    LazyColumn(
        modifier = modifier.height(188.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(vertical = 30.dp)
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

internal fun dateTimeToMillis(
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
internal fun AmountKeypad(
    value: String,
    onValueChange: (String) -> Unit,
    hasPendingCalculation: Boolean = false,
    onConfirm: () -> Unit = {},
    confirmEnabled: Boolean = true,
    onSaveAndContinue: () -> Unit = {},
    saveAndContinueEnabled: Boolean = true
) {
    val confirmLabel = if (hasPendingCalculation) "=" else "完成"
    val rows = listOf(
        listOf("7", "8", "9", "⌫"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf(".", "0", AMOUNT_KEY_SAVE_AND_CONTINUE, confirmLabel)
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
                        val isSaveAndContinue = key == AMOUNT_KEY_SAVE_AND_CONTINUE
                        val isConfirm = key == confirmLabel
                        AmountKey(
                            label = key,
                            modifier = Modifier.weight(1f),
                            enabled = when {
                                isSaveAndContinue -> saveAndContinueEnabled
                                isConfirm -> confirmEnabled
                                else -> true
                            },
                            confirmColor = if (hasPendingCalculation) Color(0xFFF59E0B) else MaterialTheme.colorScheme.primary,
                            onClick = {
                                when {
                                    isSaveAndContinue -> onSaveAndContinue()
                                    isConfirm -> onConfirm()
                                    else -> onValueChange(handleAmountKey(value, key))
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
internal fun AmountKey(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    confirmColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    val isOperator = label in setOf("+", "-")
    val isConfirm = label == "=" || label == "完成"
    val isSaveAndContinue = label == AMOUNT_KEY_SAVE_AND_CONTINUE
    val haptic = LocalHapticFeedback.current
    val clickWithFeedback = {
        if (enabled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        }
    }
    val keyModifier = if (enabled) {
        Modifier.ledgerPressClickable(onClick = clickWithFeedback)
    } else {
        Modifier
    }
    val containerColor = when {
        isConfirm -> confirmColor
        isSaveAndContinue -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentAlpha = if (enabled) 1f else 0.38f

    Surface(
        modifier = modifier
            .height(58.dp)
            .then(keyModifier),
        color = containerColor.copy(alpha = if (enabled) 1f else 0.52f),
        border = BorderStroke(0.5.dp, LedgerDivider)
    ) {
        Box(contentAlignment = Alignment.Center) {
            when {
                label == "⌫" -> Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "退格",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                    modifier = Modifier.size(24.dp)
                )
                isConfirm -> Text(
                    label,
                    color = Color.White.copy(alpha = contentAlpha),
                    fontWeight = FontWeight.Bold,
                    fontSize = if (label == "=") 28.sp else 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                isSaveAndContinue -> Text(
                    label,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                else -> Text(
                    label,
                    color = (if (isOperator) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface).copy(alpha = contentAlpha),
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompactAccountPicker(
    label: String,
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.id == selectedAccountId }
    val tint = selected?.let { Color(it.colorArgb) } ?: MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(44.dp)
            .clickable(onClick = { expanded = true }),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = RoundedCornerShape(8.dp),
                color = tint.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        selected?.let { accountIcon(it) } ?: Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    selected?.name ?: "请选择",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(4.dp))
            Icon(
                Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${accounts.size} 个账户")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
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
            Spacer(Modifier.height(14.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountPickerField(
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
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${accounts.size} 个账户")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
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
            Spacer(Modifier.height(14.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryPickerField(
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
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LedgerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SelectionSheetHeader(title = label, subtitle = "${categories.size} 个分类")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    contentPadding = PaddingValues(4.dp),
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
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
internal fun PickerField(
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
internal fun SelectionSheetHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun AccountGridItem(
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
internal fun CategoryGridItem(
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
internal fun SelectionGridItem(
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
internal fun TagSelector(
    tags: List<TagEntity>,
    selectedTagIds: Set<Long>,
    onToggle: (Long) -> Unit,
    onAddTag: (String) -> Unit
) {
    var showAddTagSheet by remember { mutableStateOf(false) }

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
            AddTagPill(onClick = { showAddTagSheet = true })
        }
        if (tags.isEmpty()) {
            Text(
                "还没有标签，可以先添加一个固定标签",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showAddTagSheet) {
        AddTagSheet(
            onDismiss = { showAddTagSheet = false },
            onConfirm = { name ->
                onAddTag(name)
                showAddTagSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTagSheet(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tagName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 42.dp, height = 5.dp),
                shape = RoundedCornerShape(999.dp),
                color = LedgerDivider
            ) {}
            Text("添加标签", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            MinimalInputLine(
                value = tagName,
                onValueChange = { tagName = it },
                placeholder = "标签名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LedgerActionButton(
                    label = "取消",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    borderColor = LedgerDivider
                )
                LedgerActionButton(
                    label = "添加并选择",
                    icon = Icons.Default.Add,
                    onClick = { onConfirm(tagName) },
                    modifier = Modifier.weight(1f),
                    enabled = tagName.isNotBlank()
                )
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
internal fun AddTagPill(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, LedgerDivider)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "添加",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
internal fun TagPill(
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
internal fun TypeSelector(
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
