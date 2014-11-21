package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class testing the TextMessageBody
 */
public class TextMessageBodyTest extends AndroidTestCase {

    private static final String BODY = "Hello world!";
    private static final int ARRAY_SIZE = 6;

    private  MessageBody mMessageBody;

    public void setUp() {
        mMessageBody = new TextMessageBody(BODY);
    }

    public void testParcelRoundTrip() {

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
        assertEquals(BODY, mMessageBody.getMessageBody());
    }

    public void testDescribeContents() {
        assertEquals(0, mMessageBody.describeContents());
    }

    public void testNewArray() {
        TextMessageBody[] newArray = TextMessageBody.CREATOR.newArray(ARRAY_SIZE);

        assertEquals(ARRAY_SIZE, newArray.length);
    }
}
