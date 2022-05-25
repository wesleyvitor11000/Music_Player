package com.example.music_player.utils;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.metadata.SongMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SongUtil {
    private static SongMetadata[] songs;
    private static HashMap<String, SongMetadata> songsPathMap;

    public static SongMetadata[] getSongs(@NonNull Context context){
        if(songs == null || songsPathMap == null) {
            File[] songFiles = FileUtil.findSongs(Environment.getExternalStorageDirectory());
            songs = MetadataUtil.getAttributes(context, songFiles);

            songsPathMap = new HashMap<>();

            for(SongMetadata song : songs){
                if(songsPathMap.containsKey(song.getUri().getPath())) continue;

                songsPathMap.put(song.getUri().getPath(), song);
            }
        }

        return songs;
    }
    public static SongMetadata getSongMetadataFromPath(@NonNull String path, @NonNull Context context){
        getSongs(context);

        if(!songsPathMap.containsKey(path)){
            System.out.println("Doesn't contains key" + path);
            return null;}

        System.out.println("Contains key" + path);
        return songsPathMap.get(path);
    }
}
