package io.diaryofrifat.code.examroutine.ui.selectexam

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.examroutine.ui.base.helper.ProgressDialogUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SelectExamPresenter : BasePresenter<SelectExamMvpView>() {
    private var mExamTypeReference: DatabaseReference? = null
    private var mExamTypeListener: ChildEventListener? = null

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

    fun getExamTypes(context: Context) {
        val dialog = ProgressDialogUtils.on().showProgressDialog(context)

        if (mExamTypeListener == null) {
            mExamTypeListener = object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onErrorGettingExamType(error.toException())
                    dialog?.dismiss()
                }

                override fun onChildMoved(data: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(data: DataSnapshot, p1: String?) {
                    /*if (data.hasChildren()) {
                        var count: Long = 0

                        ProgressDialogUtils.on().showProgressDialog(context)

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onChildChanged(ExamType(data.key!!, it))
                            }

                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.on().hideProgressDialog()
                            }
                        }
                    }*/
                }

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    if (data.hasChildren()) {

                        var count: Long = 0

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onExamTypesAdded(
                                        ExamType(count, it, item.toString())
                                )
                            }

                            count++

                            if (count == data.childrenCount) {
                                dialog?.dismiss()
                            }
                        }
                    }
                }

                override fun onChildRemoved(data: DataSnapshot) {
                    /*if (data.hasChildren()) {
                        var count: Long = 0

                        ProgressDialogUtils.on().showProgressDialog(context)

                        for (item in data.children) {
                            item.key?.let {
                                mvpView?.onChildRemoved(ExamType(data.key!!, it))
                            }

                            count++

                            if (count == data.childrenCount) {
                                ProgressDialogUtils.on().hideProgressDialog()
                            }
                        }
                    }*/
                }

            }
        }

        if (mExamTypeReference == null && mExamTypeListener != null) {
            mExamTypeReference = DatabaseService.getExamTypes(mExamTypeListener!!)
        }
    }

    override fun detachView() {
        super.detachView()
        detachExamTypeListener()
    }

    fun detachExamTypeListener() {
        if (mExamTypeListener != null) {
            mExamTypeReference?.removeEventListener(mExamTypeListener!!)
        }
    }
}