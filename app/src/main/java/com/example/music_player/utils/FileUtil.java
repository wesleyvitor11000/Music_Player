package com.example.music_player.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.music_player.enumsAndGlobals.GlobalAtributes;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.metadata.SongMetadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FileUtil {

    private static final HashSet<String> supportedFormats = new HashSet<>(Arrays.asList(".mp3", ".m4a", ".ogg"));
    private static final String TAG = "FileUtil";

    //save files names
    private static final String sortSettingsFileName = "sortingSettings.stg";
    private static final String playlistsFileName = "playlists.stg";
    private static final String playlistsDirectoryName = "/playlists";

    public static File[] findSongs(File files){
        return findSongsArray(files).toArray(new File[0]);
    }

    public static ArrayList<File> findSongsArray(File files){

        ArrayList<File> songs = new ArrayList<>();
        File[] filesOnDirectory;

        filesOnDirectory = files.listFiles();

        if(filesOnDirectory == null) return songs;

        for (File file : filesOnDirectory) {
            if(file.isDirectory()){
                songs.addAll(findSongsArray(file));
            }else{
                if (verifyFileSupport(file)){
                  songs.add(file);
                }
            }
        }

        return songs;
    }

    public static boolean verifyFileSupport(File file){
        
        int index = file.getName().lastIndexOf(".");
        if(index == -1) return false;

        String fileExtension = file.getName().substring(index);

        return supportedFormats.contains(fileExtension);
    }

    public static void saveStringToFile(@NonNull String content, @NonNull String fileName, @NonNull Context context) {
        saveStringToFile(content, fileName, "", context);
    }

    public static void saveStringToFile(@NonNull String content, @NonNull String fileName, @NonNull String directory, @NonNull Context context){

        File file = getInternalFileFrom(fileName, directory, context);

        System.out.println("saving: " + content);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(content);

            outputStreamWriter.close();

        } catch (IOException e) {
            Log.e(TAG, "Error on save file", e);
        }
    }

    public static String[] readFile(String fileName, Context context) {
        return readFile(fileName, "", context);
    }

    public static String[] readFile(String fileName, String directory, Context context){
        String[] content = new String[GlobalAtributes.FRAGMENTS_NUMBER];

        File file = getInternalFileFrom(fileName, directory, context);

        if(!file.exists()) return null;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            for(int i = 0; i < content.length && line != null; line = bufferedReader.readLine(), i++){
                System.out.println("Reading: " + line);
                content[i] = line;
            }

            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            Log.e(TAG, "Fail on try to read file", e);
            return null;
        }

        return content;
    }

    public static File getInternalFileFrom(String fileName, String directory, Context context){
        File filesDir = context.getFilesDir();
        File fileDir = new File(filesDir.getAbsolutePath() + directory);

        if(!fileDir.exists())
            fileDir.mkdir();

        return new File(fileDir, fileName);
    }

    public static void saveSortSettings(@NonNull SortKey[] sortKeys, @NonNull Context context){
        StringBuilder sortKeysStringBuilder = new StringBuilder();

        for (SortKey sortKey : sortKeys) {
            if (sortKey != null) {
                sortKeysStringBuilder.append(sortKey).append("\n");
            } else {
                sortKeysStringBuilder.append(SortKey.SORT_NAME).append("\n");
            }
        }

        saveStringToFile(sortKeysStringBuilder.toString(), sortSettingsFileName, context);
    }

    public static SortKey[] readSortSettings(Context context){
        SortKey[] sortKeys;
        String[] sortKeysString = readFile(sortSettingsFileName, context);

        if(sortKeysString == null || sortKeysString.length < GlobalAtributes.FRAGMENTS_NUMBER){
            sortKeys = new SortKey[GlobalAtributes.FRAGMENTS_NUMBER];
        }else{
            sortKeys = MetadataUtil.extractSortKeysFromString(sortKeysString);
        }

        for(int i = 0; i < sortKeys.length; i++){
            if(sortKeys[i] == null)
                sortKeys[i] = SortKey.SORT_NAME;
        }

        return sortKeys;
    }

    public static void savePlaylists(@NonNull ArrayList<PlaylistMetadata> playlists, Context context){
        StringBuilder stringBuilder = new StringBuilder();

        for (PlaylistMetadata playlist : playlists) {
            if(playlist != null) {
                stringBuilder.append(playlist.getName()).append("|");
                stringBuilder.append(playlist.getDate()).append("\n");
            }
        }

        saveStringToFile(stringBuilder.toString(), playlistsFileName, context);
    }

    public static ArrayList<PlaylistMetadata> importPlaylists(@NonNull Context context) {
        String[] playlistsNames = readFile(playlistsFileName, context.getApplicationContext());
        ArrayList<PlaylistMetadata> playlists = new ArrayList<>();

        if(playlistsNames == null) return playlists;

        for(String s : playlistsNames){

            if(s == null) continue;
            int index = s.indexOf("|");
            String name = s.substring(0, index);
            long date = Long.parseLong(s.substring(index + 1));

            PlaylistMetadata playlist = new PlaylistMetadata(name, date);
            playlists.add(playlist);
        }

        return playlists;
    }

    public static ArrayList<SongMetadata> importPlaylistSongs(PlaylistMetadata playlist){
        return null;
    }

    public static void savePlaylist(PlaylistMetadata playlistMetadata) {

    }
}
