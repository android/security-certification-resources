package android.health.connect.aidl;

import android.health.connect.HealthConnectException;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class HealthConnectExceptionParcel implements Parcelable {

    public static final Creator<HealthConnectExceptionParcel> CREATOR =
            new Creator<HealthConnectExceptionParcel>() {
                @Override
                public HealthConnectExceptionParcel createFromParcel(Parcel in) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        return new HealthConnectExceptionParcel(null);
                    } else return null;
                }

                @Override
                public HealthConnectExceptionParcel[] newArray(int size) {
                    return new HealthConnectExceptionParcel[size];
                }
            };

    private final HealthConnectException mHealthConnectException;

    public HealthConnectExceptionParcel(HealthConnectException healthConnectException) {
        mHealthConnectException = healthConnectException;
    }

    public HealthConnectException getHealthConnectException() {
        return mHealthConnectException;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHealthConnectException.getErrorCode());
        dest.writeString(mHealthConnectException.getMessage());
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        if (mHealthConnectException != null) {
            PrintWriter pw = new PrintWriter(sw);
            mHealthConnectException.printStackTrace(pw);
        }
        return "HealthConnectExceptionParcel: " + mHealthConnectException + "\n" + sw;
    }
}
