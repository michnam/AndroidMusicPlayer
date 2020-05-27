package pl.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;

import static pl.musicplayer.MainActivity.songId;


public class PlayerFragment extends Fragment {
    private TextView songTitle;
    private TextView songAuthor;
    private SongRepository songRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        songRepository = new SongRepository();

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_player, container, false);

        songTitle = (TextView) view.findViewById(R.id.songTitle);
        songTitle.setText(songRepository.getById(songId).getTitle());

        songAuthor = (TextView) view.findViewById(R.id.songAuthor);
        songAuthor.setText(songRepository.getById(songId).getAuthor());

        System.out.println(songId);
        System.out.println(songRepository.getById(songId).getAuthor());
        return view;
    }
}