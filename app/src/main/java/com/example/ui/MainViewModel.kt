package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.ThemeEntity
import com.example.data.model.CustomizerConfig
import com.example.data.model.InstallationStep
import com.example.data.model.MtzPackage
import com.example.data.model.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val themes: List<ThemeEntity> = emptyList(),
    val favoriteThemes: List<ThemeEntity> = emptyList(),
    val activeTheme: ThemeEntity? = null,
    val selectedThemeForDetail: ThemeEntity? = null,
    val importedMtzPackage: MtzPackage? = null,
    val customizerConfig: CustomizerConfig = CustomizerConfig(),
    val installerSteps: List<InstallationStep> = emptyList(),
    val activeStepIndex: Int = 0,
    val isImportingMtz: Boolean = false,
    val isPatching: Boolean = false,
    val patchProgress: Float = 0f,
    val patchStatusMessage: String = "",
    val userNotification: String? = null
)

class MainViewModel(private val repository: ThemeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow: SharedFlow<String> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.initDefaultPresetsIfEmpty()
            _uiState.update {
                it.copy(installerSteps = repository.getHyperOsIndianRomInstallerSteps())
            }
        }

        viewModelScope.launch {
            repository.allThemes.collect { themeList ->
                val applied = themeList.find { it.isApplied } ?: themeList.firstOrNull()
                _uiState.update {
                    it.copy(
                        themes = themeList,
                        activeTheme = applied
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.favoriteThemes.collect { favs ->
                _uiState.update { it.copy(favoriteThemes = favs) }
            }
        }
    }

    fun selectThemeForDetail(theme: ThemeEntity?) {
        _uiState.update { it.copy(selectedThemeForDetail = theme) }
    }

    fun toggleFavorite(theme: ThemeEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(theme)
            _eventFlow.emit(if (!theme.isFavorite) "Added to Favorites" else "Removed from Favorites")
        }
    }

    fun applyTheme(theme: ThemeEntity) {
        viewModelScope.launch {
            repository.applyTheme(theme.id)
            _uiState.update { it.copy(activeTheme = theme.copy(isApplied = true)) }
            _eventFlow.emit("Applied '${theme.name}' to HyperOS Engine")
        }
    }

    fun deleteTheme(theme: ThemeEntity) {
        viewModelScope.launch {
            repository.deleteTheme(theme)
            _eventFlow.emit("Deleted '${theme.name}'")
        }
    }

    fun importMtzFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImportingMtz = true) }
            val parsedPackage = repository.parseMtzFromUri(context, uri)
            _uiState.update {
                it.copy(
                    importedMtzPackage = parsedPackage,
                    isImportingMtz = false
                )
            }
            _eventFlow.emit("Extracted and parsed '${parsedPackage.title}' MTZ package successfully!")
        }
    }

    fun importMtzFile(fileName: String, sizeMb: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImportingMtz = true) }
            kotlinx.coroutines.delay(800) // Realistic file parsing time

            val parsedPackage = repository.parseMtzFileSimulated(fileName, sizeMb)
            _uiState.update {
                it.copy(
                    importedMtzPackage = parsedPackage,
                    isImportingMtz = false
                )
            }
            _eventFlow.emit("Successfully parsed '${parsedPackage.title}' MTZ package!")
        }
    }

    fun saveImportedMtzAsTheme() {
        val mtz = _uiState.value.importedMtzPackage ?: return
        viewModelScope.launch {
            val entity = ThemeEntity(
                name = mtz.title,
                author = mtz.author,
                version = mtz.version,
                description = mtz.description,
                targetOs = mtz.targetOs,
                mtzFileName = mtz.fileName,
                lockscreenStyle = mtz.previewLockscreenStyle,
                iconPackStyle = mtz.previewIconStyle,
                accentColorHex = mtz.previewPrimaryColorHex,
                isCustomCreated = true
            )
            val newId = repository.saveTheme(entity)
            _uiState.update { it.copy(importedMtzPackage = null) }
            _eventFlow.emit("Saved '${mtz.title}' to local themes!")
        }
    }

    fun updateCustomizerConfig(transform: (CustomizerConfig) -> CustomizerConfig) {
        _uiState.update {
            it.copy(customizerConfig = transform(it.customizerConfig))
        }
    }

    fun saveCustomizedTheme() {
        viewModelScope.launch {
            val config = _uiState.value.customizerConfig
            val entity = repository.createMtzFromConfig(config)
            val newId = repository.saveTheme(entity)
            _eventFlow.emit("Created and saved MTZ Theme '${entity.name}'!")
        }
    }

    fun startHyperOsPatchEngine(context: Context, onCompleteStep: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPatching = true,
                    patchProgress = 0.1f,
                    patchStatusMessage = "Extracting MTZ theme structure & verification..."
                )
            }
            kotlinx.coroutines.delay(400)

            val currentThemes = repository.allThemes.first()
            val targetTheme = currentThemes.firstOrNull { it.isApplied }
                ?: currentThemes.firstOrNull()
                ?: repository.createMtzFromConfig(_uiState.value.customizerConfig)

            _uiState.update {
                it.copy(
                    patchProgress = 0.4f,
                    patchStatusMessage = "Executing ContentProvider & Shell Injection for '${targetTheme.name}'..."
                )
            }
            kotlinx.coroutines.delay(500)

            val result = repository.injectThemePayload(context, targetTheme)

            _uiState.update {
                it.copy(
                    patchProgress = 0.85f,
                    patchStatusMessage = "Injected via ${result.methodUsed}. Finalizing local Theme Store index..."
                )
            }
            kotlinx.coroutines.delay(400)

            _uiState.update {
                it.copy(
                    patchProgress = 1.0f,
                    patchStatusMessage = "Payload Injected to ${result.targetDirectory}! Ready to apply in Theme Store.",
                    isPatching = false
                )
            }
            _eventFlow.emit("Theme Injected via ${result.methodUsed}! Open Theme Store -> Local Themes.")
            onCompleteStep()
        }
    }

    fun setInstallerStep(stepIndex: Int) {
        _uiState.update { it.copy(activeStepIndex = stepIndex.coerceIn(0, 3)) }
    }

    fun triggerSystemAction(context: Context, actionType: com.example.data.model.StepActionType, target: String?) {
        when (actionType) {
            com.example.data.model.StepActionType.OPEN_THEME_STORE -> {
                try {
                    val intent = context.packageManager.getLaunchIntentForPackage("com.android.thememanager")
                    if (intent != null) {
                        context.startActivity(intent)
                    } else {
                        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:com.android.thememanager")
                        }
                        context.startActivity(settingsIntent)
                    }
                } catch (e: Exception) {
                    viewModelScope.launch { _eventFlow.emit("Opening Theme Settings...") }
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(settingsIntent)
                }
            }
            com.example.data.model.StepActionType.OPEN_REGION_SETTINGS -> {
                try {
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
            }
            com.example.data.model.StepActionType.OPEN_DEVELOPER_OPTIONS -> {
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
            }
            else -> {}
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(userNotification = null) }
    }
}

class MainViewModelFactory(private val repository: ThemeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
