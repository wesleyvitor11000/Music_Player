package com.example.music_player.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.music_player.enumsAndGlobals.GlobalAtributes;
import com.example.music_player.enumsAndGlobals.SortKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
    private static final String sortSettingsFileName = "sortingSettings.stg";

    public static File[] findSongs(File files){
        return findSongsArray(files).toArray(new File[0]);
    }

    public static ArrayList<File> findSongsArray(File files){

        ArrayList<File> songs = new ArrayList<>();
        File[] filesOnDirectory = files.listFiles();

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

    public static void saveStringToFile(@NonNull String content, @NonNull String fileName, @NonNull Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(content);

            outputStreamWriter.close();
        }catch (IOException e){
            Log.e(TAG, "Fail on try to save file", e);
        }
    }

    public static String[] readFile(String fileName, Context context){
        String[] content = new String[GlobalAtributes.FRAGMENTS_NUMBER];

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();

                for(int i = 0; i < content.length && line != null; line = bufferedReader.readLine(), i++){
                    content[i] = line;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Fail on try to read file", e);
        }

        return content;
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

        if(sortKeysString.length < GlobalAtributes.FRAGMENTS_NUMBER){
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

}
