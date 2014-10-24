package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;

/**
 * Class representing a Dialogue conversation
 */
public class Conversation {
    private long mId;
    private Set<Contact> mContacts;
    private List<DialogueSmsMessage> mMessages;

    private int mMsgCount;
    private long mTimeStamp;

    private boolean mHasUnread;


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

}
