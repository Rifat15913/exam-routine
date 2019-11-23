package io.diaryofrifat.code.examroutine.ui.selection.selectexam

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SelectExamPresenter : BasePresenter<SelectExamMvpView>() {
    // Database reference
    private var mExamTypeReference: DatabaseReference? = null
    private var mSubcategoryReference: DatabaseReference? = null

    // Database listener
    private var mExamTypeListener: ValueEventListener? = null
    private var mSubcategoryListener: ValueEventListener? = null

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

    fun getExamTypes() {
        if (mExamTypeListener == null) {
            mExamTypeListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onError(error.toException())
                }

                override fun onDataChange(data: DataSnapshot) {
                    val list: MutableList<ExamType> = ArrayList()

                    if (data.hasChildren()) {
                        for ((count, item) in data.children.withIndex()) {
                            if (item.key != null && item.value != null) {
                                list.add(ExamType(count, item.key!!, item.value.toString()))
                            }
                        }
                    }

                    mvpView?.onGettingExamTypes(list)
                }
            }
        }

        if (mExamTypeReference == null) {
            mExamTypeReference = DatabaseService.getExamTypes()
        }
    }

    fun attachExamTypeListener() {
        if(mExamTypeListener != null){
            mExamTypeReference?.addValueEventListener(mExamTypeListener!!)
        }
    }

    fun detachExamTypeListener() {
        if (mExamTypeListener != null) {
            mExamTypeReference?.removeEventListener(mExamTypeListener!!)
        }
    }

    fun getSubcategories(categoryKey: String) {
        if (mSubcategoryListener == null) {
            mSubcategoryListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mvpView?.onError(error.toException())
                    detachSubcategoryListener()
                    mSubcategoryReference = null
                }

                override fun onDataChange(data: DataSnapshot) {
                    val subcategoryList: MutableList<ExamType> = ArrayList()

                    if (data.hasChildren()) {
                        for ((count, item) in data.children.withIndex()) {
                            if (item.key != null && item.value != null) {
                                subcategoryList.add(
                                        ExamType(count, item.key!!, item.value.toString())
                                )
                            }
                        }
                    }

                    mvpView?.onGettingSubcategories(subcategoryList)
                    detachSubcategoryListener()
                    mSubcategoryReference = null
                }
            }
        }

        if (mSubcategoryReference == null) {
            mSubcategoryReference = DatabaseService.getSubcategories(categoryKey)
        }

        attachSubcategoryListener()
    }

    private fun attachSubcategoryListener() {
        if (mSubcategoryListener != null) {
            mSubcategoryReference?.addValueEventListener(mSubcategoryListener!!)
        }
    }

    private fun detachSubcategoryListener() {
        if (mSubcategoryListener != null) {
            mSubcategoryReference?.removeEventListener(mSubcategoryListener!!)
            mSubcategoryListener = null
        }
    }

    override fun detachView() {
        super.detachView()
        detachSubcategoryListener()
    }
}