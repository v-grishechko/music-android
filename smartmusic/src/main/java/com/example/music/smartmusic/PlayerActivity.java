package com.example.music.smartmusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by asus on 08.11.2014.
 */
public class PlayerActivity extends Activity {
    private Button mButton;
    private PlayerService mService;
    private boolean mBound = false;
    private TextView mTitle;
    private ImageView mCover;
    private TextView mArtist;
    private TextView mCurrentTime;
    private TextView mLengthTime;

    private Handler mHandler;
    private int mInterval = 900;

    Runnable mUiUpdater = new Runnable() {
        @Override
        public void run() {
            updateUiPlayer(mService.getSong()); //this function can change value of mInterval.
            mHandler.postDelayed(mUiUpdater, mInterval);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);
        mButton = (Button) findViewById(R.id.btn_play);
        Log.wtf(getClass().getSimpleName(), "onCreate");
        Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mArtist = (TextView) findViewById(R.id.tv_artist);
        mCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        mCover = (ImageView) findViewById(R.id.iv_cover);
        mButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (mBound) {
                                               try {
                                                   mService.play();
                                                   mUiUpdater.run();
                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               }
                                           }
                                       }
                                   }
        );

        mHandler = new Handler();
    }

    public void pause(View view) {
        mService.pause();
        mHandler.removeCallbacks(mUiUpdater);
    }

    public void next(View view) {
        try {
            mService.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prev(View view) {
        try {
            mService.prev();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUiPlayer (Song song) {
        mTitle.setText("Title:" + song.getTitle());
        mArtist.setText("Artist:" + song.getArtist());
        mCurrentTime.setText(song.getCurrentTimeMls() + "");
        Bitmap cover = BitmapFactory.decodeByteArray(song.getCover(), 0, song.getCover().length);
        mCover.setImageBitmap(cover);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.d(PlayerActivity.this.getClass().getSimpleName(), "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
