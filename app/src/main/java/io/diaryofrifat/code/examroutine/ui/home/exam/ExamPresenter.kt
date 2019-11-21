package io.diaryofrifat.code.examroutine.ui.home.exam

import android.text.TextUtils
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.data.remote.service.DatabaseService
import io.diaryofrifat.code.examroutine.ui.base.component.BasePresenter
import io.diaryofrifat.code.examroutine.ui.base.getString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class ExamPresenter : BasePresenter<ExamMvpView>() {

    companion object {
        const val MAX_NUMBER_OF_NATIVE_ADS = 5
    }

    // Exam list
    private val mExamList: MutableList<Exam> = ArrayList()

    // Database reference
    private var mExamReference: Query? = null

    // Database listener
    private var mExamListener: ValueEventListener? = null

    // The AdLoader used to load ads.
    private var mAdLoader: AdLoader? = null

    // List of native ads that have been successfully loaded.
    private val mNativeAdList: MutableList<UnifiedNativeAd> = ArrayList()

    private fun loadNativeAds() {
        if (activity != null && mAdLoader == null) {
            mAdLoader = AdLoader.Builder(activity, getString(R.string.native_exam_ad_unit_id))
                    .forUnifiedNativeAd {
                        mNativeAdList.add(it)
                        if (mAdLoader != null && !mAdLoader!!.isLoading) {
                            val list: MutableList<Exam> = ArrayList()
                            mNativeAdList.forEach { ad ->
                                list.add(Exam(nativeAd = ad))
                            }

                            mvpView?.onGettingAds(list, mExamList)
                        }
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            // Do nothing for now. If needed, we will handle error here
                            if (mAdLoader != null && !mAdLoader!!.isLoading) {
                                val list: MutableList<Exam> = ArrayList()
                                mNativeAdList.forEach { ad ->
                                    list.add(Exam(nativeAd = ad))
                                }

                                mvpView?.onGettingAds(list, mExamList)
                            }
                        }
                    })
                    .build()
        }

        if (mNativeAdList.isEmpty()) {
            mAdLoader?.loadAds(AdRequest.Builder().build(), MAX_NUMBER_OF_NATIVE_ADS)
        } else {
            val list: MutableList<Exam> = ArrayList()
            mNativeAdList.forEach { ad ->
                list.add(Exam(nativeAd = ad))
            }

            mvpView?.onGettingAds(list, mExamList)
        }
    }

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
                    loadNativeAds()
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
        val matchedExamList: MutableList<Exam> = ArrayList()

        if (TextUtils.isEmpty(query)) {
            matchedExamList.addAll(mExamList)
        } else {
            mExamList.forEach {
                if (it.subjectName != null && it.subjectName!!.contains(query)) {
                    matchedExamList.add(it)
                }
            }
        }

        if (mNativeAdList.isNotEmpty()) {
            val offset = 3
            var index = 0
            var adCount = 0
            var multiplier = 1
            val examItemCount = matchedExamList.size

            index += (offset * multiplier)
            multiplier++

            while (index < examItemCount) {
                matchedExamList.add(index, Exam(nativeAd = mNativeAdList[adCount]))
                index += (offset * multiplier++)

                if (adCount < mNativeAdList.size - 1) {
                    adCount++
                }
            }
        }

        return matchedExamList
    }
}