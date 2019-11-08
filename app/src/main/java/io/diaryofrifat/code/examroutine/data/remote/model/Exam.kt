package io.diaryofrifat.code.examroutine.data.remote.model

import com.google.firebase.database.PropertyName
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.utils.helper.Constants

class Exam {
    var id: Long = Constants.Default.DEFAULT_LONG
    var category: ExamType? = null
    var subcategory: ExamType? = null
    @PropertyName(Constants.Firebase.SUBJECT_CODE)
    var subjectCode: String? = null
    @PropertyName(Constants.Firebase.SUBJECT_NAME)
    var subjectName: String? = null
    @PropertyName(Constants.Firebase.STARTING_TIME)
    var startingTime: Long = Constants.Default.DEFAULT_LONG
    @PropertyName(Constants.Firebase.ENDING_TIME)
    var endingTime: Long = Constants.Default.DEFAULT_LONG
    @PropertyName(Constants.Firebase.VISITING_URL)
    var visitingUrl: String? = null

    /*constructor(subjectCode: String?,
                subjectName: String,
                startingTime: Long,
                endingTime: Long,
                visitingUrl: String?
    ) : this() {
        this.subjectCode = subjectCode
        this.subjectName = subjectName
        this.startingTime = startingTime
        this.endingTime = endingTime
        this.visitingUrl = visitingUrl
    }*/
}