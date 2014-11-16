package ch.epfl.sweng.bohdomp.dialogue.ui.messages;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * @author swengTeam 2013 BohDomp
 *
 * A concrete implementation of {@link android.widget.BaseAdapter} that is backed by an array of
 * {@link ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage}.
 *
 * It may throw:
 * - {@link java.lang.IllegalArgumentException} if the array is equal to null
 * - {@link java.lang.NullPointerException} if one of the message inside the array is null
 */
public class MessagesAdapter extends BaseAdapter {
    private static final String LOG_TAG = "MessagesAdapter";

    private final Context mContext;
    private List<DialogueMessage> mMessagesList;

    /**
     * Class containing all view inside a row of the message list.
     * It is used to implement the view holder pattern
     */
    private static class MessageViewHolder {
        protected TextView body;
        protected TextView timeStamp;
    }

    /**
     * Constructor
     * @param context The current context
     * @param items The array of messages used to populate the list
     */
    public MessagesAdapter(Context context, List<DialogueMessage> items) {

        super();

        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (items == null) {
            throw new NullArgumentException("items");
        }

        if (items.contains(null)) {
            throw new IllegalArgumentException("items contains null");
        }

        this.mContext = context;
        this.mMessagesList = items;

    }

    @Override
    public int getCount() {
        return mMessagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMessagesList.get(position).getId().getLong();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (parent == null) {
            throw new NullArgumentException("parent");
        }

        MessageViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.message_bubble_row, parent, false);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (MessageViewHolder) convertView.getTag();
        }

        DialogueMessage msg = (DialogueMessage) getItem(position);

        setupView(msg, viewHolder);

        return convertView;
    }

    public void updateData(List<DialogueMessage> items) {
        if (items == null) {
            throw new NullArgumentException("items");
        }

        if (items.contains(null)) {
            throw new IllegalArgumentException("items contains null");
        }

        this.mMessagesList = items;
        notifyDataSetChanged();
    }

    /**
     * Create a new {@link ch.epfl.sweng.bohdomp.dialogue.ui.messages.MessagesAdapter}
     * @param convertView Old view to reuse if possible
     * @return {@link ch.epfl.sweng.bohdomp.dialogue.ui.messages.MessagesAdapter.MessageViewHolder}
     * A new MessageViewHolder
     */
    private MessageViewHolder createViewHolder(View convertView) {
        if (BuildConfig.DEBUG && convertView == null) {
            throw new AssertionError("null convertView");
        }

        MessageViewHolder viewHolder = new MessageViewHolder();

        viewHolder.body = (TextView) convertView.findViewById(R.id.body);
        viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);

        return viewHolder;
    }

    /**
     * Setup the view using the message data
     * @param msg The message used to change view to the correct values
     * @param viewHolder The View Holder containing all view to update
     */
    private void setupView(DialogueMessage msg, MessageViewHolder viewHolder) {
        if (BuildConfig.DEBUG && msg == null) {
            throw new AssertionError("null msg");
        }
        if (BuildConfig.DEBUG && viewHolder == null) {
            throw new AssertionError("null viewHolder");
        }

        String body = msg.getBody().getMessageBody();
        viewHolder.body.setText(body);

        String timeStamp = msg.getTimeStamp().toString();
        viewHolder.timeStamp.setText(timeStamp);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.body.getLayoutParams();
        if (msg.getStatus() == DialogueMessage.MessageStatus.OUTGOING) {
            lp.gravity = Gravity.RIGHT;
        } else if (msg.getStatus() == DialogueMessage.MessageStatus.INCOMING) {
            lp.gravity = Gravity.LEFT;
        }
    }
}
