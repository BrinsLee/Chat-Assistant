package com.brins.lib_base.extensions

import androidx.annotation.ColorInt
import com.brins.lib_base.utils.ColorUtil

inline val @receiver:ColorInt Int.isColorLight
    get() = ColorUtil.isColorLight(this)