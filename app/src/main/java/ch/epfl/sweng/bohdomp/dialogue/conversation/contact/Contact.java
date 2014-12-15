package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.os.Parcelable;

import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NoFingerprintException;

/**
 * interface representing a Contact
 */
public interface Contact extends Parcelable {
    /**
     * Gets the display name of the contact
     * @return the display name of the contact
     */
    String getDisplayName();

    /**
     * Gets the phone numbers of the contact
     * @return the phone numbers of the contact
     */
    Set<PhoneNumber> getPhoneNumbers();

    /**
     * Gets the phone numbers of the contact depending on a channel type filter
     * @param channel the channel that we want to use as filter
     * @return the phone numbers
     */
    Set<PhoneNumber> getPhoneNumbers(ChannelType channel);

    /**
     * Gets the available channels for the contact
     * @return the available channels
     */
    Set<ChannelType> getAvailableChannels();

    /**
     * Checks whether the contact has a fingerprint
     * @return whether the contact has a fingerprint
     */
    boolean hasFingerprint();

    /**
     * Gets the fingerprint of the contact
     * @return the fingerprint of the contact
     * @throws NoFingerprintException if we can't retrieve it.
     */
    String getFingerprint() throws NoFingerprintException;

    /**
     * Update the info for a contact
     * @param context the context
     * @return the new contact
     */
    Contact updateInfo(final Context context);

    /**
     * Gets the parcel creator for the contact.
     * @return the parcel creator for the contact.
     */
    Creator<Contact> getParcelCreator();
}
