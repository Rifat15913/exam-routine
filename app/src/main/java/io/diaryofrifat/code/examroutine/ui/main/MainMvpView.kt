package io.diaryofrifat.code.examroutine.ui.main

import io.diaryofrifat.code.examroutine.data.remote.retrophoto.RetroPhoto
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface MainMvpView : MvpView {
    fun onFetchingData(list: List<RetroPhoto>)
}