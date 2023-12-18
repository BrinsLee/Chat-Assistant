package com.brins.lib_base.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brins.lib_base.R
import com.brins.lib_base.extensions.materialDialog
import com.brins.lib_base.utils.ScreenUtils

class LoadingDialog: DialogFragment() {


    companion object {
        fun createLoading(): LoadingDialog {
            return LoadingDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ScreenUtils.dp2px(context, 100f), ViewGroup.LayoutParams.WRAP_CONTENT);
            setGravity(Gravity.CENTER)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog().setView(R.layout.layout_loading).create()
    }
}