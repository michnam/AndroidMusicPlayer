package pl.musicplayer.models;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import pl.musicplayer.R;
import pl.musicplayer.repositories.SongRepository;
import pl.musicplayer.fragments.PlayerFragment;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private final String TAG = "SongListAdapter";
    private Song[] listdata;
    private Context context;

    public SongListAdapter(Song[] listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.song_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Song myListData = listdata[position];
        holder.songTitleItem.setText(listdata[position].getTitle());
        holder.songAuthorItem.setText(listdata[position].getAuthor());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = ((AppCompatActivity) context).getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new PlayerFragment())
                            .commit();
                } else {
                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_player, new PlayerFragment())
                            .commit();
                }
                SongRepository.currentSong = myListData.getId();
                Log.i(TAG, "Choosen song with id: " + myListData.getId() + " and name: " + myListData.getAuthor() + " - " + myListData. getTitle());
                }
            });
        }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitleItem;
        public TextView songAuthorItem;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.songTitleItem = (TextView) itemView.findViewById(R.id.songTitleItem);
            this.songAuthorItem = (TextView) itemView.findViewById(R.id.songAuthorItem);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutListItem);
        }
    }
}