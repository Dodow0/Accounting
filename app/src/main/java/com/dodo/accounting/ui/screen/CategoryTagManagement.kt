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
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
import com.dodo.accounting.domain.util.handleAmountKey
import com.dodo.accounting.domain.util.hasUnresolvedAmountExpression
import com.dodo.accounting.domain.util.normalizedAmountInput
import com.dodo.accounting.ui.viewmodel.SettingsUiState
import com.dodo.accounting.ui.viewmodel.SettingsViewModel
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
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun CategoryManagementCard(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    var showCreateCategorySheet by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryEntity?>(null) }
    val categoryItemGap = 6.dp
    val categoryItemGapPx = with(LocalDensity.current) { categoryItemGap.roundToPx() }

    LedgerCard {
            SectionHeader("分类管理", "${uiState.categories.size} 类")
            LedgerActionButton(
                label = "新增分类",
                icon = Icons.Default.Add,
                onClick = { showCreateCategorySheet = true },
                modifier = Modifier.fillMaxWidth()
            )
            CategoryKind.entries.forEach { kind ->
                val categories = uiState.categories.filter { it.kind == kind }
                val reorderState = rememberLongPressReorderState(
                    items = categories,
                    keyOf = { it.id },
                    swapThresholdFraction = 0.58f,
                    itemSpacingPx = categoryItemGapPx,
                    onReordered = { reorderedCategories ->
                        viewModel.reorderCategories(reorderedCategories.map { it.id })
                    }
                )
                if (categories.isEmpty()) return@forEach
                Text(
                    categoryKindLabel(kind),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(categoryItemGap)) {
                    reorderState.items.forEach { category ->
                        key(category.id) {
                            Box(
                                modifier = Modifier.longPressReorderItem(
                                    state = reorderState,
                                    item = category
                                )
                            ) {
                                CategoryManagementRow(
                                    category = category,
                                    onEdit = { categoryToEdit = category }
                                )
                            }
                        }
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
            },
            onDelete = {
                viewModel.deleteCategory(category.id)
                categoryToEdit = null
            }
        )
    }

    if (showCreateCategorySheet) {
        CategoryCreateSheet(
            onDismiss = { showCreateCategorySheet = false },
            onSave = { name, kind, iconName, colorArgb ->
                viewModel.addCategory(name, kind, iconName, colorArgb)
                showCreateCategorySheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryCreateSheet(
    onDismiss: () -> Unit,
    onSave: (String, CategoryKind, String, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var kind by remember { mutableStateOf(CategoryKind.EXPENSE) }
    var iconName by remember { mutableStateOf("receipt_long") }
    var colorArgb by remember { mutableStateOf(0xFFEA580C) }

    fun selectKind(nextKind: CategoryKind) {
        if (kind == nextKind) return
        kind = nextKind
        iconName = if (nextKind == CategoryKind.EXPENSE) "receipt_long" else "work"
        colorArgb = if (nextKind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                Text("新增分类", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = { onSave(name.trim(), kind, iconName, colorArgb) },
                    enabled = name.isNotBlank()
                ) {
                    Text("保存", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            MinimalInputLine(
                value = name,
                onValueChange = { name = it },
                placeholder = "分类名称",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text("类型", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(CategoryKind.EXPENSE, CategoryKind.INCOME).forEach { option ->
                    LedgerChoiceChip(
                        selected = kind == option,
                        label = categoryKindLabel(option),
                        onClick = { selectKind(option) }
                    )
                }
            }
            CategoryIconPicker(
                kind = kind,
                selectedIconName = iconName,
                tint = Color(colorArgb),
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
internal fun CategoryManagementRow(
    category: CategoryEntity,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ledgerPressClickable(onClick = onEdit),
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
        Text(
            category.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "编辑分类")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryEditDialog(
    category: CategoryEntity,
    onDismiss: () -> Unit,
    onSave: (String, String, Long) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember(category.id) { mutableStateOf(category.name) }
    var iconName by remember(category.id) { mutableStateOf(category.iconName) }
    var colorArgb by remember(category.id) { mutableStateOf(category.colorArgb) }
    var confirmDelete by remember(category.id) { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LedgerCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
            CategoryIconPicker(
                kind = category.kind,
                selectedIconName = iconName,
                tint = Color(colorArgb),
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
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            LedgerActionButton(
                label = if (confirmDelete) "确认删除" else "删除分类",
                icon = Icons.Default.Delete,
                onClick = {
                    if (confirmDelete) {
                        onDelete()
                    } else {
                        confirmDelete = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.26f)
            )
            Spacer(Modifier.height(14.dp))
        }
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun CategoryIconPicker(
    kind: CategoryKind,
    selectedIconName: String,
    tint: Color,
    onSelected: (String) -> Unit
) {
    val groups = remember(kind) { categoryIconGroups(kind) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("图标", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        groups.forEach { group ->
            if (group.options.isNotEmpty()) {
                Text(
                    group.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    group.options.forEach { option ->
                        CategoryEditPill(
                            selected = selectedIconName == option.name,
                            label = option.label,
                            icon = categoryIcon(option.name),
                            tint = tint,
                            onClick = { onSelected(option.name) }
                        )
                    }
                }
            }
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
            .ledgerPressClickable(onClick = onClick),
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
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    var newTagName by remember { mutableStateOf("") }
    var tagToRename by remember { mutableStateOf<TagEntity?>(null) }
    val reorderState = rememberLongPressReorderState(
        items = uiState.tags,
        keyOf = { it.id },
        onReordered = { reorderedTags ->
            viewModel.reorderTags(reorderedTags.map { it.id })
        }
    )

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
                LedgerIconActionButton(
                    icon = Icons.Default.Add,
                    contentDescription = "新增标签",
                    onClick = {
                        if (newTagName.isNotBlank()) {
                            viewModel.addTag(newTagName)
                            newTagName = ""
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reorderState.items.forEach { tag ->
                    Box(
                        modifier = Modifier.longPressReorderItem(
                            state = reorderState,
                            item = tag,
                            axis = ReorderDragAxis.Horizontal
                        )
                    ) {
                        TagPill(
                            tag = tag,
                            onRename = { tagToRename = tag },
                            onDelete = { viewModel.deleteTag(tag.id) }
                        )
                    }
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
                    LedgerActionButton(
                        label = "取消",
                        onClick = { tagToRename = null },
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        borderColor = LedgerDivider
                    )
                    LedgerActionButton(
                        label = "保存",
                        onClick = {
                            tagToRename?.let { viewModel.renameTag(it.id, renamedName) }
                            tagToRename = null
                        },
                        modifier = Modifier.weight(1f)
                    )
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
internal fun TagPill(
    tag: TagEntity,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(38.dp)
            .ledgerPressClickable(onClick = onRename),
        shape = RoundedCornerShape(19.dp),
        color = LedgerMint,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                tag.name,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                Icons.Default.Delete,
                contentDescription = "删除标签",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onDelete)
            )
        }
    }
}
