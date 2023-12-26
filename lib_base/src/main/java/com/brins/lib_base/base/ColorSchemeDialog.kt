package com.brins.lib_base.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brins.lib_base.utils.ScreenUtils

class ColorSchemeDialog(private val list: Array<String>,
    private val title: String,
    private var onItemClick: (String) -> Unit): DialogFragment() {


    companion object {
        fun createColorSchemeDialog(list: Array<String>, title: String, onItemClick: (String) -> Unit): ColorSchemeDialog {
            return ColorSchemeDialog(list, title, onItemClick)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ScreenUtils.dp2px(context, 280f), ViewGroup.LayoutParams.WRAP_CONTENT);
            setGravity(Gravity.CENTER)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(title)
        builder.setItems(list) {dialog, which ->
            onItemClick(list[which])
            dismissAllowingStateLoss()
        }
        return builder.create()
    }
}