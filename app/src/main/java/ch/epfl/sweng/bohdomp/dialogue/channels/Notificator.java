package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;


import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing a notification creator when receiving messages
 */
public class Notificator {

    private Context mContext;

    public Notificator(Context context) {

        Contract.throwIfArgNull(context, "context");
        this.mContext = context;
    }

    public void update(DialogueMessage message) {

        Contract.throwIfArgNull(message, "message");

        Notification.Builder mBuilder = new Notification.Builder(mContext)
                .setContentTitle("New Message from:" + message.getContact().getPhoneNumbers().iterator().next())
                .setContentText(message.getBody().getMessageBody())
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_RINGTONE_URI);
        //This image will be fixed
        //mBuilder.setSmallIcon(R.drawable.ic_action_search);


        Intent resultIntent = new Intent(mContext, ConversationListActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(ConversationListActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        final int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
