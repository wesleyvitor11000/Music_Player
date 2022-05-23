package com.example.music_player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.example.music_player.metadata.SongMetadata;

import java.util.ArrayList;
import java.util.HashMap;

public class SongPlayer {

    public enum RepeatMode {
        NO_REPEAT,
        REPEAT_CURRENT,
        REPEAT_ALL
    }

    private static PlayerActivity playerActivity;
    private static MediaPlayer mediaPlayer;

    //Data structuring
    private static HashMap<SongMetadata, Integer> songMetadataMap = new HashMap<>();
    private static ArrayList<SongMetadata> songMetadataArrayList = new ArrayList<>();

    //Current playing song
    private static SongMetadata currentSong;
    private static int currentSongIndex = 0;

    //Control vars
    private static boolean isPlaying = false;
    private static RepeatMode currentRepeatMode = RepeatMode.REPEAT_ALL;

    private static void updateMediaPlayer(Context context){

        if(mediaPlayer != null){
            stopMediaPlayer();
        }

        mediaPlayer = (currentSong == null) ? null : MediaPlayer.create(context, currentSong.getUri());
        if(mediaPlayer == null) {
            isPlaying = false;
            return;
        }
        if(isPlaying) mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            setNextSong(false);
            updateMediaPlayer(context);
        });
    }

    private static void stopMediaPlayer(){
        if(mediaPlayer == null) return;

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        mediaPlayer.release();
        mediaPlayer = null;
    }

    public static boolean isPlaying(){return isPlaying;}

    public static void setRepeatMode(RepeatMode repeatMode){
        currentRepeatMode = repeatMode;
    }

    public static RepeatMode getCurrentRepeatMode(){return currentRepeatMode;}

    public static void playSong(@NonNull SongMetadata song, @NonNull Context context){

        if(!songMetadataMap.containsKey(song)){
            currentSongIndex = addSong(song);
        }else{
            Integer index = songMetadataMap.get(song);

            if(index != null)
                currentSongIndex = index;
        }

        currentSong = song;

        updateMediaPlayer(context);
        playSongs(context);
    }

    public static void playSongs(@NonNull Context context){

        if(songMetadataMap.size() <= 0) return;

        if(!isPlaying){
            if(currentSong == null){
                currentSong = songMetadataArrayList.get(0);
                currentSongIndex = 0;
            }
            isPlaying = true;
            updateMediaPlayer(context);
            mediaPlayer.start();
        }
    }
    public static void resumeSong(){
        if(mediaPlayer != null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public static void pauseSong(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public static void playNextSong(Context context){
        setNextSong(true);
        updateMediaPlayer(context);
    }

    public static void playpreviousSong(Context context){
        setPreviousSong();
        updateMediaPlayer(context);
    }

    private static void setPreviousSong(){

        if (currentRepeatMode == RepeatMode.REPEAT_ALL) {
            currentSongIndex = (currentSongIndex - 1);
            currentSongIndex = (currentSongIndex < 0) ? songMetadataArrayList.size() - 1 : currentSongIndex;
            currentSong = songMetadataArrayList.get(currentSongIndex);

            return;
        }

        int previous = currentSongIndex - 1;

        if (previous >= 0) {
            currentSongIndex = previous;
            currentSong = songMetadataArrayList.get(previous);
        }
    }

    private static void setNextSong(boolean userInput){
        switch (currentRepeatMode){
            case NO_REPEAT:
                int next = currentSongIndex + 1;

                if (next >= songMetadataArrayList.size()) {
                    currentSong = null;
                    currentSongIndex = 0;
                    stopMediaPlayer();
                    break;
                }

                currentSongIndex = next;
                currentSong = songMetadataArrayList.get(next);

                break;
            case REPEAT_CURRENT:
                if(userInput){
                    int nextSong = currentSongIndex + 1;

                    if (nextSong >= songMetadataArrayList.size()) {
                        break;
                    }

                    currentSongIndex = nextSong;
                    currentSong = songMetadataArrayList.get(nextSong);
                }

                break;

            case REPEAT_ALL:
                currentSongIndex = (currentSongIndex + 1) % songMetadataArrayList.size();
                currentSong = songMetadataArrayList.get(currentSongIndex);
                break;
        }
    }

    public static void addAllSongs(SongMetadata[] songs){
        for (SongMetadata song: songs) {
            addSong(song);
        }
    }

    public static int addSong(@NonNull SongMetadata song){

        Integer index;

        if(songMetadataMap.containsKey(song)){
            index = songMetadataMap.get(song);
            return (index == null) ? 0 : index;
        }

        index = songMetadataArrayList.size();
        songMetadataMap.put(song, index);
        songMetadataArrayList.add(song);

        return index;
    }

    public static void clearAllSongs(){
        stopMediaPlayer();
        songMetadataArrayList.clear();
        songMetadataMap.clear();
        isPlaying = false;
        currentSong = null;
        currentSongIndex = 0;
    }

    public static void showPlayerActivity(Context context){
        if (playerActivity == null){
            playerActivity = new PlayerActivity();
        }

        Intent intent = new Intent(context, playerActivity.getClass());
        context.startActivity(intent);
    }
}
