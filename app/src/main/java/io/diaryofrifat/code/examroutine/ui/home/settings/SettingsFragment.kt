package io.diaryofrifat.code.examroutine.ui.home.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference == null || context == null || activity == null) {
            return super.onPreferenceTreeClick(preference)
        }

        when (preference.key) {
            getString(R.string.settings_key_change_exam) -> {
                SelectionContainerActivity.startActivity(context!!)
            }

            getString(R.string.settings_key_share_the_app) -> {
                ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle(getString(R.string.content_share_the_app))
                        .setText((getString(R.string.play_store_link)
                                + AndroidUtils.getApplicationId()))
                        .startChooser()
            }

            getString(R.string.settings_key_rate_it) -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.market_link)
                                    + AndroidUtils.getApplicationId())))
                } catch (e: ActivityNotFoundException) {
                    Timber.e(e)
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.play_store_link)
                                        + AndroidUtils.getApplicationId())))
                    } catch (e: Exception) {
                        Timber.e(e)
                        ToastUtils.nativeLong(getString(R.string.error_could_not_find_the_application))
                    }
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}