package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.telephony.SmsMessage;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class testing the TextMessageBody
 */
public class TextMessageBodyTest extends AndroidTestCase {

    private  MessageBody messageBody;
    private final String body = "Hello world!";

    public void testNullMessageBody() {
        try {
            this.messageBody = new TextMessageBody(null);
            fail("Exception should have been trown");
        } catch (NullArgumentException e) {
            //succes
        }
    }

    public void testTooLongMessageBody() {
        String longBody = "";
        for (int i=0; i <= SmsMessage.MAX_USER_DATA_BYTES; i++) {
            longBody += "a";
        }
        assertTrue(longBody.getBytes().length > SmsMessage.MAX_USER_DATA_BYTES);
        try {
            this.messageBody = new TextMessageBody(longBody);
            fail("Exception should have been thrown");

        } catch (IllegalArgumentException e) {
            //success
        }
    }

    public void testCorrectMessageBody() {
        this.messageBody = new TextMessageBody(body);
        assertTrue(messageBody.getMessageBody() == body);
    }
}
