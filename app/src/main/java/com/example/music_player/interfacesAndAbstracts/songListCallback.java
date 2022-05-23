package com.example.music_player.interfacesAndAbstracts;

import com.example.music_player.metadata.SongMetadata;

public interface songListCallback {
    void songListChangedCallback(SongMetadata[] songs);
    void onMediaPlayerClosed();
    void onSongChanged();
}
