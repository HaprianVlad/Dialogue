package ch.epfl.sweng.bohdomp.dialogue.messaging;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;

/**
 * Class representing a dialogue text message
 */
public class DialogueTextMessage extends DialogueMessage {
    public DialogueTextMessage(Contact contactParameter, String textBodyParameter,
                               MessageStatus messageStatusParameter) {
        super(contactParameter, textBodyParameter, messageStatusParameter, false);
    }

    @Override
    public MessageBody newMessageBody(String body) {
        return new TextMessageBody(body);

    }
}
