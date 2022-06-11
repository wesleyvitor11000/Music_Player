package com.example.music_player.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.PlaylistSongsActivity;
import com.example.music_player.R;
import com.example.music_player.metadata.PlaylistMetadata;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder>{

    private ArrayList<PlaylistMetadata> playlists;
    private final Context context;

    public PlaylistAdapter(@NonNull ArrayList<PlaylistMetadata> playlists, @NonNull Context context){
        this.playlists = playlists;

        this.context = context;
    }

    public void updatePlaylists(ArrayList<PlaylistMetadata> playlists){
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public void notifyInsertion(){
        notifyItemInserted(playlists.size() - 1);
    }

    @NonNull
    @Override
    public PlaylistAdapter.PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.playlist_holder, parent, false);

        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistHolder holder, int position) {
        holder.name.setText(playlists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder{

        TextView name;

        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.playlist_name_tv);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, PlaylistSongsActivity.class);
                intent.putExtra("position", getLayoutPosition());
                context.startActivity(intent);
            });
        }
    }
}
