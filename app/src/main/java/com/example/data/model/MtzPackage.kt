package com.example.data.model

data class MtzPackage(
    val title: String,
    val author: String,
    val designer: String = "HyperOS Studio",
    val version: String,
    val description: String,
    val targetOs: String,
    val sizeMb: Double,
    val fileName: String,
    val hasLockscreen: Boolean = true,
    val hasIcons: Boolean = true,
    val hasFonts: Boolean = true,
    val hasControlCenter: Boolean = true,
    val hasStatusBar: Boolean = true,
    val hasWallpaper: Boolean = true,
    val previewLockscreenStyle: String = "Depth Clock iOS 18",
    val previewIconStyle: String = "HyperOS Squircle",
    val previewPrimaryColorHex: String = "#3B82F6",
    val previewImagePath: String? = null,
    val tags: List<String> = listOf("HyperOS 1.0", "Indian ROM Ready", "Depth Lock")
)

data class CustomizerConfig(
    val themeName: String = "My Custom Hyper Theme",
    val lockscreenStyle: String = "Hyper Depth Clock 18",
    val depthEffectEnabled: Boolean = true,
    val controlCenterStyle: String = "Translucent Glass Blur",
    val iconPackStyle: String = "HyperOS Dynamic Squircles",
    val fontStyle: String = "San Francisco Pro",
    val statusBarStyle: String = "iOS Dynamic Island",
    val batteryIndicatorStyle: String = "Percentage Circle",
    val accentColorHex: String = "#3B82F6",
    val wallpaperType: String = "HyperOS Super Wallpaper Earth",
    val bootAnimation: String = "HyperOS Glowing Ring"
)

data class InstallationStep(
    val stepNumber: Int,
    val title: String,
    val summary: String,
    val detailedInstructions: List<String>,
    val actionType: StepActionType = StepActionType.NONE,
    val targetPackageOrAction: String? = null,
    val tipOrWarning: String? = null
)

enum class StepActionType {
    NONE,
    OPEN_THEME_STORE,
    OPEN_REGION_SETTINGS,
    OPEN_DEVELOPER_OPTIONS,
    SELECT_MTZ_FILE,
    PATCH_THEME_STORE_CACHE,
    APPLY_CUSTOM_THEME
}
