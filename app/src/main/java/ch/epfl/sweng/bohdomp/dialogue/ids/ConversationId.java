package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BohDomp! on 08.11.14.
 */
public final class ConversationId extends Id {

    private ConversationId(long id) {
        super(id);
    }

    public static ConversationId fromLong(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Given long to construct ConversationId is smaller than 0!");
        }

        return new ConversationId(id);
    }

    public static final Parcelable.Creator<ConversationId> CREATOR
        = new Parcelable.Creator<ConversationId>() {
            public ConversationId createFromParcel(Parcel in) {
                return new ConversationId(in.readLong());
            }

            public ConversationId[] newArray(int size) {
                return new ConversationId[size];
            }
        };
}
