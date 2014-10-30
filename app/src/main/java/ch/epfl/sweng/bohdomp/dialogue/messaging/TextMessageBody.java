package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.telephony.SmsMessage;


/**
 *  Class representing the body of a text message
 */
public class TextMessageBody implements MessageBody {

    public final static int MAX_MESSAGE_SIZE = SmsMessage.MAX_USER_DATA_BYTES;
    private final String body;

    public TextMessageBody(String bodyParameter) {
        if (bodyParameter != null && bodyParameter.getBytes().length < MAX_MESSAGE_SIZE) {
            this.body = bodyParameter;
        } else {
            throw new IllegalArgumentException("Null argument in TextMessageBody constructor or too long message");
        }

    }

    @Override
    public String getMessageBody() {
        return body;
    }



}
