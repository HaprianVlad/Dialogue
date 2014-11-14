package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

/**
 * Default implementation of DialogData
 */
public final class DefaultDialogData implements DialogueData {
    private  final static Comparator<Conversation> TIME_STAMPS_COMPARATOR;

    static {
        TIME_STAMPS_COMPARATOR = new Comparator<Conversation>() {
            public int compare(Conversation c1, Conversation c2) {
                return c2.getLastActivityTime().
                        compareTo(c1.getLastActivityTime()); // lowest timestamp comes before
            }
        };
    }

    private final static DialogueData OBJECT = new DefaultDialogData();

    private final static String CONVERSATION_ID = "CONVERSATION_ID";
    private final static String CONVERSATION = "CONVERSATION";


    private Map<ConversationId, Conversation> mConversations =
            new HashMap<ConversationId, Conversation>();

    private final List<DialogueDataListener> mListeners =  new ArrayList<DialogueDataListener>();

    /**
     * Gets the singleton instance
     * @return gets the singleton instance
     */
    public static DialogueData getInstance() {
        return OBJECT;
    }

    /*
        See DialogData.getConversations
     */
    public List<Conversation> getConversations() {
        if (mConversations == null) {
            return null;
        }

        List<Conversation> dialogueConversations = new ArrayList<Conversation>(mConversations.values());

        Collections.sort(dialogueConversations, TIME_STAMPS_COMPARATOR);
        return dialogueConversations;
    }

    /*
    See DialogData.getConversation
    */
    public Conversation getConversation(ConversationId conversationId) {
        if (conversationId == null) {
            throw new NullArgumentException("conversationId");
        }

        return mConversations.get(conversationId);
    }

    /*
    See DialogData.getConversation
    */
    public Conversation createOrGetConversation(Contact contact) {
        Collection<Conversation> conversations = mConversations.values();

        // Try finding it first.
        for (Conversation conversation : conversations) {
            if (conversation.getContacts().contains(contact)) {
                return conversation;
            }
        }

        // Default case, conversation not found.
        List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(contact);
        Conversation conversation = new DialogueConversation(contacts, new SystemTimeProvider());


        //Notify Dialogue Data listeners if a conversation changes
        conversation.addListener(new ConversationListener() {
            @Override
            public void onConversationChanged(ConversationId id) {
                notifyListeners();
            }
        });

        mConversations.put(conversation.getId(), conversation);
        notifyListeners();

        return conversation;
    }

    /*
    See DialogData.getConversation
    */
    public void removeConversation(ConversationId id) {
        if (mConversations.containsKey(id)) {
            mConversations.remove(id);
            notifyListeners();
        }
    }

    @Override
    public void addListener(DialogueDataListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        mListeners.add(listener);
    }

    @Override
    public void removeListener(DialogueDataListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void restoreFromBundle(Bundle savedData) {
        if (savedData == null) {
            throw new NullArgumentException("savedData");
        }

        List<ConversationId> conversationIds = savedData.getParcelableArrayList(CONVERSATION_ID);
        List<Conversation> conversations = savedData.getParcelableArrayList(CONVERSATION);
        if (conversationIds!= null && conversations!= null) {
            if (BuildConfig.DEBUG && (conversationIds.size() != conversations.size())) {
                throw new AssertionError("Wrong bundle instance in restoreFromBundle, DialogueData");
            }

            for (int i=0; i<conversationIds.size(); i++) {
                mConversations.put((ConversationId) conversationIds.get(i), (Conversation) conversations.get(i));
            }
            notifyListeners();


        }
    }

    @Override
    public Bundle createBundle() {
        Bundle b = new Bundle();

        b.putParcelableArrayList(CONVERSATION_ID, new ArrayList<ConversationId>(mConversations.keySet()));
        b.putParcelableArrayList(CONVERSATION, new ArrayList<Conversation>(mConversations.values()));

        return b;
    }


    //Method that notifies listeners when a change in conversation occurs
    private void notifyListeners() {
        if (mListeners.size() > 0) {
            for (DialogueDataListener listener : mListeners) {
                listener.onDialogueDataChanged();
            }
        }
    }
}
