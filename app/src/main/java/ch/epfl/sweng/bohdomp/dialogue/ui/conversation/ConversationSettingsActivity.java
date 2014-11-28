package ch.epfl.sweng.bohdomp.dialogue.ui.conversation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2013 BohDomp
 * Activity used to show the settings about the ConversationActivity
 */
public class ConversationSettingsActivity extends Activity {
    private RadioGroup mGroup;

    private Conversation mConversation;
    private List<Contact> mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_settings);

        Intent intent = getIntent();

        try {
            ConversationId conversationID;
            conversationID = intent.getParcelableExtra(DialogueConversation.CONVERSATION_ID);
            initData(conversationID);
            setViewElements();
            setListensers();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewElements();
    }

    private void initData(ConversationId conversationId) {
        Contract.throwIfArgNull(conversationId, "conversationId");

        mConversation = DefaultDialogData.getInstance().getConversation(conversationId);

        if (BuildConfig.DEBUG && mConversation == null) {
            throw new AssertionError("null mConversation");
        }

        mContact = mConversation.getContacts();
    }

    private void setViewElements() {
        mGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        mGroup.removeAllViews();

        Contact contact = mContact.get(0);

        Set<Contact.ChannelType> channels = contact.availableChannels();

        for (Contact.ChannelType channel : channels) {

            Set<Contact.PhoneNumber> numbers = contact.getPhoneNumbers(channel);

            for (Contact.PhoneNumber number :numbers) {
                RadioButton btn = new RadioButton(this);
                btn.setText(channel.toString() + " / " + number.number());
                btn.setTag(R.id.id_channel, channel);
                btn.setTag(R.id.id_phoneNumber, number);
                mGroup.addView(btn);
            }
        }
    }

    private void setListensers() {

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btn = (RadioButton) radioGroup.findViewById(i);

                Contact.ChannelType channel = (Contact.ChannelType) btn.getTag(R.id.id_channel);
                Contact.PhoneNumber number = (Contact.PhoneNumber) btn.getTag(R.id.id_phoneNumber);

                // FIXME Should update the info where to send

                finish();
            }
        });
    }
}
