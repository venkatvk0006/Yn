package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val author: String,
    val version: String,
    val description: String,
    val targetOs: String = "HyperOS 1.0 / MIUI 14",
    val mtzFileName: String = "",
    val lockscreenStyle: String = "Hyper Depth Clock",
    val iconPackStyle: String = "HyperOS Dynamic",
    val fontName: String = "HyperSans",
    val controlCenterStyle: String = "Translucent Glass",
    val statusBarStyle: String = "Dynamic Island iOS",
    val accentColorHex: String = "#3B82F6",
    val isCustomCreated: Boolean = false,
    val isApplied: Boolean = false,
    val isFavorite: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)
