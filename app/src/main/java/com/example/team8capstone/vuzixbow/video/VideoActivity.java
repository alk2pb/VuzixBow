package com.example.team8capstone.vuzixbow.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.team8capstone.vuzixbow.R;


public class VideoActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener {
    SpeechRecognizer mSpeechRecognizer;

    private int resource;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            resource = extras.getInt("videoResource");
        }

        setContentView(R.layout.video_layout);

        mediaPlayer = MediaPlayer.create(VideoActivity.this, resource);
        mediaPlayer.setOnCompletionListener(VideoActivity.this);

        surfaceView = (SurfaceView) this.findViewById(R.id.video);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

//        setPageSelectedListener();
    }

    @Override
    public void onPause(){
//        Intent returnIntent = new Intent();
//        setResult(RESULT_OK,returnIntent);
//        finish();
        super.onPause();
    }

    @Override
    public void onCompletion(MediaPlayer mediaplayer) {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void playVideo() {
        mediaPlayer.start();
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        mediaPlayer.setDisplay(surfaceHolder);
        playVideo();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.release();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    protected void onNewIntent (Intent i){
        if( i.getBooleanExtra("finish",false) ){
            Intent returnIntent = new Intent();
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    }

//    private void setPageSelectedListener() {
//
//        VideoActivity.this.findViewById(R.id.video).setOnLongClickListener(new View.OnLongClickListener() {
//            public boolean onLongClick(View v) {
//                Intent returnInte5nt = new Intent();
//                setResult(RESULT_OK,returnIntent);
//                finish();
//                return true;
//            }
//        });
//    }
}