package ch.epfl.sweng.bohdomp.dialogue.ui.conversation;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2013 BohDomp
 *
 * A concrete implementation of {@link android.widget.BaseAdapter} that is backed by an array of
 * {@link ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage}.
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
        protected LinearLayout wrapperParent;
        protected LinearLayout wrapper;

        protected TextView body;
        protected TextView timeStamp;
        protected TextView status;
    }

    /**
     * Constructor
     * @param context The current context
     * @param items The array of messages used to populate the list
     */
    public MessagesAdapter(Context context, List<DialogueMessage> items) {
        super();

        Contract.throwIfArgNull(context, "context");
        Contract.throwIfArgNull(items, "items");

        this.mContext = context;
        updateData(items);
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
        Contract.throwIfArgNull(parent, "parent");

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
        Contract.throwIfArgNull(items, "items");
        Contract.throwIfArg(items.contains(null), "items contains null");

        this.mMessagesList = items;
        notifyDataSetChanged();
    }

    /**
     * Create a new {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversation.MessagesAdapter}
     * @param convertView Old view to reuse if possible
     * @return {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversation.MessagesAdapter.MessageViewHolder}
     * A new MessageViewHolder
     */
    private MessageViewHolder createViewHolder(View convertView) {
        Contract.assertNotNull(convertView, "convertView");

        MessageViewHolder viewHolder = new MessageViewHolder();

        viewHolder.wrapperParent = (LinearLayout) convertView.findViewById(R.id.wrapperParent);
        viewHolder.wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);

        viewHolder.body = (TextView) convertView.findViewById(R.id.body);
        viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
        viewHolder.status = (TextView) convertView.findViewById(R.id.messageStatus);

        return viewHolder;
    }

    /**
     * Setup the view using the message data
     * @param msg The message used to change view to the correct values
     * @param viewHolder The View Holder containing all view to update
     */
    private void setupView(DialogueMessage msg, MessageViewHolder viewHolder) {
        Contract.assertNotNull(msg, "message");
        Contract.assertNotNull(viewHolder, "viewHolder");

        String body = msg.getBody().getMessageBody();
        viewHolder.body.setText(body);

        String timeStamp = msg.getTimeStamp().toString();
        viewHolder.timeStamp.setText(timeStamp);

        if (msg.getStatus() == DialogueMessage.MessageStatus.OUTGOING) {
            viewHolder.status.setVisibility(View.VISIBLE);
        }

        if (msg.getStatus() == DialogueMessage.MessageStatus.OUTGOING) {
            viewHolder.wrapper.setBackgroundResource(R.drawable.bubble_right);
            viewHolder.wrapperParent.setGravity(Gravity.RIGHT);
            viewHolder.wrapper.setGravity(Gravity.RIGHT);
        } else if (msg.getStatus() == DialogueMessage.MessageStatus.INCOMING) {
            viewHolder.wrapper.setBackgroundResource(R.drawable.bubble_left);
            viewHolder.wrapperParent.setGravity(Gravity.LEFT);
            viewHolder.wrapper.setGravity(Gravity.LEFT);
        }
    }
}
