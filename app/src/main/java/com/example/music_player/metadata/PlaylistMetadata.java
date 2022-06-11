package com.example.music_player.metadata;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.music_player.interfacesAndAbstracts.ItemMetadata;
import com.example.music_player.utils.FileUtil;

import java.util.ArrayList;

public class PlaylistMetadata extends ItemMetadata {

    private ArrayList<SongMetadata> songs;

    public PlaylistMetadata(String name, long date) {
        super(name, date);
    }

    public void addSong(@NonNull SongMetadata song, @NonNull Context context){
        getPlaylistSongs(context);
        songs.add(song);
        FileUtil.addSongOnPlaylist(this, song, context);
    }

    public void removeSong(@NonNull SongMetadata song, @NonNull Context context){
        songs.remove(song);
        FileUtil.savePlaylistSongs(this, songs, context);
    }

    public ArrayList<SongMetadata> getPlaylistSongs(@NonNull Context context){
        if(songs == null){
            songs = FileUtil.importPlaylistSongs(this, context);
        }

        return songs;
    }
}
