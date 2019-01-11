package io.diaryofrifat.code.examroutine.ui.main

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.remote.retrophoto.RetroPhoto
import io.diaryofrifat.code.examroutine.databinding.ActivityMainBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MainActivity : BaseActivity<MainMvpView, MainPresenter>(), MainMvpView {
    /**
     * Fields
     * */
    private lateinit var mBinding: ActivityMainBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun getActivityPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun shouldShowBackIconAtToolbar(): Boolean {
        return false
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityMainBinding

        ViewUtils.initializeRecyclerView(
                mBinding.recyclerViewPhotos,
                MainAdapter(),
                null,
                null,
                LinearLayoutManager(this),
                LinearMarginItemDecoration(ViewUtils.getPixel(R.dimen.margin_16)),
                null,
                DefaultItemAnimator()
        )

        presenter.compositeDisposable.add(getAdapter().dataChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.d(it.toString())
                }, {
                    Timber.e(it)
                }))

/*        NotificationUtils.buildNotification(1234,
                NotificationUtils.NotificationType.DEFAULT,
                R.drawable.ic_check_white_48dp,
                "Hello Notification",
                "Hello Notification Caption",
                R.raw.notification)*/

        presenter.test()
    }

    private fun getRecyclerView(): RecyclerView {
        return mBinding.recyclerViewPhotos
    }

    private fun getAdapter(): MainAdapter {
        return getRecyclerView().adapter as MainAdapter
    }

    override fun onFetchingData(list: List<RetroPhoto>) {
        getAdapter().clear()
        getAdapter().addItems(list)
    }

    override fun stopUI() {

    }

    override fun onClick(view: View) {
        when (view.id) {

        }
    }
}
