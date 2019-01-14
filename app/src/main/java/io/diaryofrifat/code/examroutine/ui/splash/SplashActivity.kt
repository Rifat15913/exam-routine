package io.diaryofrifat.code.examroutine.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.decisionmaker.DecisionMakerActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, DecisionMakerActivity::class.java))
        finish()
    }
}