package com.example.music_player.interfacesAndAbstracts;

import com.example.music_player.metadata.PlaylistMetadata;

public interface InputEnterCallback {
    void onInputEnter(String input);
    void onInputEnter(int position);
}
