package com.example.todolist.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import com.example.todolist.R

object PreferencesManager {

    private const val PREFS_NAME = "todo_app_preferences"
    private const val KEY_THEME_MODE = "key_theme_mode"
    private const val KEY_COLOR_THEME = "key_color_theme"
    private const val KEY_LANGUAGE = "key_language"
    private const val KEY_NOTIFICATION_OFFSET = "key_notification_offset_minutes"

    const val COLOR_THEME_DEFAULT = "default"
    const val COLOR_THEME_OCEAN = "ocean"
    const val COLOR_THEME_EMERALD = "emerald"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getNotificationOffsetMinutes(context: Context): Int {
        return getPrefs(context).getInt(KEY_NOTIFICATION_OFFSET, 60)
    }

    fun saveNotificationOffsetMinutes(context: Context, minutes: Int) {
        getPrefs(context).edit().putInt(KEY_NOTIFICATION_OFFSET, minutes).apply()
    }

    fun getThemeMode(context: Context): Int {
        return getPrefs(context).getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun saveThemeMode(context: Context, mode: Int) {
        getPrefs(context).edit().putInt(KEY_THEME_MODE, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun getColorThemeKey(context: Context): String {
        return getPrefs(context).getString(KEY_COLOR_THEME, COLOR_THEME_DEFAULT) ?: COLOR_THEME_DEFAULT
    }

    fun saveColorTheme(context: Context, themeKey: String) {
        getPrefs(context).edit().putString(KEY_COLOR_THEME, themeKey).apply()
    }

    fun applyColorTheme(activity: Activity) {
        val themeKey = getColorThemeKey(activity)
        val styleResId = when (themeKey) {
            COLOR_THEME_OCEAN -> R.style.Theme_Widgets_Ocean
            COLOR_THEME_EMERALD -> R.style.Theme_Widgets_Emerald
            else -> R.style.Theme_Widgets
        }
        activity.setTheme(styleResId)
        applyStatusBarColor(activity, themeKey)
    }

    fun applyStatusBarColor(activity: Activity, themeKey: String = getColorThemeKey(activity)) {
        val statusBarColorRes = when (themeKey) {
            COLOR_THEME_OCEAN -> R.color.status_bar_ocean
            COLOR_THEME_EMERALD -> R.color.status_bar_emerald
            else -> R.color.status_bar_default
        }
        val color = ContextCompat.getColor(activity, statusBarColorRes)
        activity.window.statusBarColor = color

        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        val isDarkBackground = androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5
        insetsController.isAppearanceLightStatusBars = !isDarkBackground
    }

    fun getLanguageTag(context: Context): String {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (!currentLocales.isEmpty) {
            currentLocales[0]?.language?.let { return it }
        }
        return getPrefs(context).getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun saveLanguageTag(context: Context, languageTag: String) {
        getPrefs(context).edit().putString(KEY_LANGUAGE, languageTag).apply()
        val appLocales = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(appLocales)
    }

    fun applySavedPreferencesOnAppLaunch(context: Context) {
        val mode = getThemeMode(context)
        AppCompatDelegate.setDefaultNightMode(mode)

        val lang = getPrefs(context).getString(KEY_LANGUAGE, null)
        if (lang != null) {
            val appLocales = LocaleListCompat.forLanguageTags(lang)
            AppCompatDelegate.setApplicationLocales(appLocales)
        }
    }
}
