package com.example.music_player.metadata;

import android.net.Uri;

import com.example.music_player.interfacesAndAbstracts.ItemMetadata;

import java.io.File;

public class SongMetadata extends ItemMetadata {
    private String artist;
    private byte[] embeddedPicture;

    public SongMetadata(File file, Uri uri, String name, String artist, long date, byte[] embeddedPicture){
        super(file, uri, name, date);
        this.artist = (artist != null)? artist : "";
        this.embeddedPicture = embeddedPicture;
    }

    public byte[] getEmbeddedPicture(){return embeddedPicture;}
    public String getArtist(){return artist;}

}
