package io.diaryofrifat.code.examroutine.ui.routine

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils.Companion.getString
import io.diaryofrifat.code.utils.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.helper.TimeUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import io.diaryofrifat.code.utils.libs.firebase.FirebaseUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RoutinePresenter : BasePresenter<RoutineMvpView>() {
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
        val examType: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE,
                Constants.Default.DEFAULT_STRING)!!

        val databaseExamPath: String?

        when (examType) {
            getString(R.string.psc) -> {
                databaseExamPath = getString(R.string.path_psc)
            }

            getString(R.string.jsc) -> {
                databaseExamPath = getString(R.string.path_jsc)
            }

            getString(R.string.ssc) -> {
                databaseExamPath = getString(R.string.path_ssc)
            }

            getString(R.string.hsc) -> {
                databaseExamPath = getString(R.string.path_hsc)
            }

            else -> {
                ToastUtils.error(getString(R.string.something_went_wrong))
                ProgressDialogUtils.hideProgressDialog()
                return true
            }
        }

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
                        mvpView?.clearTheList()
                        var count: Long = 0

                        ProgressDialogUtils.showProgressDialog(context)

                        for (item in data.children) {
                            mvpView?.onChildChanged(item.getValue(Exam::class.java)!!)
                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.hideProgressDialog()
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
                                ProgressDialogUtils.hideProgressDialog()
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