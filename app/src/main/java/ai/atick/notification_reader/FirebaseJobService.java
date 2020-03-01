package ai.atick.notification_reader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static ai.atick.notification_reader.Key.TAG;

public class FirebaseJobService extends JobService {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    ArrayList<String> dataList = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Uploading to Firebase");
        doInBackground(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Upload cancelled");
        return true;
    }

    private void doInBackground(final JobParameters parameters) {
        AppDatabase appDatabase = new AppDatabase(getApplicationContext());
        dataList = appDatabase.getListString("Dictionary");
        reference.child("notification_list").setValue(dataList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d(TAG, "Firebase upload finished");
                NotificationService notificationService = new NotificationService();
                notificationService.myList.clear();
            }
        });
    }
}
