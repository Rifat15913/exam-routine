package io.diaryofrifat.code.examroutine.ui.home.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference == null || context == null) {
            return super.onPreferenceTreeClick(preference)
        }

        when (preference.key) {
            getString(R.string.settings_key_change_exam) -> {
                SelectionContainerActivity.startActivity(context!!)
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}