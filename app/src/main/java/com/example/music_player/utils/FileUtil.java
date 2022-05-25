package com.example.music_player.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.ContentView;
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
    private static final String settingsFileExtension = ".stg";
    private static final String sortSettingsFileName = "sortingSettings" + settingsFileExtension;
    private static final String playlistsFileName = "playlists" + settingsFileExtension;;
    private static final String playlistsDirectoryName = "playlists";

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

    public static void saveStringToFile(@NonNull String content, @NonNull String fileName, boolean append, @NonNull Context context) {
        saveStringToFile(content, fileName, "", append, context);
    }

    public static void saveStringToFile(@NonNull String content, @NonNull String fileName, @NonNull String directory, boolean append, @NonNull Context context){

        File file = getInternalFileFrom(fileName, directory, context);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, append);
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
        ArrayList<String> content = new ArrayList<>();

        File file = getInternalFileFrom(fileName, directory, context);

        if(!file.exists()) return null;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            for(; line != null; line = bufferedReader.readLine()){
                content.add(line);
            }

            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            Log.e(TAG, "Fail on try to read file", e);
            return null;
        }

        return content.toArray(new String[0]);
    }

    public static File getInternalFileFrom(String fileName, String directory, Context context){
        File filesDir = context.getFilesDir();
        File fileDir = new File(filesDir.getAbsolutePath() + "/" + directory);

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

        saveStringToFile(sortKeysStringBuilder.toString(), sortSettingsFileName, false, context);
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
                stringBuilder.append(playlist.getName()).append("#");
                stringBuilder.append(playlist.getDate()).append("\n");
            }
        }

        saveStringToFile(stringBuilder.toString(), playlistsFileName, false, context);
    }

    public static ArrayList<PlaylistMetadata> importPlaylists(@NonNull Context context) {
        String[] playlistsNames = readFile(playlistsFileName, context.getApplicationContext());
        ArrayList<PlaylistMetadata> playlists = new ArrayList<>();

        if(playlistsNames == null) return playlists;

        for(String s : playlistsNames){

            if(s == null) continue;
            int index = s.indexOf("#");
            String name = s.substring(0, index);
            long date = Long.parseLong(s.substring(index + 1));

            PlaylistMetadata playlist = new PlaylistMetadata(name, date);
            playlists.add(playlist);
        }

        return playlists;
    }

    public static void savePlaylist(@NonNull PlaylistMetadata playlist, @NonNull Context context) {
        String stringBuilder =  playlist.getName() + "#" +
                                playlist.getDate() + "\n";

        saveStringToFile(stringBuilder, playlistsFileName, true, context);
    }

    public static ArrayList<SongMetadata> importPlaylistSongs(@NonNull PlaylistMetadata playlist, @NonNull Context context){

        String[] playlistSongsPaths = readFile(playlist.getName() + settingsFileExtension, playlistsDirectoryName, context);
        ArrayList<SongMetadata> songs = new ArrayList<>();

        if (playlistSongsPaths == null) return songs;

        for(String s : playlistSongsPaths){
            if(s == null) continue;

            SongMetadata song = SongUtil.getSongMetadataFromPath(s, context);
            songs.add(song);
        }

        return songs;
    }

    public static void saveSongOnPlaylist(@NonNull PlaylistMetadata playlist, @NonNull SongMetadata song, @NonNull Context context) {
        String songPath = song.getUri().getPath() + "\n";
        System.out.println("Saving path: " + songPath);
        System.out.println("in Playlist: " + playlistsDirectoryName + "/" + playlist.getName() + settingsFileExtension);

        saveStringToFile(songPath, playlist.getName() + settingsFileExtension, playlistsDirectoryName, true, context);
    }
}
