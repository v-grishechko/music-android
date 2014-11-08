package com.example.music.smartmusic;

import java.util.ArrayList;

/**
 * Created by asus on 08.11.2014.
 */
public class SongsList {
    private ArrayList<Song> list;
    private int counter;

    public SongsList (ArrayList<Song> list) {
        setSongs(list);
    }

    public void setSongs (ArrayList<Song> list) {
        this.list = list;
        counter = 0;
    }

    public Song next (){
        counter++;
        Song song;
        if(counter >= list.size()) {
            counter = 0;
            song = list.get(counter);
        } else {
            song = list.get(counter);
        }
        return song;
    }


    public Song prev (){
        counter--;
        Song song;
        if(counter < 0) {
            counter = list.size() - 1;
            song = list.get(counter);
        } else {
            song = list.get(counter);
        }
        return song;
    }

    public Song current () {
        return list.get(counter);
    }
}
