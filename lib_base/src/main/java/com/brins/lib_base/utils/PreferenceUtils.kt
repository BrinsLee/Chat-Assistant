package com.brins.lib_base.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.brins.gpt.theme.ThemeMode
import com.brins.lib_base.config.GENERAL_THEME
import com.brins.lib_base.config.LANGUAGE_NAME
import com.brins.lib_base.config.MANAGE_AUDIO_FOCUS
import com.brins.lib_base.config.PLAYBACK_PITCH
import com.brins.lib_base.config.PLAYBACK_SPEED

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


    val isAudioFocusEnabled
        get() = sharedPreferences.getBoolean(
            MANAGE_AUDIO_FOCUS, false
        )

    var playbackSpeed
        get() = sharedPreferences
            .getFloat(PLAYBACK_SPEED, 1F)
        set(value) = sharedPreferences.edit { putFloat(PLAYBACK_SPEED, value) }

    var playbackPitch
        get() = sharedPreferences
            .getFloat(PLAYBACK_PITCH, 1F)
        set(value) = sharedPreferences.edit { putFloat(PLAYBACK_PITCH, value) }
}
