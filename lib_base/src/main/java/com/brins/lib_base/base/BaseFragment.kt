package com.brins.lib_base.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.brins.lib_base.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layout: Int): Fragment(layout) {

    protected val loadingDialog: LoadingDialog by lazy { LoadingDialog.createLoading() }
    protected val colorSchemeDialog: ColorSchemeDialog by lazy { ColorSchemeDialog.createColorSchemeDialog(resources.getStringArray(R.array.color_theme_name),
        ContextCompat.getString(requireContext(), R.string.color_scheme), ::onColorSelected) }

    protected fun showChangeColorSchemeDialog() {
        if (!colorSchemeDialog.isAdded) {
            colorSchemeDialog.show(requireActivity().supportFragmentManager, "colorSchemeDialog")
        }
    }

    protected fun hideChangeColorSchemeDialog() {
        if (colorSchemeDialog.isAdded) {
            colorSchemeDialog.dismiss()
        }
    }

    protected fun showLoadingDialog() {
        if (!loadingDialog.isAdded) {
            loadingDialog.show(requireActivity().supportFragmentManager, "loadingDialog")
        }
    }

    protected fun hideLoadingDialog() {
        if (loadingDialog.isAdded) {
            loadingDialog.dismiss()
        }
    }

    protected fun navigateTo(id: Int, args: Bundle) {
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.scale_fragment_open_enter)
            .setExitAnim(R.anim.scale_fragment_open_exit)
            .setPopEnterAnim(R.anim.scale_fragment_close_enter)
            .setPopExitAnim(R.anim.scale_fragment_close_exit).build()
        findNavController().navigate(id, args, options, null)

    }

    protected fun popBackStack() {
        findNavController().popBackStack()
    }

    protected fun onColorSelected(color: String) {
        context?.let {
            when(color) {
                ContextCompat.getString(it, R.string.color_auto) -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                ContextCompat.getString(it, R.string.color_light) -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                ContextCompat.getString(it, R.string.color_dark) -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

    }
}