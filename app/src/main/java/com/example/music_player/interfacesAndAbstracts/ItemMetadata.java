package com.example.music_player.interfacesAndAbstracts;

import android.net.Uri;

import java.io.File;

public abstract class ItemMetadata {

    private File file;
    private String name = null;
    private long date = 0;
    private Uri uri = null;

    public ItemMetadata(File file, Uri uri, String name, long date){
        this.file = file;
        this.uri = uri;
        this.name = name;
        this.date = date;
    }

    public String getName(){return name;}

    public long getDate(){return date;}

    public File getFile() {return file;}

    public Uri getUri(){return uri;}

}
