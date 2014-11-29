package ch.epfl.sweng.bohdomp.dialogue.data;

import android.content.Context;
import android.os.Bundle;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
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
    private boolean mHasBeenCalled;


    public void setUp() throws Exception {
        super.setUp();

        Context mContext = getInstrumentation().getTargetContext();

        ContactFactory mContactFactory = new ContactFactory(mContext);

        mContact1 = mContactFactory.contactFromNumber("0762677108");
        mContact2 = mContactFactory.contactFromNumber("0762654278");


        String body = "Hello";
        mMessage = new DialogueTextMessage(mContact1, null, null, body, DialogueMessage.MessageStatus.INCOMING);


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
}
