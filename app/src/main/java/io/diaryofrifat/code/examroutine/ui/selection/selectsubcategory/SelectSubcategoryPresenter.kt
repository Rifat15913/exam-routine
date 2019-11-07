package io.diaryofrifat.code.examroutine.ui.selection.selectsubcategory

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SelectSubcategoryPresenter : BasePresenter<SelectSubcategoryMvpView>() {

    fun checkInternetConnectivity() {
        val checkInternetConnectivity: Single<Boolean> = ReactiveNetwork.checkInternetConnectivity()

        compositeDisposable.add(checkInternetConnectivity
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.onInternetConnectivity(it)
                }, {
                    Timber.e(it)
                }))
    }
}