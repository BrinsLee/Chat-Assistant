package com.brins.lib_base.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.brins.lib_base.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layout: Int): Fragment(layout) {

    protected val loadingDialog: LoadingDialog by lazy { LoadingDialog.createLoading() }
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
}