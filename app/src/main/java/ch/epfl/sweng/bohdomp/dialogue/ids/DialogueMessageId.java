package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BohDomp! on 08.11.14.
 */
public final class DialogueMessageId extends Id {

    private DialogueMessageId(long id) {
        super(id);
    }

    public static DialogueMessageId fromLong(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Given long to construct ConversationId is smaller than 0!");
        }

        return new DialogueMessageId(id);
    }

    public static final Parcelable.Creator<DialogueMessageId> CREATOR
        = new Parcelable.Creator<DialogueMessageId>() {
            public DialogueMessageId createFromParcel(Parcel in) {
                return new DialogueMessageId(in.readLong());
            }

            public DialogueMessageId[] newArray(int size) {
                return new DialogueMessageId[size];
            }
        };
}