package io.diaryofrifat.code.examroutine.ui.selection.selectexam

import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface SelectExamMvpView : MvpView {
    fun onGettingExamTypes(list: List<ExamType>)
    fun onGettingSubcategories(subcategoryList: List<ExamType>)
    fun onError(error: Throwable)
    fun onInternetConnectivity(isConnected: Boolean)
}