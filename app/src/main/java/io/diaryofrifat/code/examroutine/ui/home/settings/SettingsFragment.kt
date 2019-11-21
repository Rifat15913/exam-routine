package io.diaryofrifat.code.examroutine.ui.home.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.libs.ToastUtils
import timber.log.Timber
import java.util.*

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

            getString(R.string.settings_key_give_feedback) -> {
                var body: String? = null
                val packageManager = context?.packageManager

                try {
                    body = String.format(
                            Locale.getDefault(),
                            getString(R.string.settings_feedback_email_body),
                            Build.VERSION.RELEASE,
                            packageManager?.getPackageInfo(context?.packageName, 0)?.versionName,
                            Build.BRAND,
                            Build.MODEL,
                            Build.MANUFACTURER
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    Timber.e(e)
                }

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(Constants.Common.PREFIX_MAILTO)
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_developer_email)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_feedback_email_subject))
                    putExtra(Intent.EXTRA_TEXT, body)
                }

                if (packageManager != null && intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}