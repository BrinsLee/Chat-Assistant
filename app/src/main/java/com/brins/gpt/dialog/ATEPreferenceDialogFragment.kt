package com.brins.gpt.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.preference.DialogPreference
import androidx.preference.DialogPreference.TargetFragment
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class ATEPreferenceDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

    companion object {
        const val TAG = "PreferenceDialogFragment"
        const val ARG_KEY = "key"

        fun newInstance(key: String?): ATEPreferenceDialogFragment? {
            val fragment: ATEPreferenceDialogFragment = ATEPreferenceDialogFragment()
            val b = Bundle(1)
            b.putString(
                ATEPreferenceDialogFragment.ARG_KEY, key
            )
            fragment.arguments = b
            return fragment
        }

    }

    private var mWhichButtonClicked = 0
    private var mPreference: DialogPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rawFragment = this.targetFragment
        check(rawFragment is TargetFragment) { "Target fragment must implement TargetFragment interface" }
        val fragment = rawFragment as TargetFragment
        val key =
            this.requireArguments().getString(ATEPreferenceDialogFragment.ARG_KEY)?:""
        mPreference = fragment.findPreference(key)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = this.activity
        val builder = MaterialAlertDialogBuilder(
            context!!, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        ).setTitle(mPreference!!.dialogTitle).setIcon(mPreference!!.dialogIcon)
            .setMessage(mPreference!!.dialogMessage)
            .setPositiveButton(mPreference!!.positiveButtonText, this)
            .setNegativeButton(mPreference!!.negativeButtonText, this)
        this.onPrepareDialogBuilders(builder)
        val dialog = builder.create()
        if (this.needInputMethod()) {
            this.requestInputMethod(dialog)
        }
        return dialog
    }

    fun getPreference(): DialogPreference? {
        return mPreference
    }

    protected open fun needInputMethod(): Boolean {
        return false
    }

    private fun requestInputMethod(dialog: Dialog) {
        val window = dialog.window
        window!!.setSoftInputMode(5)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.i(
            ATEPreferenceDialogFragment.TAG,
            "onDismiss: $mWhichButtonClicked"
        )
        onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE)
    }

    open fun onDialogClosed(positiveResult: Boolean) {}

    override fun onClick(dialog: DialogInterface?, which: Int) {
        Log.i(
            ATEPreferenceDialogFragment.TAG,
            "onClick: $which"
        )
        mWhichButtonClicked = which
        onDialogClosed(which == DialogInterface.BUTTON_POSITIVE)
    }

    open fun onPrepareDialogBuilders(builder: MaterialAlertDialogBuilder) {}
}