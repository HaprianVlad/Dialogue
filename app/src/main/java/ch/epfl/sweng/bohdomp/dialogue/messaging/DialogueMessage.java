package ch.epfl.sweng.bohdomp.dialogue.messaging;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;

/**
 * Abstract class representing an message. This class is mutable.
 */
public abstract class DialogueMessage {

    /**
     * Class representing an dialogue message id and ensures its uniqueness.This class is immutable.
     */
    public static final class DialogueMessageId{

        private static long lastId = 0;

        public static DialogueMessageId getNewDialogueMessageId() {
            lastId += 1;
            return  new DialogueMessageId(lastId);
        }

        private final long id;

        private DialogueMessageId(long idParameter) {
            this.id = idParameter;
        }

        public long getId() {
            return id;
        }
    }
    /**
     * Enumeration representing the state of a message
     */
    public static enum MessageStatus {
        INCOMING, OUTGOING
    }

    private final static int MAX_PAYLOAD_SIZE = 50;

    private final Contact contact;
    private final MessageBody messageBody;
    private final Timestamp timestamp;
    private final DialogueMessageId dialogueMessageId;
    private final MessageStatus messageStatus;

    private boolean isReadStatus;


    public DialogueMessage(Contact contactParameter, String messageBodyParameter,
                           MessageStatus messageStatusParameter) {
        if (contactParameter != null && messageBodyParameter != null) {
            this.contact = contactParameter;
            this.messageBody = newMessageBody(messageBodyParameter);
            this.timestamp = new Timestamp((new Date()).getTime());
            this.dialogueMessageId = DialogueMessageId.getNewDialogueMessageId();
            this.isReadStatus = false;
            this.messageStatus = messageStatusParameter;

        } else {
            throw new IllegalArgumentException("Null arguments in DialogueMessage constructor");
        }

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
    public abstract List<Contact.ChannelType> getAllowedChannels();

   /**
    * Factory method for constructing the corespondent type of message body
    * @param body the string we will put in the body
    * @return the created MessageBody
    */
    public abstract MessageBody newMessageBody(String body);

}
