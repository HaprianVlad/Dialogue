package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;

/**
 * Class testing the Dialogue message id
 */
public class DialogueMessageIdTest extends AndroidTestCase {

    private DialogueMessageId messageId;
    private final long id = 5;

    public void testGetNewDialogueMessageId() {
        messageId = IdManager.getInstance().getNewDialogueMessageId();

        assertTrue(messageId != null);
    }

    public void testGetLong() {
        messageId = IdManager.getInstance().getNewDialogueMessageId();

        assertTrue(messageId != null);
        assertTrue(messageId.getLong() > 0);
    }

    public void testFromLong() {
        messageId = DialogueMessageId.fromLong(id);

        assertTrue(messageId != null);
        assertTrue(messageId.getLong() == id);
    }

    public void testCompare() {

        DialogueMessageId messageId1;
        DialogueMessageId messageId2;
        DialogueMessageId messageId3;

        messageId1 = DialogueMessageId.fromLong(id);
        messageId2 = DialogueMessageId.fromLong(id);
        messageId3 = DialogueMessageId.fromLong(id+1);

        assertTrue(messageId3.compareTo(messageId1) > 0);
        assertTrue(messageId2.compareTo(messageId1) == 0);
    }

    public void testEquals() {

        DialogueMessageId messageId1;
        DialogueMessageId messageId2;
        DialogueMessageId messageId3;

        messageId1 = DialogueMessageId.fromLong(id);
        messageId2 = DialogueMessageId.fromLong(id);
        messageId3 = DialogueMessageId.fromLong(id+1);

        assertTrue(messageId1.equals(messageId2));
        assertFalse(messageId1.equals(messageId3));
        assertFalse(messageId1.equals(null));
    }

}
