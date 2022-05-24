package com.example.music_player.metadata;

import com.example.music_player.interfacesAndAbstracts.ItemMetadata;
import com.example.music_player.utils.FileUtil;

import java.util.ArrayList;

public class PlaylistMetadata extends ItemMetadata {

    private int playlistId;
    private ArrayList<SongMetadata> songs;

    public PlaylistMetadata(String name, long date) {
        super(name, date);
    }

    public void addSong(SongMetadata song){
        songs.add(song);
        FileUtil.savePlaylist(this);
    }

    public void removeSound(SongMetadata song){
        songs.remove(song);
    }

    public ArrayList<SongMetadata> getPlaylistSongs(){
        if(songs == null){
            songs = FileUtil.importPlaylistSongs(this);
        }

        return songs;
    }



}
