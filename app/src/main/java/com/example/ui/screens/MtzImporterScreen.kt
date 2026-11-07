package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MtzPackage
import com.example.ui.MainUiState
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusTag
import com.example.ui.components.StoragePermissionCard
import java.io.File

@Composable
fun MtzImporterScreen(
    uiState: MainUiState,
    onImportMtzSelected: (fileName: String, sizeMb: Double) -> Unit,
    onImportMtzUriSelected: (Uri) -> Unit,
    onSaveImportedTheme: () -> Unit,
    onProceedToInstallerWizard: () -> Unit
) {
    var selectedSampleFile by remember { mutableStateOf("hyperos_ios18_depth_ultra.mtz") }

    // Launcher for Android System File Picker
    val mtzFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onImportMtzUriSelected(uri)
        }
    }

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
                StatusTag(text = "MTZ ARCHIVE ENGINE", color = Color(0xFF3B82F6))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Import & Analyze MTZ Theme",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Select an `.mtz` file from storage or a sample package to extract and verify HyperOS components.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Storage Access Permission Card
        item {
            StoragePermissionCard()
        }

        // System Storage Picker Card
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "mtz_file_picker_card"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFF3B82F6).copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Local Device Storage Picker",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Supported formats: .mtz, .zip archives",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    // Main Browse Button
                    Button(
                        onClick = { mtzFilePickerLauncher.launch("*/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_browse_mtz_file"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        if (uiState.isImportingMtz) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Unpacking & Extracting MTZ...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Browse Storage for .mtz File", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Divider line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF334155))
                    )

                    // Sample File Quick Selectors
                    Text(
                        "Or analyze sample MTZ theme archives:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.LightGray
                    )

                    listOf(
                        "hyperos_ios18_depth_ultra.mtz" to "24.5 MB • Depth Lock + iOS CC",
                        "glassmorphic_hyper_v2.mtz" to "18.2 MB • Frosted Glass UI",
                        "pixel_experience_hyper.mtz" to "15.8 MB • Stock Android 15"
                    ).forEach { (fname, desc) ->
                        val isSelected = selectedSampleFile == fname
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedSampleFile = fname }
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF3B82F6) else Color(0xFF334155),
                                    RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF1E3A8A).copy(alpha = 0.4f) else Color(0xFF0F172A)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (isSelected) Icons.Default.CheckCircle else Icons.Default.Description,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFF3B82F6) else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(fname, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(desc, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                }
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { onImportMtzSelected(selectedSampleFile, 24.5) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("btn_parse_sample_mtz"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Parse Selected Preset Sample", fontSize = 13.sp)
                    }
                }
            }
        }

        // Extracted MTZ Package Details Inspector
        val parsed = uiState.importedMtzPackage
        if (parsed != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(
                        title = "Parsed Theme Details",
                        subtitle = "XML Manifest & Asset Verification",
                        icon = Icons.Default.Extension
                    )

                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "parsed_mtz_card"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = parsed.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Author: ${parsed.author} • Designer: ${parsed.designer} • ${parsed.version}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                                StatusTag(text = "VALID MTZ", color = Color(0xFF10B981))
                            }

                            // Preview Image if Extracted
                            if (parsed.previewImagePath != null && File(parsed.previewImagePath).exists()) {
                                Text(
                                    "Extracted Preview Image:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF0F172A))
                                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = File(parsed.previewImagePath),
                                        contentDescription = "Extracted MTZ Preview",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            } else {
                                // Default Simulated Visual Preview Canvas
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF0F172A))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "HyperOS Depth Clock & Icon Suite",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF3B82F6)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Parsed description.xml & component manifests verified.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = parsed.description,
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )

                            // Target OS badge
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.PhonelinkSetup,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Target: ${parsed.targetOs} (${parsed.sizeMb} MB) • File: ${parsed.fileName}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }

                            // Component Breakdown
                            Text(
                                "Included MTZ Components:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            val components = listOf(
                                Triple("Lockscreen Manifest & Depth Clock", Icons.Default.LockClock, parsed.hasLockscreen),
                                Triple("Dynamic App Icons Set", Icons.Default.Widgets, parsed.hasIcons),
                                Triple("Custom Typography (Fonts)", Icons.Default.FontDownload, parsed.hasFonts),
                                Triple("Control Center & System UI", Icons.Default.PhonelinkSetup, parsed.hasControlCenter),
                                Triple("Super Wallpapers Asset", Icons.Default.Image, parsed.hasWallpaper)
                            )

                            components.forEach { (compName, icon, isIncluded) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = if (isIncluded) Color(0xFF3B82F6) else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        compName,
                                        fontSize = 12.sp,
                                        color = if (isIncluded) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onSaveImportedTheme,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("btn_save_to_my_themes"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save Theme", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = onProceedToInstallerWizard,
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(42.dp)
                                        .testTag("btn_proceed_to_installer"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                                ) {
                                    Icon(Icons.Default.PhonelinkSetup, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Indian ROM Fix", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
