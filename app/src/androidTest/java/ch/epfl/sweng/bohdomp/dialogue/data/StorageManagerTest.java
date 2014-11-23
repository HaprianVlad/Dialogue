package ch.epfl.sweng.bohdomp.dialogue.data;

import android.content.Context;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

/**
 * Class that tests the storage manager
 */
public class StorageManagerTest extends MockTestCase {
    private static final String CONTACT_NUMBER = "12345667889";
    private static final String MSG_BODY = "HELLO";

    private Context mContext;
    private Contact mContact;
    private DialogueMessage mMessage;
    private Conversation mConversation;
    private DialogueData mData;
    private StorageManager mStorageManager;
    private int nbOfConversations;

    public void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();
        mData = DefaultDialogData.getInstance();
        mStorageManager = new StorageManager(mContext);

        mContact = new ContactFactory(mContext).contactFromNumber(CONTACT_NUMBER);
        mMessage = new DialogueTextMessage(mContact, null, null, MSG_BODY, DialogueMessage.MessageDirection.INCOMING);
        mConversation = mData.createOrGetConversation(mContact);
        mConversation.addMessage(mMessage);
        nbOfConversations = mData.getConversations().size();
    }

    public void testNullArgument() {
        try {
            mStorageManager = new StorageManager(null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //all is ok
        }
    }

    public void testSaveAndRestore() {
        mStorageManager.saveData();
        mStorageManager.retreiveData();

        assertNotNull("Null conversations", mData.getConversations());
        assertEquals(nbOfConversations, mData.getConversations().size());

        assertNotNull("Null conversation", mData.getConversation(mConversation.getId()));
        assertEquals(mConversation.getMessageCount(), mData.getConversation(mConversation.getId()).getMessageCount());

        int size = mData.getConversation(mConversation.getId()).getMessages().size();

        assertTrue(size > 0);

        assertEquals(mConversation.getMessages().size(), size);
        assertEquals(mMessage.getBody().getMessageBody(),
                mData.getConversation(mConversation.getId()).getMessages().get(size-1).getBody().getMessageBody());
        assertEquals(mMessage.getContact(),
                mData.getConversation(mConversation.getId()).getMessages().get(size-1).getContact());
        assertEquals(mMessage.getId(),
                mData.getConversation(mConversation.getId()).getMessages().get(size-1).getId());

    }

}
