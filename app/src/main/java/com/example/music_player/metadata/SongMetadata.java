package com.example.music_player.metadata;

import android.net.Uri;

import java.io.File;

public class SongMetadata extends ItemMetadata {
    private File file;
    private String artist;
    private byte[] embeddedPicture;

    public SongMetadata(File file, Uri uri, String name, String artist, long date, byte[] embeddedPicture){
        super(file, uri, name, date);
        this.file = file;
        this.artist = (artist != null)? artist : "";
        this.embeddedPicture = embeddedPicture;
    }

    public byte[] getEmbeddedPicture(){return embeddedPicture;}
    public String getArtist(){return artist;}

}
