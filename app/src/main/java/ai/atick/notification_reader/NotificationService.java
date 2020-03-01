package ai.atick.notification_reader;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    String appTitle = "Untitled";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
//        Bundle extras = sbn.getNotification().extras;
//        appTitle = extras.getString("android.title");
        Log.d(Key.TAG, pack);
    }
}
