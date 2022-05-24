package com.example.music_player.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.music_player.metadata.PlaylistMetadata;

import java.util.ArrayList;

public class PlaylistsUtil {

    private static ArrayList<PlaylistMetadata> playlistMetadataArrayList;

    public static void addPlaylist(PlaylistMetadata playlist, Context context){
        getPlaylists(context);
        playlistMetadataArrayList.add(playlist);
        FileUtil.savePlaylists(playlistMetadataArrayList, context);
    }

    public static ArrayList<PlaylistMetadata> getPlaylists(@NonNull Context context){
        if(playlistMetadataArrayList == null)
            playlistMetadataArrayList = FileUtil.importPlaylists(context);

        return playlistMetadataArrayList;
    }

    public static void renamePlaylist(String name){
        ///
    }

    public static ArrayList<PlaylistMetadata> getPlaylists(){
        return playlistMetadataArrayList;
    }
}
