package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.os.Parcelable;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class defining a conversation id. This class is immutable
 */
public final class ConversationId extends Id {
    private ConversationId(long id) {
        super(id);
    }

    public static ConversationId fromLong(long id) {
        Contract.throwIfArg(id < 0, "Given long to construct ConversationId is smaller than 0!");

        return new ConversationId(id);
    }

    public static final Parcelable.Creator<ConversationId> CREATOR
        = new Parcelable.Creator<ConversationId>() {
            public ConversationId createFromParcel(Parcel in) {
                Contract.throwIfArgNull(in, "in");

                return new ConversationId(in.readLong());
            }

            public ConversationId[] newArray(int size) {
                return new ConversationId[size];
            }
        };
}
