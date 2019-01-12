package io.diaryofrifat.code.examroutine.data.local

import io.diaryofrifat.code.utils.helper.Constants

data class Exam(val id: Int = -1, val subjectName: String = Constants.Default.DEFAULT_STRING,
                val subjectCode: String = Constants.Default.DEFAULT_STRING, val time: Long = -1)