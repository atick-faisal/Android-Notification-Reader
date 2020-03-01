package ai.atick.notification_reader;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
}
