package ch.epfl.sweng.bohdomp.dialogue.messaging;

/**
 *  Class representing the body of a text message
 */
public class TextMessageBody implements MessageBody {

    private final String body;

    public TextMessageBody(String bodyParameter) {
        if (bodyParameter != null) {
            this.body = bodyParameter;
        } else {
            throw new IllegalArgumentException("Null argument in TextMessageBody constructor");
        }

    }

    @Override
    public String getMessageBody() {
        return body;
    }

    @Override
    public int getPayloadSize() {
        return body.getBytes().length;
    }
}
