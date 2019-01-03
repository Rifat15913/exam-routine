package io.diaryofrifat.code.boardroutinebd.ui.main

import io.diaryofrifat.code.boardroutinebd.data.remote.retrophoto.RetroPhoto
import io.diaryofrifat.code.boardroutinebd.ui.base.callback.MvpView

interface MainMvpView : MvpView {
    fun onFetchingData(list: List<RetroPhoto>)
}