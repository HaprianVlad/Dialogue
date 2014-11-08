package ch.epfl.sweng.bohdomp.dialogue.messaging;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;

/**
 * Abstract class representing an message. This class is mutable.
 */
public abstract class DialogueMessage {

    /**
     * Enumeration representing the state of a message
     */
    public static enum MessageStatus {
        INCOMING, OUTGOING
    }

    private final Contact contact;
    private final MessageBody messageBody;
    private final Timestamp timestamp;
    private final DialogueMessageId messageId;
    private final MessageStatus messageStatus;
    private final List<Contact.ChannelType> allowedChannels = new ArrayList<Contact.ChannelType>();

    private boolean isReadStatus;


    public DialogueMessage(Contact contactParameter, String messageBodyParameter,
                           MessageStatus messageStatusParameter, Contact.ChannelType channelTypeParameter) {
        if (contactParameter == null) {
            throw new NullArgumentException("contactParameter");
        }
        if (messageBodyParameter == null) {
            throw new NullArgumentException("messageBodyParameter");
        }
        if (messageStatusParameter == null) {
            throw new NullArgumentException("messageStatusParameter");
        }
        if (channelTypeParameter == null) {
            throw new NullArgumentException("channelTypeParameter");
        }

        this.contact = contactParameter;
        this.messageBody = newMessageBody(messageBodyParameter);
        this.timestamp = new Timestamp((new Date()).getTime());
        this.messageId = IdManager.getInstance().getNewDialogueMessageId();
        this.isReadStatus = false;
        this.messageStatus = messageStatusParameter;
        this.allowedChannels.add(channelTypeParameter);

    }


    /**
     * Getter for the contact message
     * @return the contact whose message is
     */
    public Contact getContact() {
        //FIXME:Contact should be immutable
        return contact;
    }

    /**
     * Getter for the body message
     * @return the body message
     */
    public MessageBody getBody() {
        return messageBody;
    }

    /**
     * Getter for is read status of message
     * @return true if message has been read , false otherwise
     */
    public boolean getIsReadStatus() {
        return isReadStatus;
    }

    /**
     * Getter for the message time stamp
     * @return the time stamp of the message
     */
    public Timestamp getMessageTimeStamp() {
        return timestamp;
    }

    /**
     * Getter for the message status
     * @return the message status. incoming or outgoing
     */
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    /**
     * Getter for the message id
     * @return the message id
     */
    public DialogueMessageId getMessageId() {
        return messageId;
    }

    /**
     * Sets a message as read
     */
    public void setMessageAsRead() {
        //FIXME: Decide if we need a listener somewhere or when enter in converstation all messages are read
        isReadStatus = false;
    }



    /**
     * Method that returns the allowed channels where we can send the message
     * @return the list of allowed channels
     */
    public  List<Contact.ChannelType> getAllowedChannels() {
        return new ArrayList<Contact.ChannelType>(allowedChannels);
    }

    /**
     * Factory method for constructing the corespondent type of message body
     * @param body the string we will put in the body
     * @return the created MessageBody
     */
    public abstract MessageBody newMessageBody(String body);

}
