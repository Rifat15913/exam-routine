package io.diaryofrifat.code.examroutine.data.local

import android.os.Parcel
import android.os.Parcelable
import io.diaryofrifat.code.utils.helper.Constants

data class Exam(val id: Int = -1,
                val subjectName: String = Constants.Default.DEFAULT_STRING,
                val subjectCode: String = Constants.Default.DEFAULT_STRING,
                val time: Long = -1) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readLong())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(subjectName)
        dest?.writeString(subjectCode)
        dest?.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exam> {
        override fun createFromParcel(parcel: Parcel): Exam {
            return Exam(parcel)
        }

        override fun newArray(size: Int): Array<Exam?> {
            return arrayOfNulls(size)
        }
    }
}