package com.example.netaudioplayerexample;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnCompletionListener,
		OnBufferingUpdateListener, OnClickListener, OnPreparedListener,
		OnErrorListener {

	Button startBtn;
	Button stopBtn;
	TextView statusTV;
	ProgressBar bufferSB;
	MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(this);
		startBtn.setEnabled(false);

		stopBtn = (Button) findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(this);
		stopBtn.setEnabled(false);

		statusTV = (TextView) findViewById(R.id.statusTV);
		statusTV.setText("Creating mediaPlayer");

		bufferSB = (ProgressBar) findViewById(R.id.bufferSB);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);

		statusTV.setText("Created mediaPlayer");

		statusTV.setText("seting DataSource");
		try {
			mediaPlayer
					.setDataSource("http://lnmcc.net/wordpress/wp-content/uploads/2013/10/Rolling-In-The-Deep.mp3");
			statusTV.setText("setted DataSource");

			statusTV.setText("calling prepareAsync");
			// 不同于prepare(), prepareAsync()会立即返回，后台开始缓冲
			mediaPlayer.prepareAsync();
		} catch (IOException e) {
			Log.v("mediaPlayer.setDataSource", e.getMessage());
		}
	}

	@Override
	public void onClick(View v) {

		if (v == startBtn) {
			mediaPlayer.start();
			statusTV.setText("start play");

			startBtn.setEnabled(false);
			stopBtn.setEnabled(true);
		} else if (v == stopBtn) {
			mediaPlayer.pause();
			statusTV.setText("pause play");

			stopBtn.setEnabled(false);
			startBtn.setEnabled(true);
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {

		statusTV.setText("invoke onError");

		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			statusTV.setText("MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK"
					+ extra);
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			statusTV.setText("MEDIA_ERROR_SERVER_DIED" + extra);
			break;
		case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
			statusTV.setText("MEDIA_ERROR_UNSUPPORTED" + extra);
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			statusTV.setText("MEDIA_ERROR_UNKNOWN" + extra);
			break;
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {

		statusTV.setText("invoke onCompletion");
		stopBtn.setEnabled(false);
		startBtn.setEnabled(true);
	}

	@Override
	// 当后台缓冲数据发生变化时，会调用这个方法
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		bufferSB.setProgress(percent);
	}
	
	@Override
	//数据缓冲完成，可以播放
	public void onPrepared(MediaPlayer mp) {
		
		statusTV.setText("invoke onPrepared");
		startBtn.setEnabled(true);
	}

}
