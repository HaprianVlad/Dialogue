package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.os.Parcelable;

import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;

/**
 * interface representing a Contact
 */
public interface Contact extends Parcelable {
    String getDisplayName();
    Set<PhoneNumber> getPhoneNumbers();
    Set<PhoneNumber> getPhoneNumbers(ChannelType channel);
    Set<ChannelType> availableChannels();
    boolean hasFingerprint();
    String getFingerprint();
    Contact updateInfo(final Context context);
    Creator<Contact> getParcelCreator();
}
