package pl.musicplayer.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;
import pl.musicplayer.services.MusicService;


public class PlayerFragment extends Fragment
{
    private final String TAG = "PlayerFragment";
    private MediaPlayer mediaPlayer;
    private TextView songTitle;
    private TextView songAuthor;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private boolean shouldPlay = false;
    private View view;
    private SongRepository songRepository = null;

    private MusicService musicService;
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            Log.i(TAG, "Service bind");
        }

        @Override
        public void onServiceDisconnected(ComponentName name){ Log.i(TAG, "Serivce unbind"); }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            callReloadFragment();
        }
    };

    public void callReloadFragment()
    {
        reloadFragment(this);
    }



    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        songRepository = new SongRepository(getActivity());

        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.NEXT_SONG);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

        if(songRepository.getAllSongs().size() > 0)
            if(songRepository.getCurrentSong().getId() != MusicService.currentSongId)
                if(musicService != null)
                    musicService.newSelected();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        LayoutInflater lf = getActivity().getLayoutInflater();
        view = lf.inflate(R.layout.fragment_player, container, false);

        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        btnPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(songRepository.getAllSongs().size() > 0)
                    callPlayOrPause();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(songRepository.getAllSongs().size() > 0)
                    callGoPrevious();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(songRepository.getAllSongs().size() > 0)
                    callGoNext();
            }
        });


        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        if(songRepository.getAllSongs().size() > 0)
        {
            songTitle = (TextView) view.findViewById(R.id.songTitle);
            songTitle.setText(songRepository.getAllSongs().get(SongRepository.currentSong).getTitle());

            songAuthor = (TextView) view.findViewById(R.id.songAuthor);
            songAuthor.setText(songRepository.getAllSongs().get(SongRepository.currentSong).getAuthor());
        }
        setPlayButtonIcon(view);
        return view;
    }

    private void callPlayOrPause()
    {

        musicService.playOrPause();
        reloadFragment(this);
    }

    private void callGoNext()
    {
        musicService.goNext();
        reloadFragment(this);
    }

    private void callGoPrevious()
    {
        musicService.goNext();
        reloadFragment(this);
    }





//    public void playOrPause(View v) throws IOException
//    {
//        if(shouldPlay)
//        {
//            pause(v);
//        } else
//        {
//            shouldPlay = true;
//            play(v);
//        }
//    }
//
//    public void previous(View v) throws IOException
//    {
//        SongRepository.currentSong--;
//        if(SongRepository.currentSong < 0)
//            SongRepository.currentSong = songRepository.getAllSongs().size() - 1;
//        reloadFragment(this);
//        setPlayButtonIcon(v);
//        stop(v);
//        play(v);
//    }
//
//    public void next(View v) throws IOException
//    {
//        SongRepository.currentSong++;
//        if(SongRepository.currentSong >= songRepository.getAllSongs().size())
//            SongRepository.currentSong = 0;
//        reloadFragment(this);
//        setPlayButtonIcon(v);
//        stop(v);
//        play(v);
//    }
//
//    private void play(View v) throws IOException
//    {
//        shouldPlay = true;
//        if(mediaPlayer == null)
//        {
//            mediaPlayer = new MediaPlayer();
//            String filePath = songRepository.getSongById(SongRepository.currentSong).getPath();
//            Uri myUri = Uri.parse("file://" + filePath);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setDataSource(Objects.requireNonNull(this.getContext()), myUri);
//            mediaPlayer.prepare();
//        }
//        mediaPlayer.start();
//        setPlayButtonIcon(v);
//    }
//
//    private void stop(View v)
//    {
//        if(mediaPlayer != null)
//        {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//
//    private void pause(View v)
//    {
//        if(mediaPlayer != null)
//        {
//            shouldPlay = false;
//            mediaPlayer.pause();
//            setPlayButtonIcon(v);
//        }
//    }

    private void setPlayButtonIcon(View v)
    {
        if(MusicService.playingMusic)
        {
            btnPlay.setImageResource(R.drawable.pause);
        } else
        {
            btnPlay.setImageResource(R.drawable.play);
        }
    }

    private boolean reloadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
            return true;
        }
        return false;
    }
}