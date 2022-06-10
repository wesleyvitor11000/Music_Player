package com.example.music_player.singletons;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.FileUtil;
import com.example.music_player.utils.MetadataUtil;

import java.io.File;
import java.util.HashMap;

public class SongsSingleton {
    private SongMetadata[] songs;
    private static HashMap<String, SongMetadata> songsPathMap;

    private static SongsSingleton instance;

    private SongsSingleton(){}

    public static synchronized SongsSingleton getInstance(@NonNull Context context){
        if(instance == null){
            instance = new SongsSingleton();
            instance.importSongs(context);
        }

        return instance;
    }

    private void importSongs(@NonNull Context context){
        File[] songFiles = FileUtil.findSongs(Environment.getExternalStorageDirectory());
        songs = MetadataUtil.getAttributes(context, songFiles);

        songsPathMap = new HashMap<>();

        for(SongMetadata song : songs){
            if(songsPathMap.containsKey(song.getUri().getPath())) continue;

            songsPathMap.put(song.getUri().getPath(), song);
        }
    }

    public SongMetadata[] getSongs(@NonNull Context context){
        return songs;
    }
    public SongMetadata getSongMetadataFromPath(@NonNull String path){

        if(!songsPathMap.containsKey(path))
            return null;

        return songsPathMap.get(path);
    }
}
