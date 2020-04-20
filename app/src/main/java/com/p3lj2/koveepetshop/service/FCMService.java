package com.p3lj2.koveepetshop.service;

import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.p3lj2.koveepetshop.R;

import java.util.Objects;

public class FCMService extends FirebaseMessagingService {
    public static final int MIN_QTY_WARNING_NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, getString(R.string.min_qty_warning_id))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(MIN_QTY_WARNING_NOTIFICATION_ID, notification.build());
    }

}
