package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.bohdomp.dialogue.R;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;

/**
 * @author swengTeam BohDomp
 *
 *  Implementation of an Adapter used to populate the Contact List View
 *
 */
public class ContactListAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;

    private List<DialogueConversation> mDialogueConversations;

    /**
     * Constructor
     * @param activity Parent activity
     * @param items List of the element to be displayed
     */
    public ContactListAdapter(Activity activity, List<DialogueConversation> items) {
        super();
        layoutInflater = activity.getWindow().getLayoutInflater();
        mDialogueConversations = items;
    }


    @Override
    public int getCount() {
        return mDialogueConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return mDialogueConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDialogueConversations.get(position).getConversationId().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactListViewHolder viewHolder;

        if (convertView==null) {

            convertView = layoutInflater.inflate(R.layout.contact_list_row, parent, false);

            viewHolder = new ContactListViewHolder();

            viewHolder.contactPicture = (ImageView) convertView.findViewById(R.id.contactPicture);
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contactName);
            viewHolder.contactChannels = (TextView) convertView.findViewById(R.id.contactChannels);
            viewHolder.lastMessage = (TextView) convertView.findViewById(R.id.contactLastMessage);


            //TODO MUST BE REPLACED BY A SWIPE TO DELETE

            //****************/

            viewHolder.deleteConversation = (Button) convertView.findViewById(R.id.deleteConversationButton);

            viewHolder.deleteConversation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("DELETE");
                }
            });

            //***************/

            convertView.setTag(viewHolder);
        } else {
            // Avoid using findViewById
            viewHolder = (ContactListViewHolder) convertView.getTag();
        }

        //TODO MODIFY THE LIST_ROW VIEW WITH THE CORRECT DATA

        return convertView;
    }

    /**
     *  Used in to implement View Holder pattern
     *  Enable the caching of the different UI elements present in a row
     */
    private static class ContactListViewHolder {
        protected ImageView contactPicture;
        protected TextView contactName;
        protected TextView contactChannels;
        protected TextView lastMessage;
        protected Button deleteConversation;
    }
}
