package com.example.data.model.repository

import android.content.Context
import android.net.Uri
import com.example.data.db.ThemeDao
import com.example.data.db.ThemeEntity
import com.example.data.model.CustomizerConfig
import com.example.data.model.InstallationStep
import com.example.data.model.MtzPackage
import com.example.data.model.StepActionType
import com.example.service.InjectionResult
import com.example.service.MtzParserService
import com.example.service.ThemeInjectorService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ThemeRepository(private val themeDao: ThemeDao) {

    val allThemes: Flow<List<ThemeEntity>> = themeDao.getAllThemes()
    val favoriteThemes: Flow<List<ThemeEntity>> = themeDao.getFavoriteThemes()

    fun parseMtzFromUri(context: Context, uri: Uri): MtzPackage {
        return MtzParserService.parseMtzFromUri(context, uri)
    }

    suspend fun initDefaultPresetsIfEmpty() {
        val currentList = themeDao.getAllThemes().first()
        if (currentList.isEmpty()) {
            val presets = listOf(
                ThemeEntity(
                    name = "HyperOS iOS 18 Depth Pro",
                    author = "HyperStudio Developer",
                    version = "v1.2.0",
                    description = "Features HyperOS 1 depth effect lockscreen with dual clock widgets, iOS 18 control center blur, squircle icons, and customized status bar icons. 100% compatible with HyperOS 1.0 Indian ROM.",
                    targetOs = "HyperOS 1.0 / Indian ROM",
                    mtzFileName = "hyperos_ios18_depth_pro.mtz",
                    lockscreenStyle = "iOS 18 Depth Clock",
                    iconPackStyle = "HyperOS Squircle Modern",
                    fontName = "San Francisco Pro",
                    controlCenterStyle = "Glassmorphism Blur",
                    statusBarStyle = "Dynamic Island iOS",
                    accentColorHex = "#3B82F6",
                    isCustomCreated = false,
                    isApplied = true,
                    isFavorite = true
                ),
                ThemeEntity(
                    name = "Glassmorphic HyperOS 1.0",
                    author = "MtzDesigner",
                    version = "v2.0.1",
                    description = "Translucent frosted glass aesthetics across Control Center, System Settings, Volume slider, and translucent notification cards with vibrant neon accents.",
                    targetOs = "HyperOS 1.0 / MIUI 14",
                    mtzFileName = "glassmorphic_hyper_v2.mtz",
                    lockscreenStyle = "Glass Widget Clock",
                    iconPackStyle = "Glassmorphic 3D",
                    fontName = "Inter Display",
                    controlCenterStyle = "Frosted Glass Translucent",
                    statusBarStyle = "Minimalist Dot Battery",
                    accentColorHex = "#8B5CF6",
                    isCustomCreated = false,
                    isApplied = false,
                    isFavorite = true
                ),
                ThemeEntity(
                    name = "Pixel Experience Hyper Edition",
                    author = "AOSP Devs",
                    version = "v1.0.5",
                    description = "Stock Android 15 Material You clean look adapted for Xiaomi HyperOS Indian ROM. Features Monet dynamic coloring engine, Material icons, and clean lockscreen clock.",
                    targetOs = "HyperOS 1.0",
                    mtzFileName = "pixel_exp_hyper.mtz",
                    lockscreenStyle = "Material You Double Line",
                    iconPackStyle = "Stock Android Round",
                    fontName = "Google Sans",
                    controlCenterStyle = "Material Quick Settings Tiles",
                    statusBarStyle = "Stock Android Pill",
                    accentColorHex = "#10B981",
                    isCustomCreated = false,
                    isApplied = false,
                    isFavorite = false
                ),
                ThemeEntity(
                    name = "Cyberpunk Red Hyper Matrix",
                    author = "CyberDesign",
                    version = "v3.1.0",
                    description = "Dark OLED theme with glowing crimson neon status bar, futuristic cyberpunk widgets, glitch font styling, and high-tech boot animation.",
                    targetOs = "HyperOS 1.0 / Indian ROM",
                    mtzFileName = "cyberpunk_red_matrix.mtz",
                    lockscreenStyle = "Cyberpunk Matrix Glitch Clock",
                    iconPackStyle = "Neumorphic Cyber Red",
                    fontName = "JetBrains Mono Bold",
                    controlCenterStyle = "Dark Cyber Tiles",
                    statusBarStyle = "Neon Signal Bar",
                    accentColorHex = "#EF4444",
                    isCustomCreated = false,
                    isApplied = false,
                    isFavorite = false
                ),
                ThemeEntity(
                    name = "HyperOS Super Wallpaper Earth",
                    author = "Xiaomi Ecosystem",
                    version = "v1.0.0",
                    description = "Extracts and applies HyperOS Super Wallpaper Earth lockscreen animations, dynamic geometry clock, and matching dark status bar icons.",
                    targetOs = "HyperOS 1.0",
                    mtzFileName = "super_wallpaper_earth.mtz",
                    lockscreenStyle = "Super Geometry Lockscreen",
                    iconPackStyle = "Stock HyperOS Icons",
                    fontName = "HyperSans Regular",
                    controlCenterStyle = "HyperOS Stock Glass",
                    statusBarStyle = "Circle Battery Percentage",
                    accentColorHex = "#F59E0B",
                    isCustomCreated = false,
                    isApplied = false,
                    isFavorite = false
                )
            )

            presets.forEach { themeDao.insertTheme(it) }
        }
    }

    suspend fun saveTheme(theme: ThemeEntity): Long {
        return themeDao.insertTheme(theme)
    }

    suspend fun applyTheme(themeId: Long) {
        themeDao.clearAppliedStatus()
        themeDao.setAppliedTheme(themeId)
    }

    fun injectThemePayload(context: Context, theme: ThemeEntity): InjectionResult {
        return ThemeInjectorService.injectMtzTheme(context, theme)
    }

    suspend fun toggleFavorite(theme: ThemeEntity) {
        val updated = theme.copy(isFavorite = !theme.isFavorite)
        themeDao.updateTheme(updated)
    }

    suspend fun deleteTheme(theme: ThemeEntity) {
        themeDao.deleteTheme(theme)
    }

    suspend fun getThemeById(id: Long): ThemeEntity? {
        return themeDao.getThemeById(id)
    }

    fun parseMtzFileSimulated(fileName: String, fileSizeMb: Double): MtzPackage {
        val nameWithoutExt = fileName.removeSuffix(".mtz").replace("_", " ").replace("-", " ")
            .split(" ")
            .joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }

        return MtzPackage(
            title = if (nameWithoutExt.isBlank()) "Custom Hyper Theme" else nameWithoutExt,
            author = "MTZ Community Designer",
            version = "v1.0.0",
            description = "Parsed `.mtz` theme package containing lockscreen_manifest.xml, icons, framework-res, fonts, control_center, and wallpaper. Ready for HyperOS Indian ROM installation.",
            targetOs = "Xiaomi HyperOS 1.0 / Indian ROM",
            sizeMb = if (fileSizeMb <= 0.0) 18.5 else fileSizeMb,
            fileName = fileName,
            hasLockscreen = true,
            hasIcons = true,
            hasFonts = true,
            hasControlCenter = true,
            hasStatusBar = true,
            hasWallpaper = true,
            previewLockscreenStyle = "Hyper Depth Clock",
            previewIconStyle = "Squircle Dynamic",
            previewPrimaryColorHex = "#3B82F6"
        )
    }

    fun createMtzFromConfig(config: CustomizerConfig): ThemeEntity {
        return ThemeEntity(
            name = config.themeName.ifBlank { "Custom HyperOS Theme" },
            author = "You (Hyper Theme Studio)",
            version = "v1.0",
            description = "Custom synthesized MTZ theme combining ${config.lockscreenStyle} lockscreen, ${config.iconPackStyle} icons, ${config.fontStyle} typography, and ${config.controlCenterStyle} control center.",
            targetOs = "HyperOS 1.0 / Indian ROM",
            mtzFileName = "${config.themeName.lowercase().replace(" ", "_")}_custom.mtz",
            lockscreenStyle = config.lockscreenStyle,
            iconPackStyle = config.iconPackStyle,
            fontName = config.fontStyle,
            controlCenterStyle = config.controlCenterStyle,
            statusBarStyle = config.statusBarStyle,
            accentColorHex = config.accentColorHex,
            isCustomCreated = true,
            isApplied = false,
            isFavorite = false
        )
    }

    fun getHyperOsIndianRomInstallerSteps(): List<InstallationStep> {
        return listOf(
            InstallationStep(
                stepNumber = 1,
                title = "Select & Import MTZ Theme",
                summary = "Import or synthesize your target `.mtz` file into Hyper Theme Studio workspace.",
                detailedInstructions = listOf(
                    "Choose an MTZ file from internal storage or pick from the Preset Theme Gallery.",
                    "The MTZ Parser automatically verifies file structure (description.xml, icons, lockscreen_manifest, framework-res).",
                    "Click 'Prepare for HyperOS 1.0 Engine' to compile patch payload."
                ),
                actionType = StepActionType.SELECT_MTZ_FILE,
                tipOrWarning = "Make sure your device is running Xiaomi HyperOS 1.0 (Android 14 / Android 15 based Indian ROM)."
            ),
            InstallationStep(
                stepNumber = 2,
                title = "Xiaomi Theme Store Region Setup (Indian ROM Fix)",
                summary = "Adjust Theme Store authorization parameters to bypass 3rd-party theme signature blocks.",
                detailedInstructions = listOf(
                    "Open Device Settings -> Additional Settings -> Region.",
                    "Temporarily set Region to 'India' or 'United States' (if on Indian ROM, toggle region once to reset Theme Store token cache).",
                    "Open Theme Store -> Settings -> Clear Cache & force stop Theme Store app.",
                    "This clears the signature enforcement lock."
                ),
                actionType = StepActionType.OPEN_REGION_SETTINGS,
                targetPackageOrAction = "android.settings.LOCATION_SOURCE_SETTINGS",
                tipOrWarning = "Indian ROM Theme Manager uses regional token validation. Resetting Region cache removes error 'Themes from 3rd party sources are not supported'."
            ),
            InstallationStep(
                stepNumber = 3,
                title = "Inject MTZ Theme Payload to Local Themes",
                summary = "Use Theme Patcher Engine to place parsed MTZ components directly into Theme Store local data directory.",
                detailedInstructions = listOf(
                    "Click 'Inject MTZ Payload' button below.",
                    "The app generates local cache folders: /Android/data/com.android.theme/files/MtzThemes/",
                    "Locks component manifest signatures and creates local theme shortcut in Theme Store -> My Themes."
                ),
                actionType = StepActionType.PATCH_THEME_STORE_CACHE,
                tipOrWarning = "Allow File Access permission when requested so the installer can write to local theme cache."
            ),
            InstallationStep(
                stepNumber = 4,
                title = "Apply Theme via Theme Store / SetEdit Method",
                summary = "Finalize theme application seamlessly without authorization errors.",
                detailedInstructions = listOf(
                    "Open Xiaomi Theme Store -> Profile -> Themes.",
                    "You will now see your imported MTZ listed under 'Local Themes' as a verified theme.",
                    "Tap 'Apply' -> Select 'Apply All' (Lockscreen, Icons, System UI, Control Center).",
                    "Reboot device or restart System Launcher for full HyperOS blur and depth effect clocks to render!"
                ),
                actionType = StepActionType.OPEN_THEME_STORE,
                targetPackageOrAction = "com.android.thememanager",
                tipOrWarning = "If icons don't immediately refresh, go to Developer Options -> Force Desktop GPU Rendering or clear System Launcher cache."
            )
        )
    }

    fun getTroubleshootingGuides(): List<Pair<String, String>> {
        return listOf(
            "Error: 'Themes from 3rd party sources are not supported (402)'" to
                    "Cause: Theme Manager signature lock on Indian ROM.\nFix: Go to Step 2 in Installer Wizard. Clear Theme Store App Data, set Region to India, then re-inject MTZ via Hyper Theme Installer.",

            "Lockscreen Depth Clock Effect not showing behind wallpaper" to
                    "Cause: HyperOS Depth Effect requires wallpaper depth map.\nFix: In Mix & Match Customizer, enable 'HyperOS Depth Engine' and ensure wallpaper has clear subject foreground.",

            "Status bar icons or Control Center blur not changing" to
                    "Cause: HyperOS System Plugin cache retains previous theme.\nFix: Go to Device Settings -> Apps -> System UI Plugin -> Force Stop & Clear Cache, then re-apply theme.",

            "Custom Font reset back to Default after reboot" to
                    "Cause: Font subsystem override in HyperOS 1.0.\nFix: Apply theme using 'Font Component Only' in Customizer tab or use TTF to MTZ installer tool."
        )
    }
}
