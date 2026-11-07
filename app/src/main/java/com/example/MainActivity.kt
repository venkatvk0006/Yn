package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhonelinkSetup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.model.repository.ThemeRepository
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HyperOsInstallerWizardScreen
import com.example.ui.screens.MixAndMatchCustomizerScreen
import com.example.ui.screens.MtzImporterScreen
import com.example.ui.screens.ThemeCatalogScreen
import com.example.ui.screens.ThemeDetailScreen
import com.example.ui.theme.HyperBackgroundDark
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = ThemeRepository(db.themeDao())
        val factory = MainViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel(factory = factory)
                HyperThemeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun HyperThemeApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Listen to ViewModel toast events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { eventMsg ->
            scope.launch {
                snackbarHostState.showSnackbar(eventMsg)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = HyperBackgroundDark,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (uiState.selectedThemeForDetail == null) {
                NavigationBar(
                    containerColor = Color(0xFF131B2E),
                    contentColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        Triple("Home", Icons.Filled.Home, Icons.Outlined.Home),
                        Triple("Import", Icons.Filled.FolderOpen, Icons.Outlined.FolderOpen),
                        Triple("Patcher", Icons.Filled.PhonelinkSetup, Icons.Outlined.PhonelinkSetup),
                        Triple("Studio", Icons.Filled.Extension, Icons.Outlined.Extension),
                        Triple("Catalog", Icons.Filled.Palette, Icons.Outlined.Palette)
                    )

                    navItems.forEachIndexed { index, (label, filledIcon, outlinedIcon) ->
                        val isSelected = selectedBottomNavIndex == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedBottomNavIndex = index },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) filledIcon else outlinedIcon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFF1E293B)
                            ),
                            modifier = Modifier.testTag("nav_item_$index")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HyperBackgroundDark)
        ) {
            // App Dark Ambient Background
            Image(
                painter = painterResource(id = R.drawable.img_app_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Crop
            )

            val selectedDetail = uiState.selectedThemeForDetail

            if (selectedDetail != null) {
                ThemeDetailScreen(
                    theme = selectedDetail,
                    onBackClick = { viewModel.selectThemeForDetail(null) },
                    onApplyTheme = { theme -> viewModel.applyTheme(theme) },
                    onToggleFavorite = { theme -> viewModel.toggleFavorite(theme) },
                    onProceedToInstaller = {
                        viewModel.selectThemeForDetail(null)
                        selectedBottomNavIndex = 2
                    }
                )
            } else {
                when (selectedBottomNavIndex) {
                    0 -> HomeScreen(
                        uiState = uiState,
                        onNavigateToImport = { selectedBottomNavIndex = 1 },
                        onNavigateToInstaller = { selectedBottomNavIndex = 2 },
                        onNavigateToCustomizer = { selectedBottomNavIndex = 3 },
                        onNavigateToCatalog = { selectedBottomNavIndex = 4 },
                        onSelectThemeDetail = { theme -> viewModel.selectThemeForDetail(theme) },
                        onToggleFavorite = { theme -> viewModel.toggleFavorite(theme) },
                        onApplyTheme = { theme -> viewModel.applyTheme(theme) }
                    )
                    1 -> MtzImporterScreen(
                        uiState = uiState,
                        onImportMtzSelected = { fname, size -> viewModel.importMtzFile(fname, size) },
                        onImportMtzUriSelected = { uri -> viewModel.importMtzFromUri(context, uri) },
                        onSaveImportedTheme = { viewModel.saveImportedMtzAsTheme() },
                        onProceedToInstallerWizard = { selectedBottomNavIndex = 2 }
                    )
                    2 -> HyperOsInstallerWizardScreen(
                        uiState = uiState,
                        onStepSelected = { stepIdx -> viewModel.setInstallerStep(stepIdx) },
                        onStartPatchEngine = { onComplete -> viewModel.startHyperOsPatchEngine(context, onComplete) },
                        onTriggerSystemAction = { ctx, actionType, target ->
                            viewModel.triggerSystemAction(ctx, actionType, target)
                        }
                    )
                    3 -> MixAndMatchCustomizerScreen(
                        uiState = uiState,
                        onUpdateConfig = { transform -> viewModel.updateCustomizerConfig(transform) },
                        onSaveCustomizedTheme = { viewModel.saveCustomizedTheme() }
                    )
                    4 -> ThemeCatalogScreen(
                        uiState = uiState,
                        onSelectThemeDetail = { theme -> viewModel.selectThemeForDetail(theme) },
                        onToggleFavorite = { theme -> viewModel.toggleFavorite(theme) },
                        onApplyTheme = { theme -> viewModel.applyTheme(theme) },
                        onDeleteTheme = { theme -> viewModel.deleteTheme(theme) }
                    )
                }
            }
        }
    }
}
