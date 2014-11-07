package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.telephony.SmsMessage;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;


/**
 *  Class representing the body of a text message
 */
public class TextMessageBody implements MessageBody {

    public final static int MAX_MESSAGE_SIZE = SmsMessage.MAX_USER_DATA_BYTES;
    private final String body;

    public TextMessageBody(String bodyParameter) {
        if (bodyParameter == null) {
            throw new NullArgumentException(bodyParameter);
        }
        if (bodyParameter.getBytes().length < MAX_MESSAGE_SIZE) {
            throw new IllegalArgumentException("Too big message body!");
        }

        this.body = bodyParameter;

    }

    @Override
    public String getMessageBody() {
        return body;
    }



}
