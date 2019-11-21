package io.diaryofrifat.code.examroutine.ui.home.exam

import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.examroutine.ui.base.makeItGone
import io.diaryofrifat.code.examroutine.ui.base.makeItVisible
import io.diaryofrifat.code.examroutine.ui.base.toTitleCase
import io.diaryofrifat.code.examroutine.ui.home.container.HomeActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.TimeUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_exam.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ExamFragment : BaseFragment<ExamMvpView, ExamPresenter>(), ExamMvpView {

    private var mCategory: ExamType? = null
    private var mSubcategory: ExamType? = null
    private val mItemDecoration = LinearMarginItemDecoration(
            ViewUtils.getPixel(R.dimen.margin_16),
            ViewUtils.getPixel(R.dimen.margin_16),
            ViewUtils.getPixel(R.dimen.margin_16),
            ViewUtils.getPixel(R.dimen.margin_16)
    )

    override val layoutId: Int
        get() = R.layout.fragment_exam

    override fun getFragmentPresenter(): ExamPresenter {
        return ExamPresenter()
    }

    override fun startUI() {
        extractDataFromArguments()
        initialize()
        setListeners()
        loadData()
    }

    private fun setListeners() {
        setClickListener(chip_change_exam)
    }

    private fun initialize() {
        text_view_title?.text = mCategory?.examTypeTitle?.toTitleCase(false)

        ViewUtils.initializeRecyclerView(
                recycler_view_exam,
                ExamAdapter(),
                null,
                null,
                LinearLayoutManager(mContext),
                mItemDecoration,
                null,
                null
        )
    }

    private fun getAdapter(): ExamAdapter {
        return recycler_view_exam?.adapter as ExamAdapter
    }

    private fun extractDataFromArguments() {
        val args = arguments
        if (args != null
                && args.containsKey(Constants.IntentKey.CATEGORY)
        ) {
            mCategory = args.getParcelable(Constants.IntentKey.CATEGORY)

            if (args.containsKey(Constants.IntentKey.SUBCATEGORY)) {
                mSubcategory = args.getParcelable(Constants.IntentKey.SUBCATEGORY)
            }
        } else {
            (activity as HomeActivity).onBackPressed()
        }
    }

    private fun loadData() {
        if (mCategory != null) {
            presenter.checkInternetConnectivity()
            presenter.getExams(mCategory!!, mSubcategory)
        }
    }

    override fun stopUI() {

    }

    override fun onInternetConnectivity(isConnected: Boolean) {
        if (!isConnected) {
            ToastUtils.nativeLong(getString(R.string.error_you_are_not_connected_to_the_internet))
        }
    }

    override fun onGettingExams(list: List<Exam>) {
        getAdapter().clear()
        getAdapter().addItems(list)

        if (list.isEmpty()) {
            return
        }

        var hasScrolled = false
        for (i in list.indices) {
            val item = list[i]
            if (DateUtils.isToday(item.startingTime)) {
                recycler_view_exam?.smoothScrollToPosition(i)
                hasScrolled = true
            }
        }

        if (!hasScrolled) {
            for (i in list.indices) {
                val item = list[i]
                if (item.startingTime > TimeUtils.currentTime()) {
                    recycler_view_exam?.smoothScrollToPosition(i)
                    hasScrolled = true
                    break
                }
            }
        }

        if (!hasScrolled) {
            recycler_view_exam?.smoothScrollToPosition(list.size - 1)
        }
    }

    override fun onGettingAds(adList: List<Exam>, examList: List<Exam>) {
        if (adList.isEmpty()) {
            return
        }

        val offset = 3
        var index = 0
        var adCount = 0
        var multiplier = 1
        val examItemCount = examList.size

        index += (offset * multiplier)
        multiplier++

        while (index < examItemCount) {
            getAdapter().addItem(adList[adCount], index)
            index += (offset * multiplier++)
            if (adCount < adList.size - 1) {
                adCount++
            }
        }

        val list = getAdapter().getItems()
        var hasScrolled = false
        for (i in list.indices) {
            val item = list[i]
            if (DateUtils.isToday(item.startingTime)) {
                recycler_view_exam?.smoothScrollToPosition(i)
                hasScrolled = true
            }
        }

        if (!hasScrolled) {
            for (i in list.indices) {
                val item = list[i]
                if (item.startingTime > TimeUtils.currentTime()) {
                    recycler_view_exam?.smoothScrollToPosition(i)
                    hasScrolled = true
                    break
                }
            }
        }

        if (!hasScrolled) {
            recycler_view_exam?.smoothScrollToPosition(list.size - 1)
        }
    }

    override fun onError(error: Throwable) {
        Timber.e(error)
        ToastUtils.nativeLong(getString(R.string.something_went_wrong))
    }

    override fun onStart() {
        super.onStart()
        addContinuousSubscriptions()
    }

    private fun addContinuousSubscriptions() {
        presenter.attachExamListener()

        presenter.compositeDisposable.add(
                RxSearchView.queryTextChanges(search_view_exam)
                        .skip(1)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .map {
                            presenter.getMatchedExamList(it.toString())
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            onGettingExams(it)
                        }, {
                            Timber.e(it)
                            onError(it)
                        })
        )

        presenter.compositeDisposable.add(getAdapter().dataChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it == 0) {
                        text_view_empty_placeholder?.makeItVisible()
                    } else {
                        text_view_empty_placeholder?.makeItGone()
                    }
                }, {
                    Timber.e(it)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        presenter.detachExamListener()
    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view.id) {
            R.id.chip_change_exam -> {
                (activity as HomeActivity).onBackPressed()
            }
        }
    }
}