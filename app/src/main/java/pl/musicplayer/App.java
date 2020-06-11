package pl.musicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class App extends Application
{
    public static final String TAG = "App";

    public static final String CHANNEL_ID = "AndroidMusicPLayer";

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG, "App -> onCreate()");
        createNotificationChannel();

    }

    private void createNotificationChannel()
    {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "AndroidMusicPLayer", NotificationManager.IMPORTANCE_DEFAULT);
        serviceChannel.setSound(null, null);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

}


