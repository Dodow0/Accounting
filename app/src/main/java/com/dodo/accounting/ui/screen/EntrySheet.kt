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
internal fun EntrySheetContentV2(
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
    val contentScrollState = rememberScrollState()

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

    LaunchedEffect(detailsExpanded) {
        if (detailsExpanded) {
            delay(120)
            contentScrollState.animateScrollTo(contentScrollState.maxValue)
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
    val hasPendingCalculation = hasUnresolvedAmountExpression(amount)
    val pendingCalculationColor = Color(0xFFF59E0B)

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
            .heightIn(min = 520.dp, max = 760.dp)
            .navigationBarsPadding()
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
                    Text(
                        "完成",
                        color = if (hasPendingCalculation) pendingCalculationColor else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(contentScrollState)
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

                AnimatedVisibility(visible = detailsExpanded) {
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
            hasPendingCalculation = hasPendingCalculation,
            onConfirm = ::submit
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
        ModalBottomSheet(onDismissRequest = { showAllCategories = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
                    contentPadding = PaddingValues(bottom = 8.dp),
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
internal fun MinimalInputLine(
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

@Composable
internal fun CategoryBudgetHint(
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
                            confirmColor = if (hasPendingCalculation) Color(0xFFF59E0B) else MaterialTheme.colorScheme.primary,
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
internal fun AmountKey(
    label: String,
    modifier: Modifier = Modifier,
    confirmColor: Color = MaterialTheme.colorScheme.primary,
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
            .height(58.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = clickWithFeedback
            ),
        color = if (isConfirm) confirmColor else MaterialTheme.colorScheme.surfaceVariant,
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
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
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
