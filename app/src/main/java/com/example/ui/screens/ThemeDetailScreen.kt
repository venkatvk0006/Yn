package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ThemeEntity
import com.example.data.model.CustomizerConfig
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.HyperDeviceMockup
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusTag

@Composable
fun ThemeDetailScreen(
    theme: ThemeEntity,
    onBackClick: () -> Unit,
    onApplyTheme: (ThemeEntity) -> Unit,
    onToggleFavorite: (ThemeEntity) -> Unit,
    onProceedToInstaller: () -> Unit
) {
    val simulatedConfig = CustomizerConfig(
        themeName = theme.name,
        lockscreenStyle = theme.lockscreenStyle,
        depthEffectEnabled = theme.lockscreenStyle.contains("Depth", ignoreCase = true),
        iconPackStyle = theme.iconPackStyle,
        fontStyle = theme.fontName,
        controlCenterStyle = theme.controlCenterStyle,
        accentColorHex = theme.accentColorHex
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFF1E293B), CircleShape)
                        .testTag("btn_detail_back")
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = theme.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "By ${theme.author} • ${theme.version}",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                IconButton(
                    onClick = { onToggleFavorite(theme) },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFF1E293B), CircleShape)
                ) {
                    Icon(
                        if (theme.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (theme.isFavorite) Color(0xFFF59E0B) else Color.White
                    )
                }
            }
        }

        // Live Device Mockup
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "detail_mockup_card"
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HyperDeviceMockup(config = simulatedConfig)
                }
            }
        }

        // Detailed Specs
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "detail_specs_card"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Theme Specifications",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        StatusTag(
                            text = if (theme.isApplied) "APPLIED" else theme.targetOs,
                            color = if (theme.isApplied) Color(0xFF10B981) else Color(0xFF3B82F6)
                        )
                    }

                    Text(
                        text = theme.description,
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 18.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF334155))
                    )

                    // Specs List
                    val specs = listOf(
                        "Lockscreen Clock" to theme.lockscreenStyle,
                        "App Icons Set" to theme.iconPackStyle,
                        "Control Center" to theme.controlCenterStyle,
                        "System Typography" to theme.fontName,
                        "Status Bar Style" to theme.statusBarStyle
                    )

                    specs.forEach { (label, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, fontSize = 12.sp, color = Color(0xFF94A3B8))
                            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onProceedToInstaller,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_detail_installer_fix"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Indian ROM Fix", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onApplyTheme(theme) },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("btn_detail_apply_theme"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (theme.isApplied) Color(0xFF059669) else Color(0xFF2563EB)
                            )
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (theme.isApplied) "Applied" else "Apply Theme", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
