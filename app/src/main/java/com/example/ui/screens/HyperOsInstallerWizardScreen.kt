package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstallationStep
import com.example.data.model.StepActionType
import com.example.ui.MainUiState
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusTag
import com.example.ui.components.StoragePermissionCard

@Composable
fun HyperOsInstallerWizardScreen(
    uiState: MainUiState,
    onStepSelected: (Int) -> Unit,
    onStartPatchEngine: (onComplete: () -> Unit) -> Unit,
    onTriggerSystemAction: (Context, StepActionType, String?) -> Unit
) {
    val context = LocalContext.current
    var expandedFaqIndex by remember { mutableIntStateOf(-1) }

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
                StatusTag(text = "HYPEROS 1.0 INDIAN ROM FIX", color = Color(0xFFF59E0B))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "MTZ Installer & Patcher Wizard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Follow this guided process to bypass '3rd-party theme unsupported (402)' signature locks on Xiaomi HyperOS Indian ROM devices.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Storage Access Permission Card
        item {
            StoragePermissionCard()
        }

        // Patcher Status Card
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "patcher_status_card"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Theme Signature Injector",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        StatusTag(
                            text = if (uiState.patchProgress >= 1f) "PATCHED" else "READY",
                            color = if (uiState.patchProgress >= 1f) Color(0xFF10B981) else Color(0xFF3B82F6)
                        )
                    }

                    if (uiState.isPatching || uiState.patchProgress > 0f) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            LinearProgressIndicator(
                                progress = { uiState.patchProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = Color(0xFF3B82F6),
                                trackColor = Color(0xFF1E293B)
                            )
                            Text(
                                text = uiState.patchStatusMessage,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    } else {
                        Text(
                            text = "Injects local MTZ authorization keys so Xiaomi Theme Manager treats third-party themes as verified official themes.",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }

                    Button(
                        onClick = {
                            onStartPatchEngine {
                                onStepSelected(3) // Jump to step 4 upon patch complete
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_start_patcher"),
                        enabled = !uiState.isPatching,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        if (uiState.isPatching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bypassing Signature Lock...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run HyperOS MTZ Signature Injector", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Installation Steps Wizard
        item {
            SectionHeader(
                title = "4-Step Installation Guide",
                subtitle = "Follow in order for Indian ROM",
                icon = Icons.Default.PhonelinkSetup
            )
        }

        itemsIndexed(uiState.installerSteps, key = { _, step -> step.stepNumber }) { index, step ->
            val isActive = uiState.activeStepIndex == index
            StepCard(
                step = step,
                isActive = isActive,
                onCardClick = { onStepSelected(index) },
                onActionTrigger = {
                    if (step.actionType == StepActionType.PATCH_THEME_STORE_CACHE) {
                        onStartPatchEngine { onStepSelected(3) }
                    } else {
                        onTriggerSystemAction(context, step.actionType, step.targetPackageOrAction)
                    }
                }
            )
        }

        // Troubleshooting & FAQ Accordion
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(
                    title = "HyperOS Troubleshooting",
                    subtitle = "Fix common 3rd party theme errors",
                    icon = Icons.Default.HelpOutline
                )

                val faqs = listOf(
                    "Error 402: 'Themes from third-party sources are not supported'" to
                            "This happens because Theme Store on Indian ROM requires digital signature verification. Run Step 3 'Run HyperOS MTZ Signature Injector' above, then open Theme Store -> Profile -> Local Themes to apply.",

                    "Lockscreen Depth Clock does not render behind subjects" to
                            "HyperOS Depth Effect requires a distinct subject in the wallpaper. Make sure 'HyperOS Depth Engine' is toggled ON in the Mix & Match Customizer tab.",

                    "Control Center blur or Volume slider not updating" to
                            "HyperOS caches System UI Plugin components. Go to Settings -> Apps -> Manage Apps -> System UI Plugin -> Clear All Data, then re-apply the MTZ theme.",

                    "Custom Font resets back to default upon phone restart" to
                            "Xiaomi system fonts require explicit font component installation. Select 'Fonts Component Only' in the Customizer before exporting the MTZ package."
                )

                faqs.forEachIndexed { faqIdx, (question, answer) ->
                    val isExpanded = expandedFaqIndex == faqIdx
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { expandedFaqIndex = if (isExpanded) -1 else faqIdx }
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = question,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8)
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = answer,
                                        fontSize = 12.sp,
                                        color = Color(0xFFCBD5E1),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepCard(
    step: InstallationStep,
    isActive: Boolean,
    onCardClick: () -> Unit,
    onActionTrigger: () -> Unit
) {
    val borderColor = if (isActive) Color(0xFF3B82F6) else Color(0xFF334155)
    val bgColor = if (isActive) Color(0xFF1E293B) else Color(0xFF131B2E)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onCardClick() }
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .testTag("step_card_${step.stepNumber}"),
        shape = RoundedCornerShape(18.dp),
        color = bgColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (isActive) Color(0xFF2563EB) else Color(0xFF334155),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${step.stepNumber}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = step.summary,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                if (isActive) {
                    StatusTag(text = "CURRENT", color = Color(0xFF3B82F6))
                }
            }

            AnimatedVisibility(visible = isActive) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF334155))
                    )

                    step.detailedInstructions.forEach { instruction ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                            Text(
                                text = instruction,
                                fontSize = 12.sp,
                                color = Color(0xFFE2E8F0),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    if (step.tipOrWarning != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "💡 Tip: ${step.tipOrWarning}",
                                fontSize = 11.sp,
                                color = Color(0xFFFCD34D)
                            )
                        }
                    }

                    if (step.actionType != StepActionType.NONE) {
                        Spacer(modifier = Modifier.height(4.dp))
                        val btnText = when (step.actionType) {
                            StepActionType.OPEN_THEME_STORE -> "Open Xiaomi Theme Store"
                            StepActionType.OPEN_REGION_SETTINGS -> "Open Region Settings"
                            StepActionType.OPEN_DEVELOPER_OPTIONS -> "Open Developer Options"
                            StepActionType.PATCH_THEME_STORE_CACHE -> "Run Signature Injector"
                            else -> "Action"
                        }

                        Button(
                            onClick = onActionTrigger,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("step_action_btn_${step.stepNumber}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Icon(
                                Icons.Default.Launch,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(btnText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
