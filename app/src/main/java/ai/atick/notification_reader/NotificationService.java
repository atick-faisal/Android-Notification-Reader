package ai.atick.notification_reader;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends NotificationListenerService {

    public ArrayList<String> myList = new ArrayList<>();
    AppDatabase appDatabase;
    String packageName = "no-name";
    String appTitle = "Untitled";
    String text = "empty";
    long postTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = new AppDatabase(getApplicationContext());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        packageName = sbn.getPackageName();
        postTime = sbn.getPostTime();
//        Bundle extras = sbn.getNotification().extras;
//        appTitle = extras.getString("android.title");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        Log.d(Key.TAG, currentDateAndTime);
        myList.add(currentDateAndTime);
        appDatabase.putListString("Dictionary", myList);
    }
}
