package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.ThemeEntity
import com.example.ui.MainUiState
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusTag

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onNavigateToImport: () -> Unit,
    onNavigateToInstaller: () -> Unit,
    onNavigateToCustomizer: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onSelectThemeDetail: (ThemeEntity) -> Unit,
    onToggleFavorite: (ThemeEntity) -> Unit,
    onApplyTheme: (ThemeEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                // Background Banner Image
                Image(
                    painter = painterResource(id = R.drawable.img_hyperos_banner_1785113329941),
                    contentDescription = "HyperOS Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color(0xFF0B0F19).copy(alpha = 0.85f),
                                    Color(0xFF0B0F19)
                                )
                            )
                        )
                )

                // Banner Text Content
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    StatusTag(
                        text = "HYPEROS 1.0 • INDIAN ROM",
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Text(
                        text = "Venkat Theme Studio",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "Seamless third-party theme installation & customization for Xiaomi devices",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Quick Feature Cards Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Import MTZ",
                        subtitle = "Parse & Install",
                        icon = Icons.Default.FolderOpen,
                        accentColor = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f),
                        testTag = "home_btn_import",
                        onClick = onNavigateToImport
                    )

                    QuickActionCard(
                        title = "Indian ROM Fix",
                        subtitle = "Bypass Auth 402",
                        icon = Icons.Default.PhonelinkSetup,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        testTag = "home_btn_installer",
                        onClick = onNavigateToInstaller
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Mix & Match",
                        subtitle = "Depth Clock & Icons",
                        icon = Icons.Default.Extension,
                        accentColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        testTag = "home_btn_customizer",
                        onClick = onNavigateToCustomizer
                    )

                    QuickActionCard(
                        title = "Theme Store",
                        subtitle = "Curated Presets",
                        icon = Icons.Default.Palette,
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f),
                        testTag = "home_btn_catalog",
                        onClick = onNavigateToCatalog
                    )
                }
            }
        }

        // Currently Active Theme Banner
        item {
            val active = uiState.activeTheme
            if (active != null) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    SectionHeader(
                        title = "Active Theme Engine",
                        subtitle = "Currently applied on device",
                        icon = Icons.Default.CheckCircle
                    )

                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "active_theme_card",
                        onClick = { onSelectThemeDetail(active) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                                        ),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = active.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    StatusTag(text = "APPLIED", color = Color(0xFF10B981))
                                }

                                Text(
                                    text = "By ${active.author} • ${active.version}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )

                                Text(
                                    text = "${active.lockscreenStyle} • ${active.iconPackStyle}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF3B82F6),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }

        // Recent / Favorite Themes Horizontal Carousel
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                PaddingValues(horizontal = 16.dp).let {
                    Box(modifier = Modifier.padding(it)) {
                        SectionHeader(
                            title = "Local MTZ Themes",
                            subtitle = "Installed & saved packages",
                            icon = Icons.Default.ColorLens,
                            actionText = "See All",
                            onActionClick = onNavigateToCatalog
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.themes, key = { it.id }) { theme ->
                        ThemeItemCard(
                            theme = theme,
                            onCardClick = { onSelectThemeDetail(theme) },
                            onToggleFav = { onToggleFavorite(theme) },
                            onApply = { onApplyTheme(theme) }
                        )
                    }
                }
            }
        }

        // Indian ROM Patcher Helper Summary Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1E1B4B),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4338CA))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.PhonelinkSetup,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Fix 3rd-Party Theme Error",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Getting 'Themes from third-party sources are not supported (402)' on your HyperOS Indian ROM? Use our 4-step installer wizard to bypass Theme Manager checks.",
                            fontSize = 12.sp,
                            color = Color(0xFFC7D2FE)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onNavigateToInstaller,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_btn_launch_wizard")
                        ) {
                            Text(
                                "Launch Indian ROM Installer Wizard",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag(testTag)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(accentColor.copy(alpha = 0.5f), Color(0xFF334155))
                ),
                RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
private fun ThemeItemCard(
    theme: ThemeEntity,
    onCardClick: () -> Unit,
    onToggleFav: () -> Unit,
    onApply: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(210.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onCardClick() }
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF1E293B)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Card Preview Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1B4B)
                            )
                        )
                    )
                    .padding(10.dp)
            ) {
                // Top Tag
                StatusTag(
                    text = if (theme.isApplied) "ACTIVE" else "MTZ",
                    color = if (theme.isApplied) Color(0xFF10B981) else Color(0xFF3B82F6),
                    modifier = Modifier.align(Alignment.TopStart)
                )

                // Favorite Icon
                IconButton(
                    onClick = onToggleFav,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        if (theme.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (theme.isFavorite) Color(0xFFF59E0B) else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Clock Preview
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "10:08",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        theme.lockscreenStyle,
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = theme.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )

            Text(
                text = theme.targetOs,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (theme.isApplied) Color(0xFF059669) else Color(0xFF2563EB)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (theme.isApplied) "Applied" else "Apply Theme",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
