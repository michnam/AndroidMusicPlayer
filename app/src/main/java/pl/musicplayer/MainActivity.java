package pl.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import pl.musicplayer.fragments.PlayerFragment;
import pl.musicplayer.fragments.PlaylistsFragment;
import pl.musicplayer.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    public static int songId = 1;
    private MediaPlayer mediaPlayer;
    private ImageButton btnPlay;
    private boolean shouldPlay = true;
    private TextView songTitle;
    private TextView songAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        loadFragment(new PlayerFragment());

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    public void playOrPause(View v) {
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        setPlayButtonIcon(v);
    }

    private void play(View v) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.betterdays);
        }
        mediaPlayer.start();
        btnPlay.setImageResource(R.drawable.pause);
        shouldPlay = false;
    }

    private void pause(View v) {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play);
            shouldPlay = true;
        }
    }

    public void previous(View v) {
        songId--;
        loadFragment(new PlayerFragment());
        setPlayButtonIcon(v);
    }

    public void next(View v) {
        songId++;
        System.out.println(songId);
        loadFragment(new PlayerFragment());
        setPlayButtonIcon(v);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.player:
                fragment = new PlayerFragment();
                break;

            case R.id.playlists:
                fragment = new PlaylistsFragment();
                break;

            case R.id.search:
                fragment = new SearchFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private void setPlayButtonIcon(View v) {
        if(shouldPlay) {
            play(v);
        } else {
            pause(v);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean refreshFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();

            return true;
        }
        return false;
    }
}
