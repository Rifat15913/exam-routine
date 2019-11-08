package io.diaryofrifat.code.examroutine.ui.home.exam

import androidx.recyclerview.widget.LinearLayoutManager
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.examroutine.ui.home.container.HomeActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.fragment_exam.*
import timber.log.Timber

class ExamFragment : BaseFragment<ExamMvpView, ExamPresenter>(), ExamMvpView {

    private var mCategory: ExamType? = null
    private var mSubcategory: ExamType? = null
    private val mItemDecoration = LinearMarginItemDecoration(
            ViewUtils.getPixel(R.dimen.margin_8)
    )

    override val layoutId: Int
        get() = R.layout.fragment_exam

    override fun getFragmentPresenter(): ExamPresenter {
        return ExamPresenter()
    }

    override fun startUI() {
        extractDataFromArguments()
        initialize()
        loadData()
    }

    private fun initialize() {
        ViewUtils.initializeRecyclerView(
                recycler_view_exam,
                ExamAdapter(),
                null,
                null,
                LinearLayoutManager(mContext),
                null,
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
            presenter.getExams(mContext, mCategory!!, mSubcategory)
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
    }

    override fun onError(error: Throwable) {
        Timber.e(error)
        ToastUtils.nativeLong(getString(R.string.something_went_wrong))
    }

    override fun onStart() {
        super.onStart()
        recycler_view_exam?.addItemDecoration(mItemDecoration)
        presenter.attachExamListener()
    }

    override fun onStop() {
        super.onStop()
        recycler_view_exam?.removeItemDecoration(mItemDecoration)
        presenter.detachExamListener()
    }
}