package com.miniproj.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class RecordBlock(
    val dateOfVisit: String,
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val prescribedMeds: String,
    val nextDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateOfVisit)
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeString(diagnosis)
        parcel.writeString(prescribedMeds)
        parcel.writeString(nextDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecordBlock> {
        override fun createFromParcel(parcel: Parcel): RecordBlock {
            return RecordBlock(parcel)
        }

        override fun newArray(size: Int): Array<RecordBlock?> {
            return arrayOfNulls(size)
        }
    }
}