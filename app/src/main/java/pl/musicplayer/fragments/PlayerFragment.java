package pl.musicplayer.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;

import static pl.musicplayer.MainActivity.songId;

public class PlayerFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(songId == 0) {
            songId = R.raw.betterdays;
        }

        songRepository = new SongRepository();

        LayoutInflater lf = getActivity().getLayoutInflater();
        view =  lf.inflate(R.layout.fragment_player, container, false);

        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPause(view);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous(view);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(view);
            }
        });

        songTitle = (TextView) view.findViewById(R.id.songTitle);
        songTitle.setText(songRepository.getById(songId).getTitle());

        songAuthor = (TextView) view.findViewById(R.id.songAuthor);
        songAuthor.setText(songRepository.getById(songId).getAuthor());

        setPlayButtonIcon(view);
        return view;
    }

    public void playOrPause(View v) {
        if(shouldPlay) {
            pause(v);
        } else {
            shouldPlay = true;
            play(v);
        }
    }

    public void previous(View v) {
        songId--;
        reloadFragment(this);
        setPlayButtonIcon(v);
        stop(v);
        play(v);
    }

    public void next(View v) {
        songId++;
        reloadFragment(this);
        setPlayButtonIcon(v);
        stop(v);
        play(v);
    }

    private void play(View v) {
        shouldPlay = true;
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), songId);
        }
        mediaPlayer.start();
        setPlayButtonIcon(v);
    }

    private void stop(View v) {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void pause(View v) {
        if(mediaPlayer != null) {
            shouldPlay = false;
            mediaPlayer.pause();
            setPlayButtonIcon(v);
        }
    }

    private void setPlayButtonIcon(View v) {
        if(shouldPlay) {
            btnPlay.setImageResource(R.drawable.pause);
        } else {
            btnPlay.setImageResource(R.drawable.play);
        }
    }

    private boolean reloadFragment(Fragment fragment) {
        if (fragment != null) {
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