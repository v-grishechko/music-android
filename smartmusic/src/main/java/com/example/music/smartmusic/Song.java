package com.example.music.smartmusic;

import android.media.MediaMetadataRetriever;

import java.io.File;

/**
 * Created by asus on 08.11.2014.
 */
public class Song {
    String path;
    String title;
    String artist;
    byte[] cover;

    public int getCurrentTimeMls() {
        return currentTimeMls;
    }

    public void setCurrentTimeMls(int currentTimeMls) {
        this.currentTimeMls = currentTimeMls;
    }

    public int getLengthTimeMls() {
        return lengthTimeMls;
    }

    public void setLengthTimeMls(int lengthTimeMls) {
        this.lengthTimeMls = lengthTimeMls;
    }

    int currentTimeMls;
    int lengthTimeMls;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String toString() {
        String toString = "Title:" + title + "\n" + "Path:" + path + "\n" + "Artist:" + artist;
        return toString;
    }

    public static Song createSong(MediaMetadataRetriever mmr, File file) {
        Song song = new Song();
        mmr.setDataSource(file.getPath());
        song.setPath(file.getPath());
        song.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        song.setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        song.setLengthTimeMls(Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        song.setCover(mmr.getEmbeddedPicture());
        return song;
    }
}
