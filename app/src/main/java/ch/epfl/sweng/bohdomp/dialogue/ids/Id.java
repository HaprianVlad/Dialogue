package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;

/**
 * Created by BohDomp! on 08.11.14.
 */
abstract class Id implements Identifier, Comparable<Id> {

    private final long mId;

    protected Id(long id) {
        this.mId = id;
    }

    @Override
    public long getLong() {
        return mId;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Id another) {
        return Long.compare(this.mId, another.mId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Id that = (Id) o;
        return mId == that.mId;

    }

    @Override
    public int hashCode() {
        return Long.valueOf(mId).hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mId);
    }
}
