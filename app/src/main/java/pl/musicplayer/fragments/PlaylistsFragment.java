package pl.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;
import pl.musicplayer.R;
import pl.musicplayer.models.Song;
import pl.musicplayer.repositories.SongRepository;

import java.util.ArrayList;
import java.util.List;


public class PlaylistsFragment extends Fragment {
    private List<Song> songList = new ArrayList<>();
    private SongRepository songRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        songRepository = new SongRepository();
        songList = songRepository.getSongs();
        return inflater.inflate(R.layout.fragment_playlists, null);
    }
}