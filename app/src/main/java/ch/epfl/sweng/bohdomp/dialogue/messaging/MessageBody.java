package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcelable;

/**
 * Interface representing the body of a message
 */
public interface MessageBody extends Parcelable {

    /**
     * Getter for the message body
     *
     * @return the string representing the message body
     */
    String getMessageBody();
}
