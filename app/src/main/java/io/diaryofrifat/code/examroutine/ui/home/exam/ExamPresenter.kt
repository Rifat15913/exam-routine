package io.diaryofrifat.code.examroutine.ui.home.exam

import android.text.TextUtils
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ExamPresenter : BasePresenter<ExamMvpView>() {

    // Exam list
    val mExamList: MutableList<Exam> = ArrayList()

    // Database reference
    private var mExamReference: Query? = null

    // Database listener
    private var mExamListener: ValueEventListener? = null

    fun checkInternetConnectivity() {
        val checkInternetConnectivity: Single<Boolean> = ReactiveNetwork.checkInternetConnectivity()

        compositeDisposable.add(checkInternetConnectivity
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.onInternetConnectivity(it)
                }, {
                    Timber.e(it)
                }))
    }

    fun getExams(category: ExamType, subcategory: ExamType?) {
        if (mExamListener == null) {
            mExamListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onError(error.toException())
                }


                override fun onDataChange(data: DataSnapshot) {
                    val list: MutableList<Exam> = ArrayList()

                    if (data.hasChildren()) {
                        for ((count, item) in data.children.withIndex()) {
                            if (item.key != null && item.value != null) {
                                val exam = item.getValue(Exam::class.java).apply {
                                    this?.id = count
                                    this?.category = category
                                    this?.subcategory = subcategory
                                }

                                exam?.let {
                                    list.add(it)
                                }
                            }
                        }
                    }

                    mExamList.clear()
                    mExamList.addAll(list)
                    mvpView?.onGettingExams(list)
                }
            }
        }

        if (mExamReference == null) {
            mExamReference = DatabaseService.getExams(category, subcategory)
        }
    }

    fun attachExamListener() {
        if (mExamListener != null) {
            mExamReference?.addValueEventListener(mExamListener!!)
        }
    }

    fun detachExamListener() {
        if (mExamListener != null) {
            mExamReference?.removeEventListener(mExamListener!!)
        }
    }

    fun getMatchedExamList(query: String): List<Exam> {
        if (TextUtils.isEmpty(query)) {
            return mExamList
        }

        val matchedExamList: MutableList<Exam> = ArrayList()

        mExamList.forEach {
            if (it.subjectName != null && it.subjectName!!.contains(query)) {
                matchedExamList.add(it)
            }
        }

        return matchedExamList
    }
}