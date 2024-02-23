package com.brins.gpt.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ATEListPreferenceDialogFragmentCompat: ATEPreferenceDialogFragment() {
    companion object {
        const val TAG = "ListPreferenceDialogFragmentCompat"

        fun newInstance(key: String?): ATEListPreferenceDialogFragmentCompat? {
            val fragment: ATEListPreferenceDialogFragmentCompat =
                ATEListPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.setArguments(b)
            return fragment
        }
    }

    private var mClickedDialogEntryIndex = 0

    private fun getListPreference(): ListPreference? {
        return getPreference() as ListPreference?
    }


    override fun onPrepareDialogBuilders(builder: MaterialAlertDialogBuilder) {
        super.onPrepareDialogBuilders(builder)
        val preference = getListPreference()
        check(!(preference!!.entries == null || preference.entryValues == null)) { "ListPreference requires an entries array and an entryValues array." }
        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.value)
        builder.setSingleChoiceItems(
            preference.entries,
            mClickedDialogEntryIndex
        ) { dialog: DialogInterface?, which: Int ->
            mClickedDialogEntryIndex = which
            dismiss()
            onClick(dialog, which)
        }

        /*
             * The typical interaction for list-based dialogs is to have
             * click-on-an-item dismiss the dialog instead of the user having to
             * press 'Ok'.
             */
        builder.setPositiveButton(null, null)
        builder.setNegativeButton(null, null)
        builder.setNeutralButton(null, null)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val preference = getListPreference()
        Log.i(
            ATEListPreferenceDialogFragmentCompat.TAG,
            "onDialogClosed: $positiveResult"
        )
        if (positiveResult && mClickedDialogEntryIndex >= 0 && preference!!.entryValues != null) {
            val value = preference.entryValues[mClickedDialogEntryIndex].toString()
            Log.i(
                ATEListPreferenceDialogFragmentCompat.TAG,
                "onDialogClosed: value $value"
            )
            if (preference.callChangeListener(value)) {
                preference.value = value
                Log.i(
                    ATEListPreferenceDialogFragmentCompat.TAG,
                    "onDialogClosed: set value "
                )
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        Log.i(
            ATEListPreferenceDialogFragmentCompat.TAG,
            "onClick: $which"
        )
        mClickedDialogEntryIndex = which
        super.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

}