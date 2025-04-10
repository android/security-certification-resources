package android.health.connect.backuprestore;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Objects;

public final class BackupChange implements Parcelable {

    // A record ID that uniquely identifies the specific record this change refers to.

    private final String mRecordId;

    private final boolean mIsDeletion;

    // Only present if isDeletion is false.
    // The data is returned as bytes rather than records to keep the data opaque from the client.
    // As long as the client doesn't parse the data, it doesn't know what type of data this is.
    private final byte[] mData;

    /**
     * @param recordId A record ID that uniquely identifies the specific record this change refers
     *     to.
     * @param isDeletion Whether this change is a deletion.
     * @param data Only present if isDeletion is false. The data is returned as bytes rather than
     *     records to keep the data opaque from the client. The caller shouldn't make any
     *     assumptions about the format of the data.
     */
    public BackupChange(String recordId, boolean isDeletion, byte[] data) {
        mRecordId = recordId;
        mIsDeletion = isDeletion;
        mData = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
        /*if (!(o instanceof BackupChange that)) return false;
        return mIsDeletion == that.mIsDeletion
                && mRecordId.equals(that.mRecordId)
                && Arrays.equals(mData, that.mData);*/
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mRecordId, mIsDeletion);
        result = 31 * result + Arrays.hashCode(mData);
        return result;
    }

    private BackupChange(Parcel in) {
        mRecordId = in.readString();
        mIsDeletion = in.readByte() != 0;
        mData = in.readBlob();
    }

    public static final Parcelable.Creator<BackupChange> CREATOR =
            new Creator<>() {
                @Override
                public BackupChange createFromParcel(Parcel in) {
                    return new BackupChange(in);
                }

                @Override
                public BackupChange[] newArray(int size) {
                    return new BackupChange[size];
                }
            };

    /** Returns the ID of the record that was changed. */
    public String getRecordId() {
        return mRecordId;
    }

    /** Returns whether this change is a deletion. */
    public boolean isDeletion() {
        return mIsDeletion;
    }

    /**
     * Returns the record that was changed.
     *
     * <p>Only present if isDeletion is false.
     */
    public byte[] getData() {
        return mData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecordId);
        dest.writeByte((byte) (mIsDeletion ? 1 : 0));
        dest.writeBlob(mData);
    }
}