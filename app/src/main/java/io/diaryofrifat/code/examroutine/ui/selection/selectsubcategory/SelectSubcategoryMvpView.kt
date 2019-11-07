package io.diaryofrifat.code.examroutine.ui.selection.selectsubcategory

import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface SelectSubcategoryMvpView : MvpView {
    fun onInternetConnectivity(isConnected: Boolean)
}