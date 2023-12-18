package com.brins.lib_base.utils

import android.content.Context

import com.tencent.mmkv.MMKV

object MMKVUtils {

    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }

    fun init(context: Context) {
        MMKV.initialize(context)
    }

    private fun getDefaultMMKV() = mmkv


    //===============================String=======================

    fun putString(key: String, value: String) {
        if (key.isNotEmpty()) {
            getDefaultMMKV().putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String): String {
        if (key.isEmpty()) {
            return defaultValue
        }
        return getDefaultMMKV().getString(key, defaultValue)?:defaultValue
    }


    fun encode(key: String, value: String) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().encode(key, value)
    }

    fun decodeString(key: String, defaultValue: String): String {
        return if (key.isEmpty()) {
            defaultValue
        } else getDefaultMMKV().decodeString(key, defaultValue)?:""
    }

    //==============================Int============================

    fun putInt(key: String, value: Int) {
        getDefaultMMKV().putInt(key, value)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return getDefaultMMKV().getInt(key, defaultValue)
    }

    fun encode(key: String, value: Int) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().encode(key, value)
    }

    fun decodeInt(key: String, defaultValue: Int = 0): Int {
        return if (key.isEmpty()) {
            defaultValue
        } else getDefaultMMKV().decodeInt(key, defaultValue)
    }


    //==============================Long============================

    fun putLong(key: String, value: Long) {
        getDefaultMMKV().putLong(key, value)
    }

    fun getLong(key: String, defaultValue: Long = 0): Long {
        return getDefaultMMKV().getLong(key, defaultValue)
    }

    fun encode(key: String, value: Long) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().encode(key, value)
    }

    fun decodeLong(key: String, defaultValue: Long = 0): Long {
        return if (key.isEmpty()) {
            defaultValue
        } else getDefaultMMKV().decodeLong(key, defaultValue)
    }

    //==============================Float============================

    fun putFloat(key: String, value: Float) {
        getDefaultMMKV().putFloat(key, value)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return getDefaultMMKV().getFloat(key, defaultValue)
    }

    fun encode(key: String, value: Float) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().encode(key, value)
    }

    fun decodeFloat(key: String, defaultValue: Float = 0f): Float {
        return if (key.isEmpty()) {
            defaultValue
        } else getDefaultMMKV().decodeFloat(key, defaultValue)
    }


    //==============================Boolean============================

    fun putBoolean(key: String, value: Boolean) {
        getDefaultMMKV().putBoolean(key, value)
    }

    fun getBoolean(key: String, defaultValue: Boolean = true): Boolean {
        return getDefaultMMKV().getBoolean(key, defaultValue)
    }

    fun encode(key: String, value: Boolean) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().encode(key, value)
    }

    fun decodeBoolean(key: String, defaultValue: Boolean = true): Boolean {
        return if (key.isEmpty()) {
            defaultValue
        } else getDefaultMMKV().decodeBool(key, defaultValue)
    }


    fun contain(key: String): Boolean {
        return getDefaultMMKV().contains(key)
    }


    //================================删除=================================
    fun remove(key: String) {
        if (key.isEmpty()) {
            return
        }
        getDefaultMMKV().remove(key)
    }

/*    fun getGeneralThemeValue(isDark: Boolean): ThemeStyle {
        val themeMode: String =
            getString(GENERAL_THEME_STYLE, "auto")
        return when (themeMode) {
            "light" -> ThemeStyle.LIGHT
            "dark" -> ThemeStyle.DARK
            "auto" -> if (isDark) ThemeStyle.DARK else ThemeStyle.LIGHT
            else -> ThemeStyle.AUTO
        }

    }*/


/*
    val wallpaperAccent
        get() = getBoolean(WALLPAPER_ACCENT, VersionUtils.hasOreoMR1() && !VersionUtils.hasS())
*/

}