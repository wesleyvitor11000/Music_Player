package com.example.music_player;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.music_player.metadata.SongMetadata;

import java.util.ArrayList;
import java.util.Arrays;


public class SongPlayerOld {

    public enum RepeatMode {
        NO_REPEAT,
        REPEAT_CURRENT,
        REPEAT_ALL
    }

    private static ArrayList<SongMetadata> playingSongs = new ArrayList<>();
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    private static boolean alreadyRunning = false;

    private static int actualSong = 0;
    private static boolean playing = true;
    private static RepeatMode repeatMode = RepeatMode.NO_REPEAT;

    private static void runMediaPlayer(Context context){
        if(alreadyRunning) return;

        startMediaPlayer(context, actualSong);
        alreadyRunning = true;
    }

    private static void reproduceSong(Context context, int position){
        if(playingSongs == null || position >= playingSongs.size() || playingSongs.size() <= 0) return;

        stopMediaPlayer();

        mediaPlayer = MediaPlayer.create(context, playingSongs.get(position).getUri());

        if(playing) mediaPlayer.start();

    }

    private static void startMediaPlayer(Context context, int position){
        reproduceSong(context, position);
        playing = true;

        mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
            switch (repeatMode){
                case NO_REPEAT:

                    actualSong++;
                    if(actualSong >= playingSongs.size()){
                        actualSong = 0;
                        playing = false;
                    }
                    break;

                case REPEAT_CURRENT:
                    break;

                case REPEAT_ALL:

                    actualSong = (actualSong + 1) % playingSongs.size();
                    break;
            }

            if(playing) reproduceSong(context, actualSong);
        });
    }

    private static void stopMediaPlayer(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        alreadyRunning = false;
    }

    public static void resetPlayer(){
        stopMediaPlayer();
        actualSong = 0;
        playingSongs = new ArrayList<>();
        alreadyRunning = false;
    }

    public static void pauseSong(){

    }

    public static void continueSong(){

    }

    public static void nextSong(){

    }

    public static void previousSong(){

    }

    public static void goToSong(int position){

    }

    public static void activateRandomReproduction(){

    }

    public static void setSong(SongMetadata song, Context context){
        resetPlayer();
        addSong(song, context);
    }

    public static void setSongRange(SongMetadata[] songs, int position, Context context){
        resetPlayer();
        addSongRange(songs, context);
    }

    public static void addSong(SongMetadata song, Context context){
        playingSongs.add(song);
        runMediaPlayer(context);
    }

    public static void addSongRange(SongMetadata[] songs, Context context){
        playingSongs.addAll(Arrays.asList(songs));
        runMediaPlayer(context);
    }

    public static void setRepeatMode(RepeatMode newRepeatMode){
        repeatMode = newRepeatMode;
    }

}
