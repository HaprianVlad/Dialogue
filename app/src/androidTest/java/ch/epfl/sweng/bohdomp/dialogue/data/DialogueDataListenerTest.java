package ch.epfl.sweng.bohdomp.dialogue.data;

import android.content.Context;
import android.os.Bundle;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import java.util.List;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

/**
 * Class testing a dialogue data listener
 */
public final class DialogueDataListenerTest extends MockTestCase {
    private Contact mContact1;
    private Contact mContact2;

    private DialogueMessage mMessage;

    // TODO make this local
    private boolean mHasBeenCalled;

    private Context mContext;


    public void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();

        ContactFactory mContactFactory = new ContactFactory(mContext);

        mContact1 = mContactFactory.contactFromNumber("0762677108");
        mContact2 = mContactFactory.contactFromNumber("0762654278");

        String body = "Hello";

        mMessage = new DialogueTextMessage(mContact1, null, null, body, DialogueMessage.MessageDirection.INCOMING);

        mHasBeenCalled = false;
    }


    public void testListenerOnMessageAdded() {
        mHasBeenCalled = false;

        DialogueDataListener listener = new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                mHasBeenCalled = true;
            }
        };

        DefaultDialogData.getInstance().addListener(listener);
        DefaultDialogData.getInstance().addMessageToConversation(mMessage);
        assertTrue(mHasBeenCalled);

        mHasBeenCalled=false;

        DefaultDialogData.getInstance().removeListener(listener);
        DefaultDialogData.getInstance().addMessageToConversation(mMessage);
        assertFalse(mHasBeenCalled);

    }

    public void testListenerOnConversationAdded() {
        mHasBeenCalled = false;

        DialogueDataListener listener = new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                mHasBeenCalled = true;
            }
        };

        DefaultDialogData.getInstance().addListener(listener);
        DefaultDialogData.getInstance().createOrGetConversation(mContact2);
        assertTrue(mHasBeenCalled);

        mHasBeenCalled=false;

        DefaultDialogData.getInstance().removeListener(listener);
        DefaultDialogData.getInstance().createOrGetConversation(mContact1);
        assertFalse(mHasBeenCalled);

    }

    public void testListenerOnConversationRemoved() {

        mHasBeenCalled = false;

        DialogueDataListener listener = new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                mHasBeenCalled = true;
            }
        };

        Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(mContact1);


        DefaultDialogData.getInstance().addListener(listener);
        DefaultDialogData.getInstance().removeConversation(conversation.getId());
        assertTrue(mHasBeenCalled);



        DefaultDialogData.getInstance().createOrGetConversation(mContact1);

        mHasBeenCalled=false;

        DefaultDialogData.getInstance().removeListener(listener);
        DefaultDialogData.getInstance().removeConversation(conversation.getId());
        assertFalse(mHasBeenCalled);

    }


    public void testListenerOnBundleRestore() {
        mHasBeenCalled = false;

        DialogueDataListener listener = new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                mHasBeenCalled = true;
            }
        };

        Bundle b = DefaultDialogData.getInstance().createBundle();

        DefaultDialogData.getInstance().createOrGetConversation(mContact1);

        DefaultDialogData.getInstance().addListener(listener);
        DefaultDialogData.getInstance().restoreFromBundle(b);
        assertTrue(mHasBeenCalled);

        mHasBeenCalled=false;

        DefaultDialogData.getInstance().removeListener(listener);
        DefaultDialogData.getInstance().restoreFromBundle(b);
        assertFalse(mHasBeenCalled);


    }

    public void testSetMessageStatusNullMessageValidStatus() {
        try {
            DefaultDialogData.getInstance().setMessageStatus(null, DialogueMessage.MessageStatus.SENT);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // OK
        }
    }

    public void testSetMessageStatusValidMessageNullStatus() {
        try {
            DefaultDialogData.getInstance().setMessageStatus(mMessage, null);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // OK
        }
    }

    public void testSetMessageStatusNullMessageNullStatus() {
        try {
            DefaultDialogData.getInstance().setMessageStatus(null, null);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // OK
        }
    }

    public void testSetMessageStatusValidMessageValidStatus() {
        DialogueData data = DefaultDialogData.getInstance();
        data.addMessageToConversation(mMessage);

        data.setMessageStatus(mMessage, DialogueMessage.MessageStatus.SENT);

        List<DialogueMessage> messageList =  data.createOrGetConversation(mMessage.getContact()).getMessages();

        // Retrieve message we just added, the last one
        DialogueMessage message = messageList.get(messageList.size() - 1);

        assertEquals(DialogueMessage.MessageStatus.SENT, message.getStatus());
    }

    public void testSetMessageStatusListener() throws InvalidNumberException {
        Contact contact = new ContactFactory(mContext).contactFromNumber("1234");

        DialogueMessage message = new DialogueTextMessage(contact, null, null, "Hello World!",
                DialogueMessage.MessageDirection.OUTGOING);

        mHasBeenCalled = false;

        DialogueDataListener listener = new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                mHasBeenCalled = true;
            }
        };

        DefaultDialogData.getInstance().addListener(listener);

        DefaultDialogData.getInstance().setMessageStatus(mMessage, DialogueMessage.MessageStatus.SENT);

        assertFalse("Should not set an non-existing message", mHasBeenCalled);

        mHasBeenCalled = false;

        DefaultDialogData.getInstance().addMessageToConversation(message);

        assertTrue("Should notify when setting a status", mHasBeenCalled);
    }
}
