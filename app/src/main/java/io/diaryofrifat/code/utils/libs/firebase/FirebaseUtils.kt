package io.diaryofrifat.code.utils.libs.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

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
}