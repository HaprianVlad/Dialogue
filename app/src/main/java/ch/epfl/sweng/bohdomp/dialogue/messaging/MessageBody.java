package ch.epfl.sweng.bohdomp.dialogue.messaging;

/**
 * Interface representing the body of a message
 */
public interface MessageBody{

    /**
     * Getter for the message body
     * @return the string representing the message body
     */
    String getMessageBody();

    /**
     * Methods that computes the size of a body message
     * @return number of bytes of the message body
     */
    int getPayloadSize();
}
