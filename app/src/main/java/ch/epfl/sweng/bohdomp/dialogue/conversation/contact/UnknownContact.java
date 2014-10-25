package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * class representing a contact for which no entry was found in the contact database
 */
class UnknownContact implements Contact {

    private final String mPhoneNumber;

    UnknownContact(final String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    @Override
    public String getDisplayName() {
        return "unknown: " + mPhoneNumber;
    }

    @Override
    public Set<String> getPhoneNumbers() {
        Set<String> result = new HashSet<String>();
        result.add(mPhoneNumber);
        return result;
    }

    @Override
    public Set<ChannelType> availableChannels() {
        Set<ChannelType> result = new HashSet<ChannelType>();
        result.add(ChannelType.SMS);
        return result;
    }

    @Override
    public Contact updateInfo(final Context context) {
        return new ContactFactory(context).contactFromNumber(mPhoneNumber);
    }
}
