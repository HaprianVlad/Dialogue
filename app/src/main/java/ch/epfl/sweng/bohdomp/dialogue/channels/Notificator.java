package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing a notification creator when receiving messages
 */
public final class Notificator {

    private static Notificator msNotificator;

    public static Notificator getInstance(Context context) {
        Contract.throwIfArgNull(context, "context");

        if (msNotificator == null) {
            msNotificator = new Notificator(context);
        }

        return msNotificator;
    }

    private Context mContext;
    private NotificationManager mNotificationManager;

    private Notificator(Context context) {
        Contract.assertNotNull(context, "context");

        this.mContext = context;
        this.mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyIncomming(DialogueMessage message) {
        Contract.throwIfArgNull(message, "message");

        Conversation conversation = DefaultDialogData.getInstance().
                createOrGetConversation(message.getContact());

        PendingIntent resultPendingIntent =
                makeStackBuilder(conversation).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String tag = conversation.getName();
        int id = (int) conversation.getId().getLong();
        mNotificationManager.notify(tag, id, makeNotification(message, resultPendingIntent));
    }

    public void cancelNotificationsForConversation(Conversation conversation) {
        mNotificationManager.cancel(conversation.getName(), (int) conversation.getId().getLong());
    }

    public void cancelAllNotifications() {
        mNotificationManager.cancelAll();
    }

    private TaskStackBuilder makeStackBuilder(Conversation conversation) {
        Intent resultIntent = new Intent(mContext, ConversationActivity.class);
        resultIntent.putExtra(DialogueConversation.CONVERSATION_ID, conversation.getId());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(ConversationActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder;
    }

    private Notification makeNotification(DialogueMessage message, PendingIntent intent) {
        Contract.assertNotNull(message, "message");
        Contract.assertNotNull(intent, "intent");

        return new Notification.Builder(mContext)
                .setContentTitle(message.getContact().getDisplayName())
                .setContentText(message.getPlainTextBody().getMessageBody())
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(intent).build();
    }

}
