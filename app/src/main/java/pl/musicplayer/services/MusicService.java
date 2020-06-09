package pl.musicplayer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pl.musicplayer.MainActivity;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;

import static pl.musicplayer.App.CHANNEL_ID;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mp;
    private SongRepository db;
    public final static String MY_ACTION = "MY_ACTION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        pushForeground();
        db = new SongRepository(this.getApplicationContext());
        playTest(); // TEST

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

    public void updateNotification(String message)
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message)
                .setSmallIcon(R.drawable.note_image)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(364, notification);
    }

    /**
     * TEST
     */
    public void playTest()
    {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(mp ->
        {
            Log.i(TAG, "MediaPlayer.onCompletion");
            SongRepository.currentSong++;
            playTest();
        });

        String filePath = db.getSongById(SongRepository.currentSong).getPath();
        Uri myUri = Uri.parse("file://" + filePath);
        Log.i(TAG, "Plaing file with path: " + myUri);
        mp.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        try
        {
            mp.setDataSource(String.valueOf(myUri));
            mp.prepare();
            mp.start();
            updateNotification("Playing song with id: " + SongRepository.currentSong);
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("DATAPASSED", SongRepository.currentSong);
            sendBroadcast(intent);
        }
        catch(Exception e)
        {
            Log.i(TAG, "Couldn't play song, no file with that path.");
            e.printStackTrace();
        }

    }

}
