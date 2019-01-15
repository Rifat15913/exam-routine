package io.diaryofrifat.code.utils.libs.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

    /**
     * Fields
     * */
    private val mFirebaseDatabase = FirebaseDatabase.getInstance()
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

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

    fun initAnalytics(context: Context) {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
        }

        mFirebaseAnalytics?.setAnalyticsCollectionEnabled(true)
    }

    fun getFirebaseAnalytics(): FirebaseAnalytics? {
        return mFirebaseAnalytics
    }
}