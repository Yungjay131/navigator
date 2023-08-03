package dev.joshuasylvanus.navigator_demo_app.splash

import android.os.Parcel
import android.os.Parcelable

data class Args(val dummyString:String): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    companion object CREATOR : Parcelable.Creator<Args> {
        override fun createFromParcel(parcel: Parcel): Args {
            return Args(parcel)
        }

        override fun newArray(size: Int): Array<Args?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dummyString)
    }

    override fun describeContents(): Int {
        return 0
    }
}