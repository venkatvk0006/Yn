package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ThemeEntity
import com.example.ui.MainUiState
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.StatusTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThemeCatalogScreen(
    uiState: MainUiState,
    onSelectThemeDetail: (ThemeEntity) -> Unit,
    onToggleFavorite: (ThemeEntity) -> Unit,
    onApplyTheme: (ThemeEntity) -> Unit,
    onDeleteTheme: (ThemeEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTagFilter by remember { mutableStateOf("All") }

    val filteredThemes = remember(uiState.themes, searchQuery, selectedTagFilter) {
        uiState.themes.filter { theme ->
            val matchesQuery = searchQuery.isBlank() ||
                    theme.name.contains(searchQuery, ignoreCase = true) ||
                    theme.author.contains(searchQuery, ignoreCase = true) ||
                    theme.lockscreenStyle.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedTagFilter) {
                "Favorites" -> theme.isFavorite
                "Custom Created" -> theme.isCustomCreated
                "Depth Lock" -> theme.lockscreenStyle.contains("Depth", ignoreCase = true)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title Header
        item {
            Column {
                StatusTag(text = "MTZ CATALOG", color = Color(0xFF10B981))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "HyperOS Local Themes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Manage, view details, and apply imported or custom created themes.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("catalog_search_input"),
                placeholder = { Text("Search MTZ themes, authors...", color = Color(0xFF64748B)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }

        // Filter Tags
        item {
            val tags = listOf("All", "Favorites", "Custom Created", "Depth Lock")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    val isSelected = tag == selectedTagFilter
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF059669) else Color(0xFF1E293B))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF10B981) else Color(0xFF334155),
                                CircleShape
                            )
                            .clickable { selectedTagFilter = tag }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("tag_filter_$tag")
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Catalog List
        items(filteredThemes, key = { it.id }) { theme ->
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "catalog_item_${theme.id}",
                onClick = { onSelectThemeDetail(theme) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Preview Icon Box
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0F172A), Color(0xFF1E1B4B))
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Palette,
                            contentDescription = null,
                            tint = if (theme.isApplied) Color(0xFF10B981) else Color(0xFF3B82F6),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = theme.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (theme.isApplied) {
                                Spacer(modifier = Modifier.width(6.dp))
                                StatusTag(text = "APPLIED", color = Color(0xFF10B981))
                            }
                        }

                        Text(
                            text = "By ${theme.author} • ${theme.targetOs}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        Text(
                            text = "${theme.lockscreenStyle} • ${theme.iconPackStyle}",
                            fontSize = 10.sp,
                            color = Color(0xFF3B82F6)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onToggleFavorite(theme) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                if (theme.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (theme.isFavorite) Color(0xFFF59E0B) else Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { onDeleteTheme(theme) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onApplyTheme(theme) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("btn_apply_catalog_${theme.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (theme.isApplied) Color(0xFF059669) else Color(0xFF2563EB)
                    )
                ) {
                    Text(
                        text = if (theme.isApplied) "Applied on Device" else "Apply Theme",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
