package com.example.audiomanagertest;

import java.io.File;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button play;
	private Button pause;
	private Button replay;
	private Button stop;
	private EditText editPath;
	private MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editPath = (EditText) findViewById(R.id.edit_path);
		play = (Button) findViewById(R.id.play);
		pause = (Button) findViewById(R.id.pause);
		replay = (Button) findViewById(R.id.replay);
		stop = (Button) findViewById(R.id.stop);
		
		play.setOnClickListener(click);
		pause.setOnClickListener(click);
		replay.setOnClickListener(click);
		stop.setOnClickListener(click);
	}
	
	private View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()){
			case R.id.play:
				play();
				break;
			case R.id.pause:
				pause();
				break;
			case R.id.replay:
				replay();
				break;
			case R.id.stop:
				stop();
				break;
			default:
				break;	
			}
		}
	};
	
	protected void play(){
		String path = editPath.getText().toString().trim();
		File file = new File(path);
		if(file.exists() && file.length() > 0){
			try {
				mediaPlayer = new  MediaPlayer();
				mediaPlayer.setDataSource(path);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				
				mediaPlayer.prepareAsync();
				mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
					@Override
					public void onPrepared(MediaPlayer mp){
						mediaPlayer.start();
						Toast.makeText(MainActivity.this, "��ʼ����", Toast.LENGTH_LONG).show();
						play.setEnabled(false);
					}
				});
				
				mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
					@Override
					public void onCompletion(MediaPlayer mp){
						play.setEnabled(true);
					}
				});
				
				mediaPlayer.setOnErrorListener(new OnErrorListener(){
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra){
						replay();
						return false;
					}
				});
				
			} catch(Exception e){
				e.printStackTrace();
				Toast.makeText(this, "����ʧ��", Toast.LENGTH_LONG).show();
			}
		} else{
			Toast.makeText(this, "�ļ�������", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void pause(){
		if(pause.getText().toString().trim().equals("����")){
			pause.setText("��ͣ");
			mediaPlayer.start();
			Toast.makeText(this, "��������", Toast.LENGTH_LONG).show();
			return;
		}
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			pause.setText("����");
			Toast.makeText(this, "��ͣ����", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void replay(){
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.seekTo(0);
			Toast.makeText(this, "���²���", Toast.LENGTH_LONG).show();
			pause.setText("��ͣ");
			return;
		}
		play();
	}
	
	protected void stop(){
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			play.setEnabled(true);
			Toast.makeText(this, "ֹͣ����", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy(){
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}
}





















