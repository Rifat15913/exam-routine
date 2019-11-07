package io.diaryofrifat.code.examroutine.data.local

import android.os.Parcel
import android.os.Parcelable

data class ExamType(var id: Int,
                    val examTypeKey: String,
                    val examTypeTitle: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(examTypeKey)
        parcel.writeString(examTypeTitle)
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