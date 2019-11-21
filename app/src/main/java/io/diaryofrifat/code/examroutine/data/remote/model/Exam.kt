package io.diaryofrifat.code.examroutine.data.remote.model

import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.utils.helper.Constants

data class Exam(
        @Exclude
        var id: Int = Constants.Default.DEFAULT_INTEGER,
        @Exclude
        var category: ExamType? = null,
        @Exclude
        var subcategory: ExamType? = null,
        @Exclude
        var nativeAd: UnifiedNativeAd? = null,
        @set:PropertyName(Constants.Firebase.SUBJECT_CODE)
        @get:PropertyName(Constants.Firebase.SUBJECT_CODE)
        var subjectCode: String? = null,
        @set:PropertyName(Constants.Firebase.SUBJECT_NAME)
        @get:PropertyName(Constants.Firebase.SUBJECT_NAME)
        var subjectName: String? = null,
        @set:PropertyName(Constants.Firebase.STARTING_TIME)
        @get:PropertyName(Constants.Firebase.STARTING_TIME)
        var startingTime: Long = Constants.Default.DEFAULT_LONG,
        @set:PropertyName(Constants.Firebase.ENDING_TIME)
        @get:PropertyName(Constants.Firebase.ENDING_TIME)
        var endingTime: Long = Constants.Default.DEFAULT_LONG,
        @set:PropertyName(Constants.Firebase.VISITING_URL)
        @get:PropertyName(Constants.Firebase.VISITING_URL)
        var visitingUrl: String? = null
)