package com.example.music.smartmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 08.11.2014.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private IBinder mBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    private SongsList mSongs;
    private int mCurrentPosition = 0;
    private MediaMetadataRetriever mDataRetriever;
    private final String MUSIC_DIR = Environment.getExternalStorageDirectory().getPath() + "/Music";

    @Override
    public void onCreate() {
        super.onCreate();
        listOfSongsInitialize();
        mediaInitialize();
    }

    private void mediaInitialize() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
    }

    private void listOfSongsInitialize() {
        File musicDir = new File(MUSIC_DIR);
        File[] songs = musicDir.listFiles();
        ArrayList<Song> songsList = new ArrayList<Song>(100);
        mDataRetriever = new MediaMetadataRetriever();
        for (int i = 0; i < songs.length; i++) {
            Song song = Song.createSong(mDataRetriever, songs[i]);
            songsList.add(song);
            Log.d(getClass().getSimpleName(), song.toString());
        }
        mSongs = new SongsList(songsList);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(getClass().getSimpleName(), "onPrepared");
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(getClass().getSimpleName(), "onCompletion");
        try {
            next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    public void play() throws IOException {
        play(mSongs.current());
    }

    public void next() throws IOException {
        mMediaPlayer.reset();
        play(mSongs.next());
    }

    public void prev() throws IOException {
        mMediaPlayer.reset();
        play(mSongs.prev());
    }

    public void pause() {
        Log.d(getClass().getSimpleName(), "Pause");
        mCurrentPosition = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.pause();
    }

    public Song getSong() {
        Song song = mSongs.current();
        song.setCurrentTimeMls(mMediaPlayer.getCurrentPosition()/1000);
        return song;
    }

    private void play(Song song) throws IOException {
           if (mCurrentPosition != 0) {
               mMediaPlayer.seekTo(mCurrentPosition);
               mMediaPlayer.start();
               mCurrentPosition = 0;
           } else {
               mMediaPlayer.setDataSource(song.getPath());
               mMediaPlayer.prepareAsync();
           }
    }
}
