package io.diaryofrifat.code.examroutine.ui.home.exam

import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.callback.MvpView

interface ExamMvpView : MvpView {
    fun onInternetConnectivity(isConnected: Boolean)
    fun onGettingExams(list: List<Exam>)
    fun onError(error: Throwable)
}