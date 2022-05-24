package com.example.music_player.metadata;

import android.net.Uri;

import com.example.music_player.interfacesAndAbstracts.ItemMetadata;

import java.io.File;

public class SongMetadata extends ItemMetadata {

    private File file;
    private Uri uri = null;
    private String artist;
    private byte[] embeddedPicture;

    public SongMetadata(File file, Uri uri, String name, String artist, long date, byte[] embeddedPicture){
        super(name, date);
        this.file = file;
        this.uri = uri;
        this.artist = (artist != null)? artist : "";
        this.embeddedPicture = embeddedPicture;
    }

    public byte[] getEmbeddedPicture(){return embeddedPicture;}
    public String getArtist(){return artist;}
    public File getFile() {return file;}
    public Uri getUri(){return uri;}

}
