package com.example.firebase_curd.MVVM.Model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        userId = parcel.readString() ?: "",
        name = parcel.readString() ?: "",
        email = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}