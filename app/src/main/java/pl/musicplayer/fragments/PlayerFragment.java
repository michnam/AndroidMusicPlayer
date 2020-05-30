package pl.musicplayer.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;

import static pl.musicplayer.MainActivity.songId;

public class PlayerFragment extends Fragment
{
    private MediaPlayer mediaPlayer;
    private TextView songTitle;
    private TextView songAuthor;
    private SongRepository songRepository;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private boolean shouldPlay = false;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        songRepository = new SongRepository();

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

        songTitle = (TextView) view.findViewById(R.id.songTitle);
        songTitle.setText(songRepository.getSongs().get(songId).getTitle());

        songAuthor = (TextView) view.findViewById(R.id.songAuthor);
        songAuthor.setText(songRepository.getSongs().get(songId).getAuthor());

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
        songId--;
        if (songId < 0)
            songId = songRepository.getSongs().size() - 1;
        reloadFragment(this);
        setPlayButtonIcon(v);
        stop(v);
        play(v);
    }

    public void next(View v) throws IOException {
        songId++;
        if (songId >= songRepository.getSongs().size())
            songId = 0;
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
            String filePath = Environment.getExternalStorageDirectory() + songRepository.getById(songId).getPath();
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
}