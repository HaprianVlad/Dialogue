package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcelable;

/**
 * Interface describing an identifier.
 */
public interface Identifier extends Parcelable {
    long getLong();
}
