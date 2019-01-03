package io.diaryofrifat.code.boardroutinebd.ui.main

import io.diaryofrifat.code.boardroutinebd.data.BaseRepository
import io.diaryofrifat.code.boardroutinebd.ui.base.component.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainPresenter : BasePresenter<MainMvpView>() {
    fun test() {
        compositeDisposable.add(
                BaseRepository.on().getAllPhotosFromServer()
                        .map { it.subList(0, 9) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Timber.d(it.size.toString())
                            mvpView?.onFetchingData(it)
                        }, {
                            Timber.d(it)
                        }))
    }
}