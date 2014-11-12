package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Tests the DataMessageBody.
 */
public class DataMessageBodyTest extends AndroidTestCase {
    private final String mBody = "Hello world!";
    private MessageBody mMessageBody;

    // TODO create valid URI

//    public void testParcelRoundTrip() {
//        mMessageBody = new DataMessageBody(mBody);
//
//        Parcel parcel = Parcel.obtain();
//        mMessageBody.writeToParcel(parcel, 0);
//
//        parcel.setDataPosition(0); // reset parcel for reading
//
//        MessageBody messageFromParcel = TextMessageBody.CREATOR.createFromParcel(parcel);
//
//        parcel.recycle();
//
//        assertEquals(mMessageBody.getMessageBody(), messageFromParcel.getMessageBody());
//    }

    public void testNullMessageBody() {
        try {
            this.mMessageBody = new DataMessageBody(null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            // success
        }
    }

//    public void testCorrectMessageBody() {
//        this.mMessageBody = new DataMessageBody(mBody);
//        assertEquals(mBody, mMessageBody.getMessageBody());
//    }
}