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
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pl.musicplayer.MainActivity;
import pl.musicplayer.R;
import pl.musicplayer.models.Song;
import pl.musicplayer.repositories.SongRepository;

import static pl.musicplayer.App.CHANNEL_ID;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mp;
    private SongRepository songRepository;
    public static boolean playingMusic;
    public static int currentSongId;
    public final static String NEXT_SONG = "MY_ACTION";




    public class LocalBinder extends Binder
    {
        public MusicService getService()
        {
            return MusicService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        pushForeground();
        songRepository = new SongRepository(this.getApplicationContext());
        //playTest(); // TEST

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



    public void playNew()
    {
        if(mp != null) mp.stop();
        mp = new MediaPlayer();
        mp.setOnCompletionListener(mp ->
        {
            Log.i(TAG, "Song completed");
            songRepository.getNextSong();
            playNew();

            Intent nextSongIntent = new Intent();
            nextSongIntent.setAction(NEXT_SONG);
            sendBroadcast(nextSongIntent);
        });

        Song currentSong = songRepository.getCurrentSong();
        Uri uri = Uri.parse("file://" + currentSong.getPath());

        Log.i(TAG, "Playing file with path: " + uri);
        mp.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        try
        {
            mp.setDataSource(String.valueOf(uri));
            mp.prepare();
            mp.start();
            updateNotification("Playing: " + currentSong.getAuthor() + " - " + currentSong.getTitle());
            playingMusic = true;
            currentSongId = currentSong.getId();
        }
        catch(Exception e)
        {
            Log.i(TAG, "Couldn't play song, no file with that path.");
        }
    }

    public void playOrPause()
    {
        if(mp != null)
        {
            Song currentSong = songRepository.getCurrentSong();
            if(playingMusic)
            {
                mp.pause();
                playingMusic = false;
                updateNotification("Paused: " + currentSong.getAuthor() + " - " + currentSong.getTitle());
                Log.i(TAG, "Pause music");
            }
            else
            {
                mp.seekTo(mp.getCurrentPosition());
                mp.start();
                playingMusic = true;
                updateNotification("Playing: " + currentSong.getAuthor() + " - " + currentSong.getTitle());
                Log.i(TAG, "Resume music");
            }
        }
        else
            playNew();
    }

    public void goNext()
    {
        songRepository.getNextSong();
        if(playingMusic)
            playNew();
        else
        {
            if (mp != null)
                mp.stop();
            mp = null;
        }

    }

    public void goPrevious()
    {
        songRepository.getPreviousSong();
        if(playingMusic)
            playNew();
        else
        {
            if (mp != null)
                mp.stop();
            mp = null;
        }
    }

    public void newSelected()
    {
        if(playingMusic)
            playNew();
        else
        {
            if(mp != null)
                mp.stop();
            mp = null;
        }
    }








    /**
     * TEST
     */
    public void playTest()
    {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(mp ->
        {
            Log.i(TAG, "MediaPlayer -> playTest -> onCompletion");
            SongRepository.currentSong++;
            playTest();
        });

        String filePath = songRepository.getSongById(SongRepository.currentSong).getPath();
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
            intent.setAction(NEXT_SONG);
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
