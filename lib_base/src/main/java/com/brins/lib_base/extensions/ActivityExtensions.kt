package com.brins.lib_base.extensions

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import com.brins.lib_base.R
import com.brins.lib_base.utils.ColorUtil
import com.brins.lib_base.utils.VersionUtils

fun AppCompatActivity.hideStatusBar() {
    hideStatusBar(true)
}

fun AppCompatActivity.setBehindStatusBar() {
    hideStatusBar(false)
}

private fun AppCompatActivity.hideStatusBar(fullscreen: Boolean) {
    val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
    if (statusBar != null) {
        statusBar.isGone = fullscreen
    }
    setEdgeToEdgeOrImmersive(fullscreen)
}
fun AppCompatActivity.setEdgeToEdgeOrImmersive(isFullScreen: Boolean) {
    if (isFullScreen) {
        setImmersiveFullscreen()
    } else {
        setDrawBehindSystemBars()
    }
}

fun AppCompatActivity.setImmersiveFullscreen() {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
    if (VersionUtils.hasP()) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
    if (VersionUtils.hasR()) {
        with(window) {
            setDecorFitsSystemWindows(false)
            insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
        if (insets.displayCutout != null) {
            insets
        } else {
            // Consume insets if display doesn't have a Cutout
            WindowInsetsCompat.CONSUMED
        }
    }
}


fun AppCompatActivity.setDrawBehindSystemBars() {
    setNavigationBarColorPreOreo(surfaceColor())
    if (VersionUtils.hasMarshmallow()) {
        setStatusBarColor(getColor(R.color.white))
    } else {
        setStatusBarColor(Color.BLACK)
    }
    /*if (VersionUtils.hasOreo()) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        if (VersionUtils.hasQ()) {
            window.isNavigationBarContrastEnforced = false
        }
    } else {
        setNavigationBarColorPreOreo(surfaceColor())
        if (VersionUtils.hasMarshmallow()) {
            setStatusBarColor(Color.TRANSPARENT)
        } else {
            setStatusBarColor(Color.BLACK)
        }
    }*/
}

fun AppCompatActivity.setNavigationBarColorPreOreo(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        window.navigationBarColor = ColorUtil.darkenColor(color)
    }
}


fun AppCompatActivity.setStatusBarColor(color: Int) {
    val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
    if (statusBar != null) {
        when {
            VersionUtils.hasMarshmallow() -> statusBar.setBackgroundColor(color)
            else -> statusBar.setBackgroundColor(
                ColorUtil.darkenColor(
                    color
                )
            )
        }
    } else {
        when {
            VersionUtils.hasMarshmallow() -> window.statusBarColor = color
            else -> window.statusBarColor = ColorUtil.darkenColor(color)
        }
    }
    setLightStatusBarAuto(surfaceColor())
}

fun AppCompatActivity.setLightStatusBarAuto(bgColor: Int) {
    setLightStatusBar(bgColor.isColorLight)
}

fun AppCompatActivity.setLightStatusBar(enabled: Boolean) {
    if (VersionUtils.hasMarshmallow()) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility
        if (enabled) {
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

inline val Activity.rootView: View get() = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
