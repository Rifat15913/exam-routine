package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.libs.firebase.FirebaseUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DecisionMakerPresenter : BasePresenter<DecisionMakerMvpView>() {
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
                    ProgressDialogUtils.hideProgressDialog()
                }))
    }

    fun attachFirebaseDatabase(context: Context): Boolean {
        ProgressDialogUtils.showProgressDialog(context)

        val databaseExamPath = DataUtils.getString(R.string.path_exam_types)

        if (mFirebaseDatabaseReference == null) {
            mFirebaseDatabaseReference = FirebaseUtils.getDatabaseReference(databaseExamPath)
        }

        if (mChildEventListener == null) {
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onChildError(error.toException())
                    ProgressDialogUtils.hideProgressDialog()
                }

                override fun onChildMoved(data: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(data: DataSnapshot, p1: String?) {
                    if (data.hasChildren()) {
                        var count: Long = 0

                        ProgressDialogUtils.showProgressDialog(context)

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onChildChanged(ExamType(data.key!!, it))
                            }

                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.hideProgressDialog()
                            }
                        }
                    }
                }

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    if (data.hasChildren()) {
                        var count: Long = 0

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onChildAdded(ExamType(data.key!!, it))
                            }

                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.hideProgressDialog()
                            }
                        }
                    }
                }

                override fun onChildRemoved(data: DataSnapshot) {
                    if (data.hasChildren()) {
                        var count: Long = 0

                        ProgressDialogUtils.showProgressDialog(context)

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onChildRemoved(ExamType(data.key!!, it))
                            }

                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.hideProgressDialog()
                            }
                        }
                    }
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