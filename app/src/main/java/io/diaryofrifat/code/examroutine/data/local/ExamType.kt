package io.diaryofrifat.code.examroutine.data.local

import android.os.Parcel
import android.os.Parcelable
import io.diaryofrifat.code.utils.helper.Constants

data class ExamType(val id: String = Constants.Default.DEFAULT_STRING,
                    val examType: String = Constants.Default.DEFAULT_STRING) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(examType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExamType> {
        override fun createFromParcel(parcel: Parcel): ExamType {
            return ExamType(parcel)
        }

        override fun newArray(size: Int): Array<ExamType?> {
            return arrayOfNulls(size)
        }
    }
}