package pl.musicplayer.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.Nullable;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;
import pl.musicplayer.models.Song;
import pl.musicplayer.models.SongListAdapter;

import static pl.musicplayer.fragments.SearchFragment.searchPhrase;

public class SongListFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private SongRepository db = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new SongRepository(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        view =  lf.inflate(R.layout.fragment_playlists, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        SongListAdapter adapter = new SongListAdapter(getSongs(), getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private Song[] getSongs() {
        if(searchPhrase == null) {
            return db.getAllSongs().stream().toArray(n -> new Song[n]);
        } else {
            return db.searchByTitle(searchPhrase).stream().toArray(n -> new Song[n]);
        }
    }
}
