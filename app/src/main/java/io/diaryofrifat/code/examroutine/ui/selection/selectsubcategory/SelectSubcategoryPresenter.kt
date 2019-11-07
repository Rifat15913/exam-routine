package io.diaryofrifat.code.examroutine.ui.selection.selectsubcategory

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.examroutine.ui.base.helper.ProgressDialogUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SelectSubcategoryPresenter : BasePresenter<SelectSubcategoryMvpView>() {
    private var mExamTypeReference: DatabaseReference? = null
    private var mExamTypeListener: ValueEventListener? = null

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
            mExamTypeListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onErrorGettingExamTypes(error.toException())
                    dialog?.dismiss()
                }

                override fun onDataChange(data: DataSnapshot) {
                    val list: MutableList<ExamType> = ArrayList()

                    if (data.hasChildren()) {
                        for ((count, item) in data.children.withIndex()) {
                            if (item.key != null && item.value != null) {
                                list.add(ExamType(count, item.key!!, item.value.toString()))
                            }
                        }

                        mvpView?.onGettingExamTypes(list)
                        dialog?.dismiss()
                    }
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