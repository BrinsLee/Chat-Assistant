package com.brins.gpt.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.preference.DialogPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.brins.gpt.R
import com.brins.gpt.dialog.ATEListPreferenceDialogFragmentCompat
import com.brins.gpt.dialog.ATEPreferenceDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BasePreferenceFragmentCompat: PreferenceFragmentCompat() {

    open fun onCreatePreferenceDialog(preference: Preference): DialogFragment? {
        if (preference is ListPreference) {
            return ATEListPreferenceDialogFragmentCompat.newInstance(preference.key)
        } else if (preference is DialogPreference) {
            return ATEPreferenceDialogFragment.newInstance(preference.key)
        }
        return null
    }

    internal fun setSummary(preference: Preference, value: Any?) {
        val stringValue = value.toString()
        if (preference is ListPreference) {
            val index = preference.findIndexOfValue(stringValue)
            preference.setSummary(if (index >= 0) preference.entries[index] else null)
        } else {
            preference.summary = stringValue
        }
    }

    protected fun setSummary(preference: Preference?) {
        preference?.let {
            setSummary(
                it, PreferenceManager
                    .getDefaultSharedPreferences(it.context)
                    .getString(it.key, "")
            )
        }
    }

    abstract fun invalidateSettings()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            listView.overScrollMode = View.OVER_SCROLL_NEVER
        }

        listView.updatePadding(bottom = resources.getDimensionPixelSize(R.dimen.spacing_56dp))
        /*listView.applyInsetter {
            type(navigationBars = true) {
                padding(vertical = true)
            }
        }*/
        invalidateSettings()
    }

    @SuppressLint("RestrictedApi")
    override fun onDisplayPreferenceDialog(preference: Preference) {
        /*when (preference) {
            is LibraryPreference -> {
                val fragment = LibraryPreferenceDialog.newInstance()
                fragment.show(childFragmentManager, preference.key)
            }
            is NowPlayingScreenPreference -> {
                val fragment = NowPlayingScreenPreferenceDialog.newInstance()
                fragment.show(childFragmentManager, preference.key)
            }
            is AlbumCoverStylePreference -> {
                val fragment = AlbumCoverStylePreferenceDialog.newInstance()
                fragment.show(childFragmentManager, preference.key)
            }
            is BlacklistPreference -> {
                val fragment = BlacklistPreferenceDialog.newInstance()
                fragment.show(childFragmentManager, preference.key)
            }
            is DurationPreference -> {
                val fragment = DurationPreferenceDialog.newInstance()
                fragment.show(childFragmentManager, preference.key)
            }
            else -> super.onDisplayPreferenceDialog(preference)
        }*/
        if (callbackFragment is OnPreferenceDisplayDialogCallback) {
            (callbackFragment as OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }
        if (activity is OnPreferenceDisplayDialogCallback) {
            (activity as OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }

        if (parentFragmentManager.findFragmentByTag("androidx.preference.PreferenceFragment.DIALOG") == null) {
            val dialogFragment: DialogFragment? = onCreatePreferenceDialog(preference)
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(
                    this.parentFragmentManager,
                    "androidx.preference.PreferenceFragment.DIALOG"
                )
                return
            }
        }
        super.onDisplayPreferenceDialog(preference)
    }

    fun restartActivity() {
        activity?.recreate()
    }
}