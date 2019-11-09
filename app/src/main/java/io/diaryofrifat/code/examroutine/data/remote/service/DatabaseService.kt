package io.diaryofrifat.code.examroutine.data.remote.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils

object DatabaseService {

    /**
     * Fields
     * */
    private val mFirebaseDatabase = FirebaseDatabase.getInstance()

    init {
        mFirebaseDatabase.setPersistenceEnabled(true)
    }

    /**
     * This method provides database reference using path of it
     *
     * @param path database path
     * @return [DatabaseReference] reference of the database
     * */
    fun getDatabaseReference(path: String): DatabaseReference? {
        return mFirebaseDatabase.reference.child(path)
    }

    /**
     * This method provides all the exam types
     *
     * @return [DatabaseReference] reference of the exam types
     * */
    fun getExamTypes(): DatabaseReference? {
        return mFirebaseDatabase.reference
                .child(DataUtils.getString(R.string.path_exam_types))
    }

    /**
     * This method provides all the subcategory keys
     * @param categoryKey key of the category
     * @return [DatabaseReference] reference of the subcategory keys
     * */
    fun getSubcategories(categoryKey: String): DatabaseReference? {
        return mFirebaseDatabase.reference
                .child(DataUtils.getString(R.string.path_subcategories) + categoryKey)
    }

    /**
     * This method provides all the exams
     * @param category category of the exam
     * @param subcategory subcategory of the exam
     * @return [DatabaseReference] reference of the exams
     * */
    fun getExams(category: ExamType, subcategory: ExamType?): Query? {
        return mFirebaseDatabase.reference
                .child(DataUtils.getString(R.string.path_exams)
                        + category.examTypeKey
                        +
                        if (subcategory != null) {
                            Constants.Common.SLASH + subcategory.examTypeKey
                        } else {
                            Constants.Default.DEFAULT_STRING
                        }
                ).orderByChild(Constants.Firebase.STARTING_TIME)
    }
}