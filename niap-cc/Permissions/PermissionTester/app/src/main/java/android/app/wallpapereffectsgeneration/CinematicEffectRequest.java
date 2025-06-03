/*
 * Copyright (C) 2022 The Android Open Source Project
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

package android.app.wallpapereffectsgeneration;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * A {@link CinematicEffectRequest} is the data class having all the information
 * passed to wallpaper effects generation service.
 *
 * @hide
 */
public final class CinematicEffectRequest implements Parcelable {
    /**
     * Unique id of a cienmatic effect generation task.
     */
    private String mTaskId;

    /**
     * The bitmap to generate cinematic effect from.
     */
    private Bitmap mBitmap;

    private CinematicEffectRequest(Parcel in) {
        this.mTaskId = in.readString();
        this.mBitmap = Bitmap.CREATOR.createFromParcel(in);
    }

    /**
     * Constructor with task id and bitmap.
     */
    public CinematicEffectRequest( String taskId,Bitmap bitmap) {
        mTaskId = taskId;
        mBitmap = bitmap;
    }

    /**
     * Returns the task id.
     */

    public String getTaskId() {
        return mTaskId;
    }

    /**
     * Returns the bitmap of this request.
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CinematicEffectRequest that = (CinematicEffectRequest) o;
        return mTaskId.equals(that.mTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mTaskId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTaskId);
        mBitmap.writeToParcel(out, flags);
    }


    public static final Creator<CinematicEffectRequest> CREATOR =
            new Creator<CinematicEffectRequest>() {
                @Override
                public CinematicEffectRequest createFromParcel(Parcel in) {
                    return new CinematicEffectRequest(in);
                }

                @Override
                public CinematicEffectRequest[] newArray(int size) {
                    return new CinematicEffectRequest[size];
                }
            };
}