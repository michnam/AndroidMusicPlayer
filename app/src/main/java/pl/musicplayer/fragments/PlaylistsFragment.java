package pl.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;
import pl.musicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;


public class PlaylistsFragment extends Fragment {
    private List<Song> songList = new ArrayList<>();
    private SongRepository db = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new SongRepository(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        songList = db.getAllSongs();
        return inflater.inflate(R.layout.fragment_playlists, null);
    }
}