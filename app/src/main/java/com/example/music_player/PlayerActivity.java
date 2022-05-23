package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class PlayerActivity extends AppCompatActivity {

    private ImageButton repeatModeButton;
    private ImageButton playPauseButton;
    private ImageButton previousSongButton;
    private ImageButton nextSongButton;

    private static SongPlayer.RepeatMode currentRepeatMode = SongPlayer.getCurrentRepeatMode();
    private static boolean isPlaying = SongPlayer.isPlaying();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playPauseButton = findViewById(R.id.play_pause_button);
        previousSongButton = findViewById(R.id.previous_song_button);
        nextSongButton = findViewById(R.id.nex_song_button);
        repeatModeButton = findViewById(R.id.repeat_mode_button);

        setPlayPauseButtonBackground(isPlaying);
        setRepeatButtonBackground(currentRepeatMode);

        playPauseButton.setOnClickListener(view -> {
            if(isPlaying){
                SongPlayer.pauseSong();
                isPlaying = false;
            }else{
                SongPlayer.resumeSong();
                isPlaying = true;
            }

            setPlayPauseButtonBackground(isPlaying);
        });

        previousSongButton.setOnClickListener(view -> SongPlayer.playpreviousSong(this));
        nextSongButton.setOnClickListener(view -> SongPlayer.playNextSong(this));


        repeatModeButton.setOnClickListener(view -> {
            switch (currentRepeatMode){
                case NO_REPEAT:
                    //set repeat mode to repeat all
                    currentRepeatMode = SongPlayer.RepeatMode.REPEAT_ALL;
                    SongPlayer.setRepeatMode(currentRepeatMode);
                    repeatModeButton.setBackgroundResource(R.drawable.ic_baseline_repeat_all_24);
                    break;

                case REPEAT_ALL:
                    //set repeat mode to repeat current
                    currentRepeatMode = SongPlayer.RepeatMode.REPEAT_CURRENT;
                    SongPlayer.setRepeatMode(currentRepeatMode);
                    repeatModeButton.setBackgroundResource(R.drawable.ic_baseline_repeat_one_24);
                    break;

                case REPEAT_CURRENT:
                    //set repeat mode to no repeat
                    currentRepeatMode = SongPlayer.RepeatMode.NO_REPEAT;
                    SongPlayer.setRepeatMode(currentRepeatMode);
                    break;
            }

            setRepeatButtonBackground(currentRepeatMode);
        });




    }

    private void setPlayPauseButtonBackground(boolean isPlaying){
        if(isPlaying){
            playPauseButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
            return;
        }

        playPauseButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
    }

    private void setRepeatButtonBackground(SongPlayer.RepeatMode repeatMode){
        switch (repeatMode){
            case NO_REPEAT:
                repeatModeButton.setBackgroundResource(R.drawable.ic_baseline_repeat_24);
                break;
            case REPEAT_ALL:
                repeatModeButton.setBackgroundResource(R.drawable.ic_baseline_repeat_all_24);
                break;
            case REPEAT_CURRENT:
                repeatModeButton.setBackgroundResource(R.drawable.ic_baseline_repeat_one_24);
                break;
        }
    }
}