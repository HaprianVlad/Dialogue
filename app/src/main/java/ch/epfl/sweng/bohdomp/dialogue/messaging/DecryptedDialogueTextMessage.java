package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.os.Parcelable;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.PhoneNumber;

/**
 * Decrypted version of a DialogueTextMessage
 */
public class DecryptedDialogueTextMessage extends DialogueMessage {
    public DecryptedDialogueTextMessage(Contact contact, ChannelType channel, PhoneNumber phoneNumber,
                                        String messageBody, MessageDirection messageDirection) {
        super(contact, channel, phoneNumber, messageBody, messageDirection, false);
    }

    public DecryptedDialogueTextMessage(Parcel in) {
        super(in);
    }

    @Override
    public MessageBody newBody(String body) {
        return new TextMessageBody(body);
    }

    @Override
    public boolean isEncrypted() {
        return true;
    }

    public static final Parcelable.Creator<DecryptedDialogueTextMessage> CREATOR =
            new Parcelable.Creator<DecryptedDialogueTextMessage>() {
                public DecryptedDialogueTextMessage createFromParcel(Parcel source) {
                    return new DecryptedDialogueTextMessage(source);
                }

                public DecryptedDialogueTextMessage[] newArray(int size) {
                    return new DecryptedDialogueTextMessage[size];
                }
            };
}
