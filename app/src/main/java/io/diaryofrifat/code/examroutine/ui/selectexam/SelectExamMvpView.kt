package io.diaryofrifat.code.examroutine.ui.selectexam

import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface SelectExamMvpView : MvpView {
    fun onChildChanged(item: ExamType)
    fun onExamTypesAdded(item: ExamType)
    fun onChildRemoved(item: ExamType)
    fun onErrorGettingExamType(error: Throwable)
    fun onInternetConnectivity(isConnected: Boolean)
    fun clearTheList()
}