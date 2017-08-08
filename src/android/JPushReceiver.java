package cn.jiguang.cordova.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        try {
            if (action.equals(JPushInterface.ACTION_REGISTRATION_ID)) {
                String rId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
                JPushPlugin.onReceiveRegistrationId(rId);

            } else if (action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                handlingNotificationReceive(bundle);

            } else if (action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                handlingNotificationOpen(bundle);

                Intent launch = context.getPackageManager().getLaunchIntentForPackage(
                        context.getPackageName());
                launch.addCategory(Intent.CATEGORY_LAUNCHER);
                launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(launch);

            } else if (action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
                handlingMessageReceive(bundle);

            } else if (action.equals(JPushInterface.ACTION_CONNECTION_CHANGE)) {
                boolean isConnected = bundle.getBoolean(JPushInterface.EXTRA_CONNECTION_CHANGE);
                JPushPlugin.onConnectionChange(isConnected);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handlingMessageReceive(Bundle bundle) throws JSONException {
        JSONObject message = new JSONObject();

        message.put("id", bundle.getString(JPushInterface.EXTRA_MSG_ID));
        message.put("title", bundle.getString(JPushInterface.EXTRA_TITLE));
        message.put("message", bundle.getString(JPushInterface.EXTRA_MESSAGE));

        JSONObject extra = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
        message.put("extra", extra);

        JPushPlugin.onReceiveMessage(message);
    }

    private void handlingNotificationOpen(Bundle bundle) throws JSONException {
        JSONObject notification = new JSONObject();

        notification.put("id", bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
        notification.put("title", bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        notification.put("alert", bundle.getString(JPushInterface.EXTRA_ALERT));

        String extraJsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        notification.put("extra", new JSONObject(extraJsonStr));

        JPushPlugin.onOpenNotification(notification);
    }

    private void handlingNotificationReceive(Bundle bundle) throws JSONException {
        JSONObject notification = new JSONObject();

        notification.put("id", bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
        notification.put("title", bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        notification.put("alert", bundle.getString(JPushInterface.EXTRA_ALERT));

        String extraJsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        notification.put("extra", new JSONObject(extraJsonStr));

        notification.put("html", bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH));

        notification.put("bigText", bundle.getString(JPushInterface.EXTRA_BIG_TEXT));
        notification.put("bigPicturePath", bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH));

        String inboxJsonStr = bundle.getString(JPushInterface.EXTRA_INBOX);
        notification.put("inbox", new JSONObject(inboxJsonStr));

        notification.put("priority", bundle.getString(JPushInterface.EXTRA_NOTI_PRIORITY));
        notification.put("category", bundle.getString(JPushInterface.EXTRA_NOTI_CATEGORY));

        JPushPlugin.onReceiveNotification(notification);
    }
}
