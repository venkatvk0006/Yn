package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig
import com.example.ui.MainUiState
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.HyperDeviceMockup
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MixAndMatchCustomizerScreen(
    uiState: MainUiState,
    onUpdateConfig: ((CustomizerConfig) -> CustomizerConfig) -> Unit,
    onSaveCustomizedTheme: () -> Unit
) {
    val config = uiState.customizerConfig

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        item {
            Column {
                StatusTag(text = "HYPEROS THEME STUDIO", color = Color(0xFF8B5CF6))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mix & Match Theme Engine",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Combine Depth Clocks, Icon Sets, Control Center Blurs, and Fonts into a synthesized MTZ package.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Live Device Mockup Preview
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "device_mockup_card"
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "LIVE INTERACTIVE PREVIEW",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5CF6)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    HyperDeviceMockup(config = config)
                }
            }
        }

        // Theme Title Input
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Theme Name", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    OutlinedTextField(
                        value = config.themeName,
                        onValueChange = { newName -> onUpdateConfig { it.copy(themeName = newName) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_theme_name"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Lockscreen Style Options
        item {
            CustomizerCategoryCard(
                title = "Lockscreen Clock Style",
                icon = Icons.Default.LockClock,
                testTag = "lockscreen_category"
            ) {
                // Depth Effect Toggle Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("HyperOS Depth Effect Engine", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Renders clock digits behind wallpaper subjects", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }
                    Switch(
                        checked = config.depthEffectEnabled,
                        onCheckedChange = { enabled -> onUpdateConfig { it.copy(depthEffectEnabled = enabled) } },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF8B5CF6)
                        ),
                        modifier = Modifier.testTag("switch_depth_effect")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val lockscreens = listOf(
                    "Hyper Depth Clock 18",
                    "Glass Widget Clock",
                    "Material You Double Line",
                    "Cyberpunk Matrix Glitch",
                    "Super Geometry Lockscreen"
                )

                OptionChipGrid(
                    options = lockscreens,
                    selectedOption = config.lockscreenStyle,
                    onSelect = { selected -> onUpdateConfig { it.copy(lockscreenStyle = selected) } }
                )
            }
        }

        // Icon Pack Style Options
        item {
            CustomizerCategoryCard(
                title = "App Icon Set",
                icon = Icons.Default.Widgets,
                testTag = "iconpack_category"
            ) {
                val iconPacks = listOf(
                    "HyperOS Dynamic Squircles",
                    "Glassmorphic 3D",
                    "Stock Android Round",
                    "Neumorphic Cyber Red"
                )

                OptionChipGrid(
                    options = iconPacks,
                    selectedOption = config.iconPackStyle,
                    onSelect = { selected -> onUpdateConfig { it.copy(iconPackStyle = selected) } }
                )
            }
        }

        // Control Center & Volume Bar Options
        item {
            CustomizerCategoryCard(
                title = "Control Center Blur",
                icon = Icons.Default.PhonelinkSetup,
                testTag = "controlcenter_category"
            ) {
                val ccStyles = listOf(
                    "Translucent Glass Blur",
                    "iOS Control Center",
                    "Dark Cyber Tiles",
                    "Material Quick Settings Tiles"
                )

                OptionChipGrid(
                    options = ccStyles,
                    selectedOption = config.controlCenterStyle,
                    onSelect = { selected -> onUpdateConfig { it.copy(controlCenterStyle = selected) } }
                )
            }
        }

        // Fonts Options
        item {
            CustomizerCategoryCard(
                title = "System Fonts",
                icon = Icons.Default.FontDownload,
                testTag = "font_category"
            ) {
                val fonts = listOf(
                    "San Francisco Pro",
                    "Google Sans",
                    "Inter Display",
                    "JetBrains Mono Bold",
                    "HyperSans Regular"
                )

                OptionChipGrid(
                    options = fonts,
                    selectedOption = config.fontStyle,
                    onSelect = { selected -> onUpdateConfig { it.copy(fontStyle = selected) } }
                )
            }
        }

        // Accent Color Palette
        item {
            CustomizerCategoryCard(
                title = "Theme Accent Color",
                icon = Icons.Default.ColorLens,
                testTag = "color_category"
            ) {
                val colors = listOf(
                    "#3B82F6" to "Electric Blue",
                    "#F59E0B" to "Hyper Amber",
                    "#06B6D4" to "Cyan Neon",
                    "#EF4444" to "Cyber Red",
                    "#10B981" to "Emerald Green",
                    "#8B5CF6" to "Vibrant Purple"
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    colors.forEach { (hex, name) ->
                        val colorObj = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Blue }
                        val isSelected = config.accentColorHex.equals(hex, ignoreCase = true)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorObj.copy(alpha = if (isSelected) 1f else 0.25f))
                                .border(
                                    2.dp,
                                    if (isSelected) Color.White else colorObj,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onUpdateConfig { it.copy(accentColorHex = hex) } }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("color_chip_$hex"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else colorObj
                                )
                            }
                        }
                    }
                }
            }
        }

        // Compile & Save Action
        item {
            Button(
                onClick = onSaveCustomizedTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_save_customized_theme"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compile & Export MTZ Package", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CustomizerCategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    testTag: String,
    content: @Composable () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        testTag = testTag
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color(0xFF8B5CF6).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OptionChipGrid(
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(option) }
                    .border(
                        1.dp,
                        if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color(0xFF5B21B6) else Color(0xFF0F172A)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = option,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}
