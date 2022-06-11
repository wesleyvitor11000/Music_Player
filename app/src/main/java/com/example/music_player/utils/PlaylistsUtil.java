package com.example.music_player.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.music_player.interfacesAndAbstracts.PlaylistsChangeCallback;
import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.metadata.SongMetadata;

import java.util.ArrayList;

public class PlaylistsUtil {

    private static ArrayList<PlaylistMetadata> playlistMetadataArrayList;
    private static ArrayList<PlaylistsChangeCallback> playlistsChangeCallbacks = new ArrayList<>();
    public static synchronized void addPlaylist(PlaylistMetadata playlist, Context context){
        getPlaylists(context);
        playlistMetadataArrayList.add(playlist);
        FileUtil.savePlaylist(playlist, context);
        notifyChange();
    }

    public static void addOnPlaylistsChange(PlaylistsChangeCallback playlistsChangeCallback){
        playlistsChangeCallbacks.add(playlistsChangeCallback);
    }

    private static void notifyChange(){
        for (PlaylistsChangeCallback p : playlistsChangeCallbacks){
            p.OnChange();
        }
    }

    public static ArrayList<PlaylistMetadata> getPlaylists(@NonNull Context context){
        if(playlistMetadataArrayList == null)
            playlistMetadataArrayList = FileUtil.importPlaylists(context);

        return playlistMetadataArrayList;
    }

    public static void addToPlaylist(@NonNull PlaylistMetadata playlist, @NonNull SongMetadata song, @NonNull Context context){
        playlist.addSong(song, context);
    }

    public static void renamePlaylist(String name){
        ///
    }

    public static ArrayList<PlaylistMetadata> getPlaylists(){
        return playlistMetadataArrayList;
    }

}
