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
        FileUtil.saveSongOnPlaylist(this, song, context);
    }

    public void removeSound(@NonNull SongMetadata song, @NonNull Context context){
        getPlaylistSongs(context);
        songs.remove(song);
    }

    public ArrayList<SongMetadata> getPlaylistSongs(@NonNull Context context){
        if(songs == null){
            songs = FileUtil.importPlaylistSongs(this, context);
        }

        return songs;
    }



}
