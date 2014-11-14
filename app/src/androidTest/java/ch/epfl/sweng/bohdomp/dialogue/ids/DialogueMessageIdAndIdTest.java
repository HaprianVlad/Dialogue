package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.test.AndroidTestCase;

/**
 * Class testing the Dialogue message id
 */
public class DialogueMessageIdAndIdTest extends AndroidTestCase {

    private DialogueMessageId mId;
    private final static long LONG_ID = 5;
    private final static int ARRAY_SIZE = 6;

    public void setUp() {
        mId = IdManager.getInstance().newDialogueMessageId();
    }

    public void testGetLong() {
        long idAsLong = mId.getLong();

        assertTrue(idAsLong > 0);
        assertTrue(idAsLong <= System.currentTimeMillis());

    }

    public void testFromInvalidLong() {
        try {
            mId = DialogueMessageId.fromLong(-1L);
            fail("This is not the Id you are looking for!");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testFromLong() {
        mId = DialogueMessageId.fromLong(LONG_ID);

        assertTrue(mId != null);
        assertTrue(mId.getLong() == LONG_ID);
    }

    public void testCompare() {

        DialogueMessageId messageId1 = DialogueMessageId.fromLong(LONG_ID);
        DialogueMessageId messageId2 = DialogueMessageId.fromLong(LONG_ID);
        DialogueMessageId messageId3 = DialogueMessageId.fromLong(LONG_ID +1);

        assertTrue(messageId3.compareTo(messageId1) > 0);
        assertTrue(messageId2.compareTo(messageId1) == 0);
        assertTrue(messageId2.compareTo(messageId3) < 0);
    }

    public void testEquals() {

        DialogueMessageId messageId1 = DialogueMessageId.fromLong(LONG_ID);
        DialogueMessageId messageId2 = DialogueMessageId.fromLong(LONG_ID);
        DialogueMessageId messageId3 = DialogueMessageId.fromLong(LONG_ID +1);

        assertEquals(messageId1, messageId1);
        assertTrue(messageId1.equals(messageId2));
        assertFalse(messageId1.equals(messageId3));
        assertFalse(messageId1.equals(new Long(0)));
        assertFalse(messageId1.equals(null));
    }

    public void testParcelability() {
        mId = DialogueMessageId.fromLong(LONG_ID);

        Parcel parcel = Parcel.obtain();
        mId.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);
        DialogueMessageId idFromParcel;
        idFromParcel = DialogueMessageId.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(mId, idFromParcel);
    }

    public void testDescribeContents() {

        assertEquals(0, mId.describeContents());
    }

    public void testHashCode() {
        long hashCodeFromLong = Long.valueOf(LONG_ID).hashCode();
        mId = DialogueMessageId.fromLong(LONG_ID);

        assertEquals(hashCodeFromLong, mId.hashCode());
    }

    public void testNewArray() {
        DialogueMessageId[] expectedArrayOfId = new DialogueMessageId[ARRAY_SIZE];

        DialogueMessageId[] createdArrayOfId;
        createdArrayOfId = DialogueMessageId.CREATOR.newArray(ARRAY_SIZE);

        assertEquals(expectedArrayOfId.length, createdArrayOfId.length);
    }
}
