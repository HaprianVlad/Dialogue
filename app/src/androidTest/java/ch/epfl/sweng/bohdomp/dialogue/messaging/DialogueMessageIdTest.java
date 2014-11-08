package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.test.AndroidTestCase;

/**
 * Class testing the Dialogue message id
 */
public class DialogueMessageIdTest extends AndroidTestCase {

    private DialogueMessage.DialogueMessageId messageId;
    private final long id = 5;

    public void testGetNewDialogueMessageId() {
        messageId = DialogueMessage.DialogueMessageId.getNewDialogueMessageId();

        assertTrue(messageId != null);
    }

    public void testGetLong() {
        messageId = DialogueMessage.DialogueMessageId.getNewDialogueMessageId();

        assertTrue(messageId != null);
        assertTrue(messageId.getLong() > 0);
    }

    public void testFromLong() {
        messageId=DialogueMessage.DialogueMessageId.fromLong(id);

        assertTrue(messageId != null);
        assertTrue(messageId.getLong() == id);
    }

    public void testCompare() {

        DialogueMessage.DialogueMessageId messageId1;
        DialogueMessage.DialogueMessageId messageId2;
        DialogueMessage.DialogueMessageId messageId3;

        messageId1=DialogueMessage.DialogueMessageId.fromLong(id);
        messageId2=DialogueMessage.DialogueMessageId.fromLong(id);
        messageId3=DialogueMessage.DialogueMessageId.fromLong(id+1);

        assertTrue(messageId3.compareTo(messageId1) > 0);
        assertTrue(messageId2.compareTo(messageId1) == 0);
    }

    public void testEquals() {

        DialogueMessage.DialogueMessageId messageId1;
        DialogueMessage.DialogueMessageId messageId2;
        DialogueMessage.DialogueMessageId messageId3;

        messageId1=DialogueMessage.DialogueMessageId.fromLong(id);
        messageId2=DialogueMessage.DialogueMessageId.fromLong(id);
        messageId3=DialogueMessage.DialogueMessageId.fromLong(id+1);

        assertTrue(messageId1.equals(messageId2));
        assertFalse(messageId1.equals(messageId3));
        assertFalse(messageId1.equals(null));
        assertTrue(DialogueMessage.DialogueMessageId.getNewDialogueMessageId().equals(
                DialogueMessage.DialogueMessageId.getNewDialogueMessageId()));
        assertFalse(DialogueMessage.DialogueMessageId.getNewDialogueMessageId()
                ==  DialogueMessage.DialogueMessageId.getNewDialogueMessageId());
    }

}
