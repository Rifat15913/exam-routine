package io.diaryofrifat.code.examroutinebd.ui.main

import io.diaryofrifat.code.examroutinebd.data.remote.retrophoto.RetroPhoto
import io.diaryofrifat.code.examroutinebd.ui.base.callback.MvpView

interface MainMvpView : MvpView {
    fun onFetchingData(list: List<RetroPhoto>)
}