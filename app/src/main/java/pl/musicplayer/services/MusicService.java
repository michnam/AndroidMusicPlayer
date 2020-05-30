package pl.musicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pl.musicplayer.MainActivity;
import pl.musicplayer.R;

import static pl.musicplayer.App.CHANNEL_ID;

public class MusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        pushForeground();

        return START_NOT_STICKY;
    }

    public void pushForeground()
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Player works in background")
                .setSmallIcon(R.drawable.note_image)
                .setContentIntent(pendingIntent).build();

        startForeground(364, notification);
    }

}
