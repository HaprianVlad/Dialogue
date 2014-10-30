package ch.epfl.sweng.bohdomp.dialogue.messaging;

import java.sql.Timestamp;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;

/**
 * Interface representing an message.
 */
public interface Message {

    /**
     * Getter for the contact message
     * @return the contact whose message is
     */
    Contact getContact();

    /**
     * Getter for the body message
     * @return the body message
     */
    String getBody();

    /**
     * Getter for is read status of message
     * @return true if message has been read , false otherwise
     */
    boolean getIsReadStatus();

    /**
     * Getter for the message time stamp
     * @return
     */
    Timestamp getMessageTimeStamp();
}
