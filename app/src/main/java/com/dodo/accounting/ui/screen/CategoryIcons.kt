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

internal data class CategoryIconGroup(
    val title: String,
    val options: List<CategoryIconOption>
)

internal data class CategoryIconOption(
    val name: String,
    val label: String,
    val keywords: List<String> = emptyList()
)

internal fun categoryIcon(iconName: String): ImageVector = when (iconName) {
    "restaurant" -> Icons.Default.Restaurant
    "fastfood" -> Icons.Default.Fastfood
    "local_cafe" -> Icons.Default.LocalCafe
    "commute" -> Icons.Default.Commute
    "directions_bus" -> Icons.Default.DirectionsBus
    "train" -> Icons.Default.Train
    "directions_car" -> Icons.Default.DirectionsCar
    "local_gas_station" -> Icons.Default.LocalGasStation
    "shopping_bag" -> Icons.Default.ShoppingBag
    "local_mall" -> Icons.Default.LocalMall
    "local_grocery_store" -> Icons.Default.LocalGroceryStore
    "storefront" -> Icons.Default.Storefront
    "home" -> Icons.Default.Home
    "local_laundry_service" -> Icons.Default.LocalLaundryService
    "devices" -> Icons.Default.Devices
    "phone_iphone" -> Icons.Default.PhoneIphone
    "movie" -> Icons.Default.Movie
    "sports_esports" -> Icons.Default.SportsEsports
    "fitness_center" -> Icons.Default.FitnessCenter
    "flight_takeoff" -> Icons.Default.FlightTakeoff
    "hotel" -> Icons.Default.Hotel
    "school" -> Icons.Default.School
    "menu_book" -> Icons.AutoMirrored.Filled.MenuBook
    "local_hospital" -> Icons.Default.LocalHospital
    "health_and_safety" -> Icons.Default.HealthAndSafety
    "child_care" -> Icons.Default.ChildCare
    "receipt_long" -> Icons.AutoMirrored.Filled.ReceiptLong
    "credit_card" -> Icons.Default.CreditCard
    "payments" -> Icons.Default.Payments
    "wallet" -> Icons.Default.Wallet
    "event_repeat" -> Icons.Default.EventRepeat
    "work" -> Icons.Default.Work
    "business_center" -> Icons.Default.BusinessCenter
    "redeem" -> Icons.Default.Redeem
    "card_giftcard" -> Icons.Default.CardGiftcard
    "add_card" -> Icons.Default.AddCard
    "assessment" -> Icons.Default.Assessment
    "savings" -> Icons.Default.Savings
    "monetization_on" -> Icons.Default.MonetizationOn
    "attach_money" -> Icons.Default.AttachMoney
    "local_shipping" -> Icons.Default.LocalShipping
    "history" -> Icons.Default.History
    else -> Icons.Default.Category
}

internal fun categoryKindLabel(kind: CategoryKind): String = when (kind) {
    CategoryKind.EXPENSE -> "支出分类"
    CategoryKind.INCOME -> "收入分类"
}

internal fun categoryIconOptions(kind: CategoryKind): List<String> {
    return categoryIconGroups(kind)
        .flatMap { it.options }
        .distinctBy { it.name }
        .map { it.name }
}

internal fun categoryIconLabel(iconName: String): String {
    return allCategoryIconOptions()
        .firstOrNull { it.name == iconName }
        ?.label ?: "通用"
}

internal fun categoryIconGroups(kind: CategoryKind): List<CategoryIconGroup> {
    val common = CategoryIconGroup(
        title = "通用",
        options = listOf(
            iconOption("receipt_long", "账单", "账单", "票据", "水电", "缴费"),
            iconOption("credit_card", "卡片", "卡片", "银行卡", "信用卡"),
            iconOption("payments", "现金", "现金", "收款", "付款"),
            iconOption("wallet", "钱包", "钱包", "余额"),
            iconOption("event_repeat", "周期", "周期", "订阅", "固定"),
            iconOption("category", "通用", "其他", "默认")
        )
    )
    val expenseGroups = listOf(
        CategoryIconGroup(
            title = "餐饮日常",
            options = listOf(
                iconOption("restaurant", "餐饮", "吃饭", "饭店", "午饭", "晚饭"),
                iconOption("fastfood", "快餐", "外卖", "零食", "快餐"),
                iconOption("local_cafe", "咖啡", "奶茶", "饮品", "咖啡"),
                iconOption("local_grocery_store", "买菜", "超市", "菜场", "食品")
            )
        ),
        CategoryIconGroup(
            title = "出行",
            options = listOf(
                iconOption("commute", "交通", "通勤", "打车", "出行"),
                iconOption("directions_bus", "公交", "公交", "地铁"),
                iconOption("train", "火车", "高铁", "铁路"),
                iconOption("directions_car", "汽车", "开车", "停车"),
                iconOption("local_gas_station", "加油", "油费", "充电")
            )
        ),
        CategoryIconGroup(
            title = "购物服务",
            options = listOf(
                iconOption("shopping_bag", "购物", "网购", "买东西"),
                iconOption("local_mall", "商场", "服饰", "百货"),
                iconOption("storefront", "门店", "线下", "店铺"),
                iconOption("local_laundry_service", "洗护", "洗衣", "家政"),
                iconOption("devices", "数码", "手机", "电脑", "软件"),
                iconOption("phone_iphone", "手机", "通信", "话费")
            )
        ),
        CategoryIconGroup(
            title = "家庭健康",
            options = listOf(
                iconOption("home", "居家", "房租", "物业", "家庭"),
                iconOption("local_hospital", "医疗", "医院", "药", "看病"),
                iconOption("health_and_safety", "健康", "保险", "体检"),
                iconOption("child_care", "育儿", "孩子", "教育支出")
            )
        ),
        CategoryIconGroup(
            title = "成长娱乐",
            options = listOf(
                iconOption("school", "教育", "课程", "学费"),
                iconOption("menu_book", "书籍", "阅读", "学习"),
                iconOption("movie", "娱乐", "电影", "会员"),
                iconOption("sports_esports", "游戏", "娱乐", "游戏"),
                iconOption("fitness_center", "运动", "健身", "锻炼"),
                iconOption("flight_takeoff", "旅行", "机票", "旅游"),
                iconOption("hotel", "住宿", "酒店", "民宿")
            )
        )
    )
    val incomeGroups = listOf(
        CategoryIconGroup(
            title = "收入",
            options = listOf(
                iconOption("work", "工资", "工资", "薪资", "工作"),
                iconOption("business_center", "副业", "项目", "兼职", "外快"),
                iconOption("redeem", "优惠", "返现", "红包", "赠送"),
                iconOption("card_giftcard", "礼金", "礼物", "奖金"),
                iconOption("add_card", "入账", "到账", "转入"),
                iconOption("assessment", "理财", "基金", "收益"),
                iconOption("savings", "储蓄", "存款", "利息"),
                iconOption("monetization_on", "奖金", "提成", "补贴"),
                iconOption("attach_money", "其他", "其他收入"),
                iconOption("local_shipping", "报销", "物流", "差旅")
            )
        )
    )
    return when (kind) {
        CategoryKind.EXPENSE -> expenseGroups + common
        CategoryKind.INCOME -> incomeGroups + common
    }
}

internal fun allCategoryIconOptions(): List<CategoryIconOption> {
    return (categoryIconGroups(CategoryKind.EXPENSE) + categoryIconGroups(CategoryKind.INCOME))
        .flatMap { it.options }
        .distinctBy { it.name }
}

internal fun iconOption(
    name: String,
    label: String,
    vararg keywords: String
): CategoryIconOption {
    return CategoryIconOption(name = name, label = label, keywords = keywords.toList())
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
