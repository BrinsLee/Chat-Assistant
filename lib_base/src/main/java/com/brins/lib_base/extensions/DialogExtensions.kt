package com.brins.lib_base.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.materialDialog(): MaterialAlertDialogBuilder {
    return MaterialAlertDialogBuilder(requireContext())
}