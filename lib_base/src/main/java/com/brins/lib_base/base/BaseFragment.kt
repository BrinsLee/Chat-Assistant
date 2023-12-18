package com.brins.lib_base.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
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
}