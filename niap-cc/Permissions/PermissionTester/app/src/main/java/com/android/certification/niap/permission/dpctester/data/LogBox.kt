package com.android.certification.niap.permission.dpctester.data
/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

data class LogBox(
    var id: Long = Random.nextLong(),
    var name: String? ="none",
    var description: String?="none",
    var type: String? = "none",
    var childs: MutableList<LogBox> = mutableListOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        //For the child the lists should be blank
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LogBox> {
        override fun createFromParcel(parcel: Parcel): LogBox {
            return LogBox(parcel)
        }

        override fun newArray(size: Int): Array<LogBox?> {
            return arrayOfNulls(size)
        }
    }

}