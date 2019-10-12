package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.helper.GridSpacingItemDecoration
import io.diaryofrifat.code.examroutine.ui.home.HomeActivity
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.activity_decision_maker.*
import timber.log.Timber


class DecisionMakerActivity : BaseActivity<DecisionMakerMvpView, DecisionMakerPresenter>(),
        DecisionMakerMvpView {

    private lateinit var mBinding: ActivityDecisionMakerBinding
    private var mInterstitialAd: InterstitialAd? = null
    private var mSelectedExamType: ExamType? = null
    private var mItemDecoration: GridSpacingItemDecoration? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_decision_maker

    override fun getActivityPresenter(): DecisionMakerPresenter {
        return DecisionMakerPresenter()
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
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
        window.setBackgroundDrawable(null)

        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            mBinding.customStatusBarView.setBackgroundColor(ViewUtils.getColor(R.color.white))
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.customStatusBarView.setBackgroundColor(ViewUtils.getColor(R.color.colorPrimaryDark))
        } else {
            // Do nothing for Jelly bean and Kitkat devices
        }

        mItemDecoration = GridSpacingItemDecoration(2, ViewUtils.dpToPx(16).toInt(), true)

        ViewUtils.initializeRecyclerView(
                mBinding.recyclerViewExamTypes,
                ExamTypeAdapter(),
                object : ItemClickListener<ExamType> {
                    override fun onItemClick(view: View, item: ExamType) {
                        clickOnItem(item)
                    }
                },
                null,
                GridLayoutManager(this, 2),
                mItemDecoration,
                null,
                null)
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

    private fun getAdapter(): ExamTypeAdapter {
        return recycler_view_exam_types.adapter as ExamTypeAdapter
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