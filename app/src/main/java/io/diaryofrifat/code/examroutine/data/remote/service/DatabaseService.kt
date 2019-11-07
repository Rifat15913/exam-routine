package io.diaryofrifat.code.examroutine.data.remote.service

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    fun getExamTypes(listener: ChildEventListener): DatabaseReference? {
        return mFirebaseDatabase.reference
                .child(DataUtils.getString(R.string.path_exam_types))
                .apply {
                    addChildEventListener(listener)
                }
    }
}