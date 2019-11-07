package io.diaryofrifat.code.examroutine.data.remote.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.diaryofrifat.code.examroutine.R
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
    private fun getDatabaseReference(path: String): DatabaseReference? {
        return mFirebaseDatabase.reference.child(path)
    }

    /**
     * This method provides all the exam types
     *
     * @param listener to get all the exam types
     * @return [DatabaseReference] reference of the exam types
     * */
    fun getExamTypes(listener: ValueEventListener): DatabaseReference? {
        return mFirebaseDatabase.reference
                .child(DataUtils.getString(R.string.path_exam_types))
                .apply {
                    addValueEventListener(listener)
                }
    }
}