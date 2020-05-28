package pl.musicplayer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import pl.musicplayer.fragments.PlayerFragment;
import pl.musicplayer.fragments.SearchFragment;
import pl.musicplayer.fragments.SongListFragment;

import static pl.musicplayer.fragments.SearchFragment.searchPhrase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    public static int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeView();
        getSupportActionBar().hide();
        songId = R.raw.betterdays;
        loadFragment(new PlayerFragment());
    }

    public void initializeView() {
        int orientation = getResources().getConfiguration().orientation;
        loadFragment(new PlayerFragment());
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_horizontal);
        }

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        initializeView();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.player:
                fragment = new PlayerFragment();
                break;

            case R.id.songs:
                fragment = new SongListFragment();
                searchPhrase = null;
                break;

            case R.id.search:
                fragment = new SearchFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        int orientation = getResources().getConfiguration().orientation;

        if (fragment != null) {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_list, fragment)
                        .commit();
            }
            return true;
        }
        return false;
    }
}
