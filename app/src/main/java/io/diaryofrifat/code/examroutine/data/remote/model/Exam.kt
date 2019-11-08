package io.diaryofrifat.code.examroutine.data.remote.model

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
) {
    /*constructor() : this(
            Constants.Default.DEFAULT_INTEGER,
            null,
            null,
            null,
            null,
            Constants.Default.DEFAULT_LONG,
            Constants.Default.DEFAULT_LONG,
            null
    )*/
}