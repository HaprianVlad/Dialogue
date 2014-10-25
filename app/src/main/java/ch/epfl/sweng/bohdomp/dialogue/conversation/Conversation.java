package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;

/**
 * Class representing a Dialogue conversation
 */
public class Conversation {

    private final long mId;
    private Set<Contact> mContacts;
    private List<DialogueSmsMessage> mMessages;

    private int mMsgCount;
    private Timestamp mTimeStamp;

    private boolean mHasUnread;

    public Conversation(long id, Set<Contact> contacts, List<DialogueSmsMessage> messages,
                        int msgCount, Timestamp timeStamp, boolean hasUnread) {
        this.mId = id;
        this.mContacts = contacts;
        this.mMessages = messages;
        this.mMsgCount = msgCount;
        this.mTimeStamp = timeStamp;
        this.mHasUnread = hasUnread;
    }


    public long getId() {
        return mId;
    }

    public Timestamp getTimeStamp() {
        return mTimeStamp;
    }

}
