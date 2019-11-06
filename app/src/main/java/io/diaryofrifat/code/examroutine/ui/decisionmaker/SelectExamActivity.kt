package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.lusosmile.main.ui.base.component.BaseItemDetailsLookup
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.SelectionListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.helper.GridSpacingItemDecoration
import io.diaryofrifat.code.examroutine.ui.home.HomeActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.activity_select_exam.*
import timber.log.Timber


class SelectExamActivity : BaseActivity<SelectExamMvpView, SelectExamPresenter>(),
        SelectExamMvpView, SelectionListener {

    private var mInterstitialAd: InterstitialAd? = null
    private var mSelectedExamType: ExamType? = null

    private var mTracker: SelectionTracker<Long>? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_select_exam

    override fun getActivityPresenter(): SelectExamPresenter {
        return SelectExamPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            ViewUtils.setStatusBarColor(this, R.color.white)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewUtils.setStatusBarColor(this, R.color.darkBackground)
        } else {
            // Do nothing for Jelly bean and Kitkat devices
        }

        if (savedInstanceState != null) {
            mTracker?.onRestoreInstanceState(savedInstanceState)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mTracker?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun startUI() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        initialization()
        setListeners()
    }

    private fun initialization() {
        loadViews()
        loadData()
    }

    private fun loadViews() {
        window.setBackgroundDrawable(null)

        ViewUtils.initializeRecyclerView(
                recycler_view_exams,
                SelectExamAdapter(),
                null,
                null,
                GridLayoutManager(this, 2),
                GridSpacingItemDecoration(2, ViewUtils.getPixel(R.dimen.margin_8), true),
                null,
                null)

        mTracker = SelectionTracker.Builder<Long>(
                Constants.SelectionIds.EXAM_TYPE,
                recycler_view_exams,
                StableIdKeyProvider(recycler_view_exams),
                BaseItemDetailsLookup(recycler_view_exams),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
                .build()

        getAdapter().tracker = mTracker
        getAdapter().selectionListener = this
    }

    private fun loadData() {
        loadAd()
        loadExamTypes()
        presenter.checkInternetConnectivity()
    }

    private fun loadExamTypes() {
        presenter.attachFirebaseDatabase(this)
    }

    private fun loadAd() {
        mInterstitialAd = InterstitialAd(applicationContext)
        mInterstitialAd?.adUnitId = getString(R.string.before_routine_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun setListeners() {
        setClickListener(text_view_next_steps)

        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                goToHomePage()
            }
        }
    }

    override fun stopUI() {
        presenter.detachFirebaseDatabase()
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    override fun onSelect(size: Int) {

    }

    private fun clickOnItem(item: ExamType) {
        mSelectedExamType = item

        if (mInterstitialAd?.isLoaded!!) {
            mInterstitialAd?.show()
        } else {
            goToHomePage()
        }
    }

    private fun goToHomePage() {
        if (mSelectedExamType != null) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(HomeActivity::class.java.simpleName, mSelectedExamType)
            startActivity(intent)
        }
    }

    private fun getAdapter(): SelectExamAdapter {
        return recycler_view_exams?.adapter as SelectExamAdapter
    }

    override fun onChildChanged(item: ExamType) {
        getAdapter().addItem(item)
    }

    override fun onChildAdded(item: ExamType) {
        getAdapter().addItem(item)
    }

    override fun onChildRemoved(item: ExamType) {
        getAdapter().removeItem(item)
    }

    override fun onChildError(error: Throwable) {
        Timber.e(error)
        ToastUtils.error(getString(R.string.something_went_wrong))
    }

    override fun onInternetConnectivity(state: Boolean) {
        if (!state) {
            ToastUtils.error(getString(R.string.error_you_are_not_connected_to_the_internet))
            ProgressDialogUtils.hideProgressDialog()
        }
    }

    override fun clearTheList() {
        getAdapter().clear()
    }
}