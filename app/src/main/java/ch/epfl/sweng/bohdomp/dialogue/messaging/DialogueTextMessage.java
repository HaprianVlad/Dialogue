package ch.epfl.sweng.bohdomp.dialogue.messaging;


import android.os.Parcel;
import android.os.Parcelable;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;

/**
 * Class representing a dialogue text message
 */
public final class DialogueTextMessage extends DialogueMessage {


    public static final Parcelable.Creator<DialogueMessage> CREATOR=new Parcelable.Creator<DialogueMessage>() {
        public DialogueTextMessage createFromParcel(Parcel source) {
            return new DialogueTextMessage(source);
        }

        public DialogueTextMessage[] newArray(int size) {
            return new DialogueTextMessage[size];
        }
    };

    public DialogueTextMessage(Contact contactParameter, String textBodyParameter,
                               MessageStatus messageStatusParameter) {
        super(contactParameter, textBodyParameter, messageStatusParameter, false);
    }

    @Override
    public MessageBody newMessageBody(String body) {
        return new TextMessageBody(body);

    }

    private DialogueTextMessage(Parcel source) {
        super(source);
    }


}
