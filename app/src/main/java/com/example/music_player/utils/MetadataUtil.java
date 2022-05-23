package com.example.music_player.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.interfacesAndAbstracts.ItemMetadata;
import com.example.music_player.metadata.SongMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MetadataUtil {

    private static String TAG = "MetadataUtil";

    public static SortKey[] extractSortKeysFromString(String[] sortKeysString){

        SortKey[] sortKeys = new SortKey[sortKeysString.length];

        for(int i = 0; i < sortKeys.length; i++){
            try{
                sortKeys[i] = Enum.valueOf(SortKey.class, sortKeysString[i]);
            }catch (Exception e){
                sortKeys[i] = SortKey.SORT_NAME;
            }
        }

        return sortKeys;
    }

    public static void sortBy(ItemMetadata[] itemsMetadata, SortKey sortType){
        switch (sortType){
            case SORT_NAME: //sort by name
                Arrays.sort(itemsMetadata, (a, b) -> a.getName().compareTo(b.getName()));
                break;
            case SORT_DATE:
                Arrays.sort(itemsMetadata, (a, b) -> Long.compare(a.getDate(), b.getDate()));
                break;
        }

    }

    public static SongMetadata[] getAttributes(Context context, File[] songs){
        SongMetadata[] songsMetadata = new SongMetadata[songs.length];

        for(int i = 0; i < songs.length; i++){
            try{
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            Uri uri = Uri.fromFile(songs[i]);
            metadataRetriever.setDataSource(context, uri);

            songsMetadata[i] = getAttributes(songs[i], metadataRetriever);

            }catch(Exception e){
                Log.e(TAG, "Error on read file metadata", e);
            }
        }

        return songsMetadata;
    }

    public static SongMetadata getAttributes(File song, MediaMetadataRetriever musicMetadata){

        if(song == null) return null;

        String name = musicMetadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                artist = musicMetadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        if(name == null || name.isEmpty()){
            int pos = song.getName().lastIndexOf(".");
            name = song.getName().substring(0, pos - 1);
        }

        if(artist == null || artist.isEmpty()){
            int pos = song.getName().indexOf("-");
            if(pos != -1)
                artist = song.getName().substring(0, pos - 1);
        }

        long date = song.lastModified();
        byte[] embeddedPicture = musicMetadata.getEmbeddedPicture();


        return new SongMetadata(song, Uri.fromFile(song), name, artist, date, embeddedPicture);
    }

    public static <item extends ItemMetadata> ArrayList<item> searchItemsByName(@NonNull String content, @NonNull item[] allItems) {

        ArrayList<item> findItems = new ArrayList<>();

        if (content.isEmpty()) return findItems;

        String lowerCaseContent = content.toLowerCase();

        for (item i : allItems) {
            if(i.getName().toLowerCase().contains(lowerCaseContent)){
                findItems.add(i);
            }
        }

        return findItems;
    }
}
