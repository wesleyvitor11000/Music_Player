package com.example.music_player.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.MainActivity;
import com.example.music_player.R;
import com.example.music_player.SongPlayer;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.interfacesAndAbstracts.SortedFragment;
import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.MetadataUtil;

import java.io.File;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements SortedFragment {

    private final SongMetadata[] allSongsMetadata;
    private SongMetadata[] findSongsMetadata;
    private SongMetadata[] currentSongsMetadata;

    private final Context context;
    private SortKey sortKey;

    private boolean isSearching = false;

    public SongAdapter(File[] songs, SortKey sortKey, Context context){
        this.context = context;

        allSongsMetadata = MetadataUtil.getAttributes(context, songs);
        findSongsMetadata = new SongMetadata[0];

        currentSongsMetadata = allSongsMetadata;

        sortElements(sortKey);
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.music_card_holder, parent, false);

        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.setAttributes(currentSongsMetadata[position]);
    }

    @Override
    public int getItemCount() {
        return currentSongsMetadata.length;
    }

    @Override
    public void sortElements(SortKey sortKey) {
        if(this.sortKey == sortKey) return;

        this.sortKey = sortKey;

        MetadataUtil.sortBy(allSongsMetadata, sortKey);
        MetadataUtil.sortBy(findSongsMetadata, sortKey);
        notifyDataSetChanged();
    }

    @Override
    public void enterSearchMode() {
        isSearching = true;
        currentSongsMetadata = new SongMetadata[0];
        notifyDataSetChanged();
    }

    @Override
    public void findItemsByName(String name) {
        if(isSearching){
            currentSongsMetadata = MetadataUtil.searchItemsByName(name, allSongsMetadata).toArray(new SongMetadata[0]);

            notifyDataSetChanged();
        }
    }

    @Override
    public void exitSearchMode() {
        isSearching = false;
        notifyDataSetChanged();
        currentSongsMetadata = allSongsMetadata;
    }

    public class SongHolder extends RecyclerView.ViewHolder{

        TextView musicName;
        TextView artistName;
        ImageView musicImage;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_title_textview);
            artistName = itemView.findViewById(R.id.music_artist_textview);
            musicImage = itemView.findViewById(R.id.music_image);

            itemView.setOnClickListener(view -> {
                SongPlayer.addAllSongs(currentSongsMetadata);
                SongPlayer.playSong(currentSongsMetadata[getLayoutPosition()], context);
                SongPlayer.showPlayerActivity(context);
            });

            ImageButton moreOptionsButton = itemView.findViewById(R.id.more_options_button);

            moreOptionsButton.setOnClickListener(view -> MainActivity.showPopupMenu(context, view, R.menu.sound_adapter_menu, menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.reproduce:
                        SongPlayer.clearAllSongs();
                        SongPlayer.addSong(currentSongsMetadata[getLayoutPosition()]);
                        SongPlayer.playSongs(context);
                        break;

                    case R.id.reproduce_all:
                        SongPlayer.clearAllSongs();
                        SongPlayer.addAllSongs(currentSongsMetadata);
                        SongPlayer.playSong(currentSongsMetadata[getLayoutPosition()], context);
                        break;

                    case R.id.add_song:
                        SongPlayer.addSong(currentSongsMetadata[getLayoutPosition()]);
                        break;

                    case R.id.add_all_songs:
                        SongPlayer.addAllSongs(currentSongsMetadata);
                        break;
                }
                return true;
            }));
        }

        public void setAttributes(SongMetadata songMetadata){

            if(songMetadata == null) return;

            musicName.setText(songMetadata.getName());
            artistName.setText(songMetadata.getArtist());

            if(songMetadata.getEmbeddedPicture() != null){
                Bitmap embeddedPictureBitmap = BitmapFactory.decodeByteArray(songMetadata.getEmbeddedPicture(), 0,
                                                songMetadata.getEmbeddedPicture().length);
                musicImage.setImageBitmap(embeddedPictureBitmap);
            }else{
                musicImage.setImageResource(R.drawable.ic_baseline_music_note_24);
            }

        }
    }
}
