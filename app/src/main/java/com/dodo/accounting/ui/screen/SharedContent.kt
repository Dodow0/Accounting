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


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TransactionRow(
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
internal fun TransactionActionSheet(
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
internal fun CategoryManagementCard(
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
internal fun CategoryManagementRow(
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
internal fun CategoryEditDialog(
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
internal fun CategoryEditPill(
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
internal fun TagManagementCard(
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
internal fun SummaryMetric(
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
internal fun ExpenseDonutChart(
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
internal fun CategorySummaryRow(
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
internal fun MonthlyTrendBarChart(
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
internal fun TrendLegend(
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
internal fun SectionHeader(
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
internal fun InfoCard(text: String) {
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

internal fun accountIcon(account: AccountEntity): ImageVector = when (account.iconName) {
    "payments" -> Icons.Default.Payments
    "credit_card" -> Icons.Default.CreditCard
    "chat" -> Icons.AutoMirrored.Filled.Chat
    "account_balance_wallet" -> Icons.Default.AccountBalanceWallet
    "phone_iphone" -> Icons.Default.PhoneIphone
    "directions_bus" -> Icons.Default.DirectionsBus
    "storefront" -> Icons.Default.Storefront
    else -> accountIcon(account.type)
}

internal fun accountIcon(type: AccountType): ImageVector = when (type) {
    AccountType.CASH -> Icons.Default.Wallet
    AccountType.BANK_CARD, AccountType.CREDIT -> Icons.Default.CreditCard
    AccountType.THIRD_PARTY_PAYMENT -> Icons.Default.AccountBalanceWallet
    AccountType.STORED_VALUE_CARD -> Icons.Default.Wallet
    AccountType.TRANSIT_CARD -> Icons.Default.DirectionsBus
    AccountType.DIGITAL_BALANCE -> Icons.Default.AccountBalanceWallet
    AccountType.CUSTOM -> Icons.Default.Wallet
}

internal fun categoryIcon(iconName: String): ImageVector = when (iconName) {
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

internal fun accountTypeLabel(type: AccountType): String = when (type) {
    AccountType.CASH -> "现金"
    AccountType.BANK_CARD -> "银行卡"
    AccountType.THIRD_PARTY_PAYMENT -> "第三方支付"
    AccountType.STORED_VALUE_CARD -> "储值卡"
    AccountType.TRANSIT_CARD -> "公交卡"
    AccountType.DIGITAL_BALANCE -> "数字余额"
    AccountType.CREDIT -> "信用账户"
    AccountType.CUSTOM -> "自定义"
}

internal fun categoryKindLabel(kind: CategoryKind): String = when (kind) {
    CategoryKind.EXPENSE -> "支出分类"
    CategoryKind.INCOME -> "收入分类"
}

internal fun categoryIconOptions(kind: CategoryKind): List<String> = when (kind) {
    CategoryKind.EXPENSE -> listOf("restaurant", "commute", "shopping_bag", "devices", "receipt_long", "category")
    CategoryKind.INCOME -> listOf("work", "redeem", "add_card", "category")
}

internal fun categoryIconLabel(iconName: String): String = when (iconName) {
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

internal fun categoryColorOptions(): List<Long> = listOf(
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

internal fun transactionLabel(type: TransactionType): String = when (type) {
    TransactionType.EXPENSE -> "支出"
    TransactionType.INCOME -> "收入"
    TransactionType.TRANSFER -> "转账"
    TransactionType.BALANCE_ADJUSTMENT -> "校正"
}

internal fun transactionIcon(type: TransactionType): ImageVector = when (type) {
    TransactionType.EXPENSE -> Icons.Default.ArrowUpward
    TransactionType.INCOME -> Icons.Default.ArrowDownward
    TransactionType.TRANSFER -> Icons.Default.SwapHoriz
    TransactionType.BALANCE_ADJUSTMENT -> Icons.Default.AccountBalanceWallet
}

@Composable
internal fun transactionColor(type: TransactionType): Color = when (type) {
    TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface
    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
    TransactionType.TRANSFER -> MaterialTheme.colorScheme.primary
    TransactionType.BALANCE_ADJUSTMENT -> MaterialTheme.colorScheme.tertiary
}

internal fun transactionTitle(item: TransactionWithDetails): String {
    val transaction = item.transaction
    return when (transaction.type) {
        TransactionType.TRANSFER -> "${item.fromAccount?.name ?: "未知账户"} -> ${item.toAccount?.name ?: "未知账户"}"
        TransactionType.BALANCE_ADJUSTMENT -> "${item.account?.name ?: "未知账户"} 余额校正"
        else -> transaction.merchant.ifBlank { item.category?.name ?: transactionLabel(transaction.type) }
    }
}

internal fun transactionSubtitle(item: TransactionWithDetails): String {
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

internal fun amountLabel(type: TransactionType, cents: Long): String {
    val prefix = when (type) {
        TransactionType.EXPENSE -> "-"
        TransactionType.INCOME -> "+"
        TransactionType.TRANSFER -> ""
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+" else ""
    }
    return prefix + Money(cents).format()
}

internal fun plainAmountLabel(type: TransactionType, cents: Long): String {
    val plain = Money(cents).formatPlain()
    return when (type) {
        TransactionType.EXPENSE -> "-${plain.removePrefix("-")}"
        TransactionType.INCOME -> "+${plain.removePrefix("-")}"
        TransactionType.TRANSFER -> plain
        TransactionType.BALANCE_ADJUSTMENT -> if (cents >= 0) "+$plain" else plain
    }
}

internal fun dateLabel(millis: Long): String {
    return SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(millis))
}

internal data class TransactionDayGroup(
    val date: LocalDate,
    val items: List<TransactionWithDetails>,
    val expenseCents: Long,
    val incomeCents: Long
)

internal data class MonthlyTrend(
    val month: LocalDate,
    val expenseCents: Long,
    val incomeCents: Long
)

internal data class CalendarDay(
    val date: LocalDate,
    val inMonth: Boolean
)

internal fun buildMonthlyTrends(transactions: List<TransactionWithDetails>): List<MonthlyTrend> {
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

internal fun List<TransactionWithDetails>.groupedByDay(): List<TransactionDayGroup> {
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

internal fun weekdayLabel(date: LocalDate): String = when (date.dayOfWeek.value) {
    1 -> "星期一"
    2 -> "星期二"
    3 -> "星期三"
    4 -> "星期四"
    5 -> "星期五"
    6 -> "星期六"
    else -> "星期日"
}

internal fun calendarMonthCells(monthStart: LocalDate): List<CalendarDay> {
    val firstDay = monthStart.withDayOfMonth(1)
    val startOffset = firstDay.dayOfWeek.value - 1
    val gridStart = firstDay.minusDays(startOffset.toLong())
    return (0 until 42).map { offset ->
        val date = gridStart.plusDays(offset.toLong())
        CalendarDay(date = date, inMonth = date.monthValue == monthStart.monthValue)
    }
}

internal fun localDateFromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

internal fun localDateStartMillis(date: LocalDate): Long {
    return date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

internal fun isInCurrentMonth(millis: Long): Boolean {
    val date = localDateFromMillis(millis)
    val current = LocalDate.now(ZoneId.systemDefault())
    return date.year == current.year && date.monthValue == current.monthValue
}

internal fun categoryPercent(cents: Long, totalCents: Long): Int {
    if (totalCents <= 0) return 0
    return ((cents.toDouble() / totalCents.toDouble()) * 100).toInt()
}

@Composable
internal fun donutPalette(): List<Color> = listOf(
    MaterialTheme.colorScheme.primary,
    MaterialTheme.colorScheme.secondary,
    MaterialTheme.colorScheme.tertiary,
    MaterialTheme.colorScheme.onSurfaceVariant,
    MaterialTheme.colorScheme.primary.copy(alpha = 0.68f),
    MaterialTheme.colorScheme.secondary.copy(alpha = 0.68f)
)
