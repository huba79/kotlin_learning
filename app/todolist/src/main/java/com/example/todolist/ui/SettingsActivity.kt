package com.example.todolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.todolist.R
import com.example.todolist.databinding.ActivitySettingsBinding
import com.example.todolist.utils.PreferencesManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var isInitializing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferencesManager.applyColorTheme(this)
        super.onCreate(savedInstanceState)
        PreferencesManager.applyStatusBarColor(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadCurrentPreferences()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadCurrentPreferences() {
        isInitializing = true

        // 1. Theme Mode
        when (PreferencesManager.getThemeMode(this)) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.radioThemeLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding.radioThemeDark.isChecked = true
            else -> binding.radioThemeSystem.isChecked = true
        }

        // 2. Color Theme
        when (PreferencesManager.getColorThemeKey(this)) {
            PreferencesManager.COLOR_THEME_OCEAN -> binding.radioColorOcean.isChecked = true
            PreferencesManager.COLOR_THEME_EMERALD -> binding.radioColorEmerald.isChecked = true
            else -> binding.radioColorDefault.isChecked = true
        }

        // 3. Language
        when (PreferencesManager.getLanguageTag(this)) {
            "hu" -> binding.radioLangHu.isChecked = true
            "ro" -> binding.radioLangRo.isChecked = true
            else -> binding.radioLangEn.isChecked = true
        }

        isInitializing = false
    }

    private fun setupListeners() {
        binding.radioGroupThemeMode.setOnCheckedChangeListener { _, checkedId ->
            if (isInitializing) return@setOnCheckedChangeListener
            val mode = when (checkedId) {
                R.id.radioThemeLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.radioThemeDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            PreferencesManager.saveThemeMode(this, mode)
        }

        binding.radioGroupColorTheme.setOnCheckedChangeListener { _, checkedId ->
            if (isInitializing) return@setOnCheckedChangeListener
            val colorKey = when (checkedId) {
                R.id.radioColorOcean -> PreferencesManager.COLOR_THEME_OCEAN
                R.id.radioColorEmerald -> PreferencesManager.COLOR_THEME_EMERALD
                else -> PreferencesManager.COLOR_THEME_DEFAULT
            }
            if (colorKey != PreferencesManager.getColorThemeKey(this)) {
                PreferencesManager.saveColorTheme(this, colorKey)
                recreate()
            }
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            if (isInitializing) return@setOnCheckedChangeListener
            val langTag = when (checkedId) {
                R.id.radioLangHu -> "hu"
                R.id.radioLangRo -> "ro"
                else -> "en"
            }
            if (langTag != PreferencesManager.getLanguageTag(this)) {
                PreferencesManager.saveLanguageTag(this, langTag)
                recreate()
            }
        }
    }
}
