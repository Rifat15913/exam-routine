package io.diaryofrifat.code.examroutine.ui.examdates

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.examroutine.ui.base.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.helper.DataUtils.Companion.getString
import io.diaryofrifat.code.utils.helper.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ExamDatesPresenter : BasePresenter<ExamDatesMvpView>() {
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null

    fun checkInternetConnectivity() {
        val checkInternetConnectivity: Single<Boolean> = ReactiveNetwork.checkInternetConnectivity()

        compositeDisposable.add(checkInternetConnectivity
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.onInternetConnectivity(it)
                }, {
                    Timber.e(it)
                    ProgressDialogUtils.on().hideProgressDialog()
                }))
    }

    fun attachFirebaseDatabase(context: Context, examType: ExamType): Boolean {
        ProgressDialogUtils.on().showProgressDialog(context)

        val databaseExamPath: String = getString(R.string.path_exam_types) + examType.id

        if (mFirebaseDatabaseReference == null) {
            mFirebaseDatabaseReference = DatabaseService.getDatabaseReference(databaseExamPath)
        }

        if (mChildEventListener == null) {
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onChildError(error.toException())
                    ProgressDialogUtils.on().hideProgressDialog()
                }

                override fun onChildMoved(data: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(data: DataSnapshot, p1: String?) {
                    if (data.hasChildren()) {
                        mvpView?.clearTheList()
                        var count: Long = 0

                        ProgressDialogUtils.on().showProgressDialog(context)

                        for (item in data.children) {
                            mvpView?.onChildChanged(item.getValue(Exam::class.java)!!)
                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.on().hideProgressDialog()
                                mvpView?.setToolbarTitle(TimeUtils.getYear(
                                        (item.getValue(Exam::class.java)!!.time)).toString())
                            }
                        }
                    }
                }

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    if (data.hasChildren()) {
                        var count: Long = 0

                        for (item in data.children) {
                            mvpView?.onChildAdded(item.getValue(Exam::class.java)!!)
                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.on().hideProgressDialog()
                                mvpView?.setToolbarTitle(TimeUtils.getYear(
                                        (item.getValue(Exam::class.java)!!.time)).toString())
                            }
                        }
                    }
                }

                override fun onChildRemoved(data: DataSnapshot) {
                    mvpView?.clearTheList()
                }

            }
        }

        mFirebaseDatabaseReference?.addChildEventListener(mChildEventListener!!)
        return false
    }

    fun detachFirebaseDatabase() {
        if (mChildEventListener != null) {
            mFirebaseDatabaseReference?.removeEventListener(mChildEventListener!!)
        }
    }
}