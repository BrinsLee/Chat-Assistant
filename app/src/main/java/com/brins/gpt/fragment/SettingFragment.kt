package com.brins.gpt.fragment


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.brins.gpt.R
import com.brins.gpt.theme.ThemeMode
import com.brins.gpt.utils.PreferenceUtils.languageCode
import com.brins.lib_base.config.GENERAL_THEME
import com.brins.lib_base.config.LANGUAGE_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BasePreferenceFragmentCompat() {

    override fun invalidateSettings() {
        /*val languagePreference: ListPreference? = findPreference(LANGUAGE_NAME)
        languagePreference?.let {
            it.setOnPreferenceChangeListener { _, _ ->
                restartActivity()
                return@setOnPreferenceChangeListener true
            }
        }*/

        val generalTheme: Preference? = findPreference(GENERAL_THEME)
        generalTheme?.let {
            setSummary(it)
            it.setOnPreferenceChangeListener { _, newValue ->
                setSummary(it, newValue)
//                ThemeStore.markChanged(requireContext())
                changeColorTheme(newValue as String)
                true
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        languageCode =
            AppCompatDelegate.getApplicationLocales().toLanguageTags().ifEmpty { "auto" }
        addPreferencesFromResource(R.xml.pref_setting)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val languagePreference: Preference? = findPreference(LANGUAGE_NAME)
        languagePreference?.let {
            setSummary(it, languageCode)
            it.setOnPreferenceChangeListener { prefs, newValue ->
                setSummary(prefs, newValue)
                if (newValue as? String == "auto") {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                } else {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            newValue as? String
                        )
                    )
                }
                true
            }
        }
    }

    private fun changeColorTheme(color: String) {
        context?.let {
            when(color) {
                ThemeMode.AUTO.themeName -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                ThemeMode.LIGHT.themeName -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                ThemeMode.DARK.themeName -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

}