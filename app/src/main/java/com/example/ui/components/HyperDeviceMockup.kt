package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig

@Composable
fun HyperDeviceMockup(
    config: CustomizerConfig,
    modifier: Modifier = Modifier,
    initialTab: Int = 0 // 0: Lockscreen, 1: Homescreen, 2: Control Center
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val accentColor = remember(config.accentColorHex) {
        try {
            Color(android.graphics.Color.parseColor(config.accentColorHex))
        } catch (e: Exception) {
            Color(0xFF3B82F6)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Selector Pills
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .background(Color(0xFF1E293B), RoundedCornerShape(20.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Lockscreen", "Homescreen", "Control Center").forEachIndexed { idx, label ->
                val isSelected = selectedTab == idx
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) accentColor else Color.Transparent)
                        .clickable { selectedTab = idx }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("mockup_tab_$idx")
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Phone Frame
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(500.dp)
                .shadow(16.dp, RoundedCornerShape(36.dp), spotColor = accentColor)
                .background(Color(0xFF0F172A), RoundedCornerShape(36.dp))
                .border(3.dp, Color(0xFF334155), RoundedCornerShape(36.dp))
                .padding(6.dp)
        ) {
            // Screen Inner Canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1B4B),
                                Color(0xFF0284C7).copy(alpha = 0.3f)
                            )
                        )
                    )
            ) {
                // Top Punchhole Notch / Dynamic Island
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .width(if (config.statusBarStyle.contains("Island")) 90.dp else 24.dp)
                        .height(18.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (config.statusBarStyle.contains("Island")) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                "HyperOS 1.0",
                                fontSize = 8.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Top Status Bar (Time & Icons)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "09:41",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Wifi,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Icon(
                            Icons.Default.SignalCellular4Bar,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height(9.dp)
                                .border(1.dp, Color.White, RoundedCornerShape(2.dp))
                                .padding(1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.85f)
                                    .background(accentColor, RoundedCornerShape(1.dp))
                            )
                        }
                    }
                }

                // Animated Main Screen Content
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "mockup_transition",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 28.dp)
                ) { tab ->
                    when (tab) {
                        0 -> MockupLockscreen(config = config, accentColor = accentColor)
                        1 -> MockupHomescreen(config = config, accentColor = accentColor)
                        2 -> MockupControlCenter(config = config, accentColor = accentColor)
                    }
                }

                // Bottom Home Bar Indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                        .width(90.dp)
                        .height(3.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun MockupLockscreen(config: CustomizerConfig, accentColor: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Depth Clock Effect
            Box(contentAlignment = Alignment.Center) {
                if (config.depthEffectEnabled) {
                    Text(
                        text = "09:41",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor.copy(alpha = 0.35f),
                        letterSpacing = (-1).sp,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.offset(y = 2.dp)
                    )
                }
                Text(
                    text = "09:41",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-1).sp,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Text(
                text = "Sunday, July 26",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lockscreen Glass Widgets
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("28°C", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Sunny • IN", fontSize = 9.sp, color = Color.LightGray)
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("88%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    Text("Hyper Charge", fontSize = 9.sp, color = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Shortcuts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FlashlightOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MockupHomescreen(config: CustomizerConfig, accentColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hyper Widget Banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color.White.copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "HYPER ENGINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        "Theme Customizer",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1.0", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of Apps
        val apps = listOf(
            "Phone" to Icons.Default.Phone,
            "Messages" to Icons.Default.Chat,
            "Camera" to Icons.Default.CameraAlt,
            "Settings" to Icons.Default.Settings,
            "Music" to Icons.Default.MusicNote,
            "Bluetooth" to Icons.Default.Bluetooth,
            "Flash" to Icons.Default.FlashlightOn,
            "WiFi" to Icons.Default.Wifi
        )

        val shape = when {
            config.iconPackStyle.contains("Squircle") -> RoundedCornerShape(12.dp)
            config.iconPackStyle.contains("Round") -> CircleShape
            else -> RoundedCornerShape(8.dp)
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (row in apps.chunked(4)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for ((appName, icon) in row) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .shadow(4.dp, shape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(accentColor, accentColor.copy(alpha = 0.7f))
                                        ),
                                        shape
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), shape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                appName,
                                fontSize = 8.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MockupControlCenter(config: CustomizerConfig, accentColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Big Toggle Cards (Wifi / Bluetooth)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                color = accentColor,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Wifi,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Wi-Fi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Connected", fontSize = 8.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Bluetooth,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Bluetooth", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Off", fontSize = 8.sp, color = Color.LightGray)
                    }
                }
            }
        }

        // Sliders
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.12f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.7f)
                            .background(accentColor, CircleShape)
                    )
                }
            }
        }

        // Quick Toggles Grid
        val toggles = listOf(
            "Flashlight" to Icons.Default.FlashlightOn,
            "Mute" to Icons.Default.VolumeUp,
            "Music" to Icons.Default.MusicNote,
            "Settings" to Icons.Default.Settings
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            toggles.forEach { (title, icon) ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
