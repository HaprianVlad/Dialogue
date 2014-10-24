package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;

import java.util.Set;

/**
 * interface representing a Contact
 */
public interface Contact {
    String getDisplayName();
    Set<String> getPhoneNumbers();
    Set<ChannelType> availableChannels();
    Contact updateInfo(final Context context);

    /**
     * enumeration for channel types
     */
    public static enum ChannelType {
        SMS
    }
}
