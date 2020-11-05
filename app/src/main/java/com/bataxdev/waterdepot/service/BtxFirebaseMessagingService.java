package com.bataxdev.waterdepot.service;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.WorkManager;
import com.bataxdev.waterdepot.MainActivity;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.worker.FirebaseWorker;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.jetbrains.annotations.NotNull;
import androidx.work.OneTimeWorkRequest;

import java.util.Map;

public class BtxFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0)
        {
            scheduleJob();
        }
    }

    private  void  scheduleJob()
    {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(FirebaseWorker.class).build();
        WorkManager.getInstance().beginWith(work).enqueue();
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
//        super.onNewToken(s);
        Log.i(TAG,s);
    }
}
