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
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.MainActivity;
import com.example.music_player.R;
import com.example.music_player.SongPlayer;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.interfacesAndAbstracts.InputEnterCallback;
import com.example.music_player.interfacesAndAbstracts.SortedFragment;
import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.MetadataUtil;
import com.example.music_player.utils.PlaylistsUtil;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements SortedFragment {

    private SongMetadata[] allSongsMetadata;
    private final SongMetadata[] findSongsMetadata;
    private SongMetadata[] currentSongsMetadata;

    private final Context context;
    private SortKey sortKey;
    private @MenuRes int optionsMenu = -1;
    private PlaylistMetadata playlist;

    private boolean isSearching = false;

    public SongAdapter(SongMetadata[] songs, SortKey sortKey, Context context){
        this.context = context;

        allSongsMetadata = songs;
        findSongsMetadata = new SongMetadata[0];

        currentSongsMetadata = allSongsMetadata;

        sortElements(sortKey);
    }

    public SongAdapter(SongMetadata[] songs, SortKey sortKey, @MenuRes int optionsMenu, Context context){
        this.context = context;
        this.optionsMenu = optionsMenu;

        allSongsMetadata = songs;
        findSongsMetadata = new SongMetadata[0];

        currentSongsMetadata = allSongsMetadata;

        sortElements(sortKey);
    }

    public void setPlaylist(PlaylistMetadata playlist){
        this.playlist = playlist;
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
        currentSongsMetadata = allSongsMetadata;
        notifyDataSetChanged();
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

            if(optionsMenu != -1){
               // moreOptionsButton.setOnClickListener(view -> MainActivity.showPopupMenu(context, view, optionsMenu, onMenuItemClickListener));
            }else{
                moreOptionsButton.setVisibility(View.GONE);
                return;
            }

            moreOptionsButton.setOnClickListener(view -> MainActivity.showPopupMenu(context, view, optionsMenu, this::onMenuItemClick));
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

        private boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
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

                case R.id.add_to_playlist:
                    MainActivity.showPlaylistsDialog(context, new InputEnterCallback() {
                        @Override
                        public void onInputEnter(String input) {
                        }

                        @Override
                        public void onInputEnter(int position) {
                            PlaylistMetadata playlist = PlaylistsUtil.getPlaylists().get(position);
                            playlist.addSong(currentSongsMetadata[getLayoutPosition()], context);
                        }
                    });
                    break;

                case R.id.remove_from_playlist:
                    if (playlist == null) break;
                    playlist.removeSong(currentSongsMetadata[getLayoutPosition()], context);
                    allSongsMetadata = playlist.getPlaylistSongs(context).toArray(new SongMetadata[0]);
                    exitSearchMode();
            }
            return true;
        }
    }
}
