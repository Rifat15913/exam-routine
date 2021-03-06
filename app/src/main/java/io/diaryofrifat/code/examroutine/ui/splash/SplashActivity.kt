package io.diaryofrifat.code.examroutine.ui.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.ViewUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            ViewUtils.setStatusBarColor(this, R.color.colorWhite)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewUtils.setStatusBarColor(this, R.color.darkBackground)
        }
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed({
            SelectionContainerActivity.startActivity(this)
        }, 0)
    }
}