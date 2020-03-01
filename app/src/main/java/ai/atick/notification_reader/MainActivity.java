package ai.atick.notification_reader;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    /////////////////////////////////////////////////////
    public boolean permissionGranted = false;
    LinearLayout settingsPrompt;

    /////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsPrompt = findViewById(R.id.settings_prompt);
        settingsPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForNotificationAccessPermission();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        permissionGranted = NotificationManagerCompat.getEnabledListenerPackages(getApplicationContext()).contains(getApplicationContext().getPackageName());
        if (!permissionGranted) {
            settingsPrompt.setVisibility(View.VISIBLE);
        } else {
            settingsPrompt.setVisibility(View.GONE);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        ComponentName componentName = new ComponentName(this, FirebaseJobService.class);
        JobInfo info = new JobInfo.Builder(Key.ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(Key.INTERVAL)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = 0;
        if (scheduler != null) {
            resultCode = scheduler.schedule(info);
        }
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(Key.TAG, "Job scheduled");
        } else {
            Log.d(Key.TAG, "Job scheduling failed");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void askForNotificationAccessPermission() {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        Toast.makeText(getApplicationContext(), "Turn on Notification Access for Notification Reader", Toast.LENGTH_LONG).show();
    }
}
