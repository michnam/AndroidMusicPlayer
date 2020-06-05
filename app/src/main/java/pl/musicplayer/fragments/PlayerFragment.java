package pl.musicplayer.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import pl.musicplayer.database.DBHelper;
import pl.musicplayer.repositories.SongRepository;
import pl.musicplayer.services.MusicService;


public class PlayerFragment extends Fragment
{
    private final static String TAG = "PlayerFragment";
    private MediaPlayer mediaPlayer;
    private TextView songTitle;
    private TextView songAuthor;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private boolean shouldPlay = false;
    private View view;
    private DBHelper db = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int datapassed = intent.getIntExtra("DATAPASSED", 0);
            Log.i(TAG,"Get new song id from service: " + datapassed);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DBHelper(getActivity());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.MY_ACTION);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
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
                try {
                    playOrPause(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    previous(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    next(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        songTitle = (TextView) view.findViewById(R.id.songTitle);
//        songTitle.setText(SongRepository.songs.get(SongRepository.currentSongId).getTitle());
        songTitle.setText(db.getAllSongs().get(SongRepository.currentSongId).getTitle());

        songAuthor = (TextView) view.findViewById(R.id.songAuthor);
//        songAuthor.setText(SongRepository.songs.get(SongRepository.currentSongId).getAuthor());
        songAuthor.setText(db.getAllSongs().get(SongRepository.currentSongId).getAuthor());

        setPlayButtonIcon(view);
        return view;
    }

    public void playOrPause(View v) throws IOException {
        if(shouldPlay)
        {
            pause(v);
        } else
        {
            shouldPlay = true;
            play(v);
        }
    }

    public void previous(View v) throws IOException {
        SongRepository.currentSongId--;
        if (SongRepository.currentSongId < 0)
            SongRepository.currentSongId = db.getAllSongs().size() - 1;
        reloadFragment(this);
        setPlayButtonIcon(v);
        stop(v);
        play(v);
    }

    public void next(View v) throws IOException {
        SongRepository.currentSongId++;
        if (SongRepository.currentSongId >= db.getAllSongs().size())
            SongRepository.currentSongId = 0;
        reloadFragment(this);
        setPlayButtonIcon(v);
        stop(v);
        play(v);
    }

    private void play(View v) throws IOException {
        shouldPlay = true;
        if(mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
            String filePath = db.getFilePath(SongRepository.currentSongId);
            Uri myUri = Uri.parse("file://" + filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(Objects.requireNonNull(this.getContext()), myUri);
            mediaPlayer.prepare();
        }
        mediaPlayer.start();
        setPlayButtonIcon(v);
    }

    private void stop(View v)
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void pause(View v)
    {
        if(mediaPlayer != null)
        {
            shouldPlay = false;
            mediaPlayer.pause();
            setPlayButtonIcon(v);
        }
    }

    private void setPlayButtonIcon(View v)
    {
        if(shouldPlay)
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

    @Override
    public void onStop()
    {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}