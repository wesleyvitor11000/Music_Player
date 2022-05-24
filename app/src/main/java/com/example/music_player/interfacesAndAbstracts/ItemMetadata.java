package com.example.music_player.interfacesAndAbstracts;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;

public abstract class ItemMetadata {

    private String name = null;
    private long date = 0;

    public ItemMetadata(String name, long date){
        this.name = name;
        this.date = date;
    }

    public String getName(){return name;}

    public long getDate(){return date;}
}
