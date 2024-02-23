package com.brins.gpt.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.brins.gpt.ChatApp
import com.brins.gpt.theme.ThemeMode
import com.brins.lib_base.config.GENERAL_THEME
import com.brins.lib_base.config.LANGUAGE_NAME
import com.brins.lib_base.utils.AppUtils
import com.brins.lib_base.utils.MMKVUtils

object PreferenceUtils {

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(AppUtils.sApplication!!.applicationContext)

    val baseTheme get() = sharedPreferences.getStringOrDefault(GENERAL_THEME, "auto")

    fun getGeneralThemeValue(isSystemDark: Boolean): ThemeMode {
        return when (sharedPreferences.getStringOrDefault(GENERAL_THEME, "auto")) {
            "light" -> ThemeMode.LIGHT
            "dark" -> ThemeMode.DARK
            "auto" -> ThemeMode.AUTO
            else -> ThemeMode.AUTO
        }
    }
    var languageCode: String
        get() = sharedPreferences.getString(LANGUAGE_NAME, "auto") ?: "auto"
        set(value) = sharedPreferences.edit {
            putString(LANGUAGE_NAME, value)
        }

    fun SharedPreferences.getStringOrDefault(key: String, default: String): String {
        return getString(key, default) ?: default
    }

    fun registerOnSharedPreferenceChangedListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) = sharedPreferences.registerOnSharedPreferenceChangeListener(listener)


    fun unregisterOnSharedPreferenceChangedListener(
        changeListener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) = sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)

}
