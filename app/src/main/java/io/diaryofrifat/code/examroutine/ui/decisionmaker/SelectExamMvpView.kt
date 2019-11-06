package io.diaryofrifat.code.examroutine.ui.decisionmaker

import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface SelectExamMvpView : MvpView {
    fun onChildChanged(item: ExamType)
    fun onChildAdded(item: ExamType)
    fun onChildRemoved(item: ExamType)
    fun onChildError(error: Throwable)
    fun onInternetConnectivity(state: Boolean)
    fun clearTheList()
}