package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.PlaylistsUtil;

import java.util.ArrayList;

public class PlaylistSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        Bundle bundle = getIntent().getExtras();
        int playlistPosition = bundle.getInt("position");


        ArrayList<SongMetadata> songs = PlaylistsUtil.getPlaylists().get(playlistPosition).getPlaylistSongs(this);

        TextView tv = findViewById(R.id.temp_playlist_songs);

        if(songs == null) return;

        StringBuilder stringBuilder = new StringBuilder();

        for (SongMetadata song: songs) {
            stringBuilder.append(song.getName());
        }

        tv.setText(stringBuilder.toString());
    }
}