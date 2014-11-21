package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class testing the TextMessageBody
 */
public class TextMessageBodyTest extends AndroidTestCase {

    private  MessageBody mMessageBody;
    private static final String BODY = "Hello world!";

    public void testParcelRoundTrip() {
        mMessageBody = new TextMessageBody(BODY);

        Parcel parcel = Parcel.obtain();
        mMessageBody.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        MessageBody messageFromParcel = TextMessageBody.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(mMessageBody.getMessageBody(), messageFromParcel.getMessageBody());
    }

    public void testNullMessageBody() {
        try {
            this.mMessageBody = new TextMessageBody(null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            // success
        }
    }

    public void testCorrectMessageBody() {
        this.mMessageBody = new TextMessageBody(BODY);
        assertEquals(BODY, mMessageBody.getMessageBody());
    }
}
