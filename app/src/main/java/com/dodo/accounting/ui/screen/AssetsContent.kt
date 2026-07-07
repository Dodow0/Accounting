package com.dodo.accounting.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dodo.accounting.ui.viewmodel.AccountingUiState

@Composable
internal fun AssetsScreen(
    uiState: AccountingUiState,
    amountsHidden: Boolean,
    onToggleAmountsHidden: () -> Unit,
    onOpenAccountManagement: () -> Unit,
    onCreateTransfer: () -> Unit,
    onOpenAccountFlow: (Long) -> Unit
) {
    val activeAccounts = uiState.accounts.filterNot { it.account.isArchived }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AssetsSummaryCard(
                uiState = uiState,
                amountsHidden = amountsHidden,
                onToggleAmountsHidden = onToggleAmountsHidden
            )
        }
        item {
            AssetsActionRow(
                onOpenAccountManagement = onOpenAccountManagement,
                onCreateTransfer = onCreateTransfer
            )
        }
        item {
            SectionHeader("资产账户", "${activeAccounts.size} 个")
        }
        if (activeAccounts.isEmpty()) {
            item {
                AssetsEmptyPanel(
                    title = "还没有资产账户",
                    subtitle = "先添加现金、银行卡或第三方支付账户，再开始记录流水。",
                    action = "去添加",
                    icon = Icons.Default.AccountBalanceWallet,
                    onClick = onOpenAccountManagement
                )
            }
        } else {
            itemsIndexed(activeAccounts, key = { index, row -> "account-${row.account.id}-$index" }) { _, row ->
                AccountRow(
                    row = row,
                    onClick = { onOpenAccountFlow(row.account.id) },
                    amountsHidden = amountsHidden
                )
            }
        }
    }

}

@Composable
private fun AssetsSummaryCard(
    uiState: AccountingUiState,
    amountsHidden: Boolean,
    onToggleAmountsHidden: () -> Unit
) {
    val activeBalances = uiState.accounts.filterNot { it.account.isArchived }
    val totalAssetCents = activeBalances.sumOf { it.balanceCents.coerceAtLeast(0L) }
    val totalLiabilityCents = activeBalances.sumOf { (-it.balanceCents).coerceAtLeast(0L) }
    val netAssetCents = totalAssetCents - totalLiabilityCents

    LedgerCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        "资产概览",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "净资产",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        privacyAmountLabel(netAssetCents, amountsHidden),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            LedgerIconActionButton(
                icon = if (amountsHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (amountsHidden) "显示金额" else "隐藏金额",
                onClick = onToggleAmountsHidden,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AssetsMetricPill(
                label = "总资产",
                value = privacyAmountLabel(totalAssetCents, amountsHidden),
                icon = Icons.Default.ArrowUpward,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            AssetsMetricPill(
                label = "总负债",
                value = privacyAmountLabel(totalLiabilityCents, amountsHidden),
                icon = Icons.Default.ArrowDownward,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AssetsActionRow(
    onOpenAccountManagement: () -> Unit,
    onCreateTransfer: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LedgerActionButton(
            label = "添加账户",
            icon = Icons.Default.Add,
            onClick = onOpenAccountManagement,
            modifier = Modifier.weight(1f),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            borderColor = LedgerDivider
        )
        LedgerActionButton(
            label = "转账",
            icon = Icons.Default.SwapHoriz,
            onClick = onCreateTransfer,
            modifier = Modifier.weight(1f),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        )
    }
}

@Composable
private fun AssetsMetricPill(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.48f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                value,
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AssetsEmptyPanel(
    title: String,
    subtitle: String,
    action: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    LedgerPanelSurface(onClick = onClick) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                action,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
