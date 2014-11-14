package ch.epfl.sweng.bohdomp.dialogue.ids;

import android.os.Parcel;
import android.test.AndroidTestCase;

/**
 * Class testing the Dialogue message id
 */
public class ConversationIdTest extends AndroidTestCase {

    private ConversationId mId;
    private final static long LONG_ID = 5;
    private final static int ARRAY_SIZE = 6;

    public void setUp() {
        mId = IdManager.getInstance().newConversationId();
    }

    public void testFromInvalidLong() {
        try {
            mId = ConversationId.fromLong(-1L);
            fail("This is not the Id you are looking for!");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testFromLong() {
        mId = ConversationId.fromLong(LONG_ID);

        assertTrue(mId != null);
        assertTrue(mId.getLong() == LONG_ID);
    }

    public void testParcelability() {
        mId = ConversationId.fromLong(LONG_ID);

        Parcel parcel = Parcel.obtain();
        mId.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);
        ConversationId idFromParcel;
        idFromParcel = ConversationId.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(mId, idFromParcel);
    }

    public void testNewArray() {
        ConversationId[] expectedArrayOfId = new ConversationId[ARRAY_SIZE];

        ConversationId[] createdArrayOfId;
        createdArrayOfId = ConversationId.CREATOR.newArray(ARRAY_SIZE);

        assertEquals(expectedArrayOfId.length, createdArrayOfId.length);
    }
}