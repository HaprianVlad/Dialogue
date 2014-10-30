package ch.epfl.sweng.bohdomp.dialogue.messaging;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;

/**
 * Class representing a dialogue text message
 */
public class DialogueTextMessage extends DialogueMessage {

    public DialogueTextMessage(Contact contactParameter, String textBodyParameter,
                               MessageStatus messageStatusParameter) {
        super(contactParameter, textBodyParameter, messageStatusParameter);
    }

    @Override
    public List<Contact.ChannelType> getAllowedChannels() {
        return new ArrayList<Contact.ChannelType>(Contact.ChannelType.SMS);
    }

    @Override
    public MessageBody newMessageBody(String body) {
        return new TextMessageBody(body);
    }
}
