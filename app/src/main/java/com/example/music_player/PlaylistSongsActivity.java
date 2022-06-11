package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.music_player.adapters.SongAdapter;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.PlaylistsUtil;

import java.util.ArrayList;

public class PlaylistSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        Toolbar toolbar = findViewById(R.id.playlist_toolbar);

        //getting playlist data
        Bundle bundle = getIntent().getExtras();
        int playlistPosition = bundle.getInt("position");

        PlaylistMetadata playlist = PlaylistsUtil.getPlaylists().get(playlistPosition);
        ArrayList<SongMetadata> songs = playlist.getPlaylistSongs(this);

        //setting playlist toolbar
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(playlist.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(songs == null) return;

        RecyclerView recycle = findViewById(R.id.playlist_songs_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        SongAdapter songAdapter = new SongAdapter(songs.toArray(new SongMetadata[0]), SortKey.SORT_NAME, R.menu.playlist_song_adapter_menu, this);
        songAdapter.setPlaylist(playlist);

        recycle.setLayoutManager(linearLayoutManager);
        recycle.setAdapter(songAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}