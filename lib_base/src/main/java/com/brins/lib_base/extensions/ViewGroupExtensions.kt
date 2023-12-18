package com.brins.lib_base.extensions

import android.view.LayoutInflater
import android.view.ViewGroup

internal val ViewGroup.ThemeInflater: LayoutInflater
    get() {
        return LayoutInflater.from(context.createThemeWrapper())
    }