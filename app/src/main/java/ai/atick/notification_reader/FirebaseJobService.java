package ai.atick.notification_reader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static ai.atick.notification_reader.Key.TAG;

public class FirebaseJobService extends JobService {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    AppDatabase appDatabase;
    ArrayList<String> uploadList = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Uploading to Firebase");
        doInBackground();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Upload cancelled");
        return true;
    }

    private void doInBackground() {
        appDatabase = new AppDatabase(getApplicationContext());
        uploadList.clear();
        uploadList = appDatabase.getListString("Notification_List");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy|MM|dd-HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        reference.child(currentDateAndTime).setValue(uploadList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Key.jobCompleted = true;
                Log.d(TAG, "Firebase upload finished");
                uploadList.clear();
            }
        });
    }
}
