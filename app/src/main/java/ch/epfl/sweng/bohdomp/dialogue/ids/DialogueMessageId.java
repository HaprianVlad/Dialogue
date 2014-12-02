package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.os.Parcelable;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class defining a dialogue message id.
 */
public final class DialogueMessageId extends Id {
    private DialogueMessageId(long id) {
        super(id);
    }

    public static DialogueMessageId fromLong(long id) {
        Contract.throwIfArg(id < 0, "Given long to construct ConversationId is smaller than 0!");

        return new DialogueMessageId(id);
    }

    public static final Parcelable.Creator<DialogueMessageId> CREATOR
        = new Parcelable.Creator<DialogueMessageId>() {
            public DialogueMessageId createFromParcel(Parcel in) {
                Contract.throwIfArgNull(in, "in");

                return new DialogueMessageId(in.readLong());
            }

            public DialogueMessageId[] newArray(int size) {
                return new DialogueMessageId[size];
            }
        };
}