package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BohDomp on 08.11.14.
 */
public interface Identifier extends Parcelable {

    long getLong();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel out, int flags);

}
