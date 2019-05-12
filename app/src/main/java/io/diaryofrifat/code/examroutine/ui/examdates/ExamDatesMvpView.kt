package io.diaryofrifat.code.examroutine.ui.examdates

import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface ExamDatesMvpView : MvpView {
    fun onChildChanged(item: Exam)
    fun onChildAdded(item: Exam)
    fun onChildRemoved(item: Exam)
    fun onChildError(error: Throwable)
    fun onInternetConnectivity(state: Boolean)
    fun clearTheList()
    fun setToolbarTitle(year: String)
}