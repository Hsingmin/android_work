package com.briup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MediaPlayerActivity extends Activity
{
	MediaPlayer mp;
	SeekBar bar;
	SeekBar vbar;
	TextView currentTime;
	TextView allTime;
	Handler handler = new Handler();
	File path;
	List<String> files;
	String currentfile;
	TextView filenameTv;
	TextView txtLrc;// ���
	String lrccon;// �������
	// ���̼߳������ȵĸı�
	private Runnable thread = new Runnable()
	{
		@Override
		public void run()
		{
			updateTextView();
			playNext(true);
			showLrc();
			handler.postDelayed(thread, 1000);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		filenameTv = (TextView) findViewById(R.id.filename);
		allTime = (TextView) findViewById(R.id.all);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			path = Environment.getExternalStorageDirectory();
		} else
		{
			Toast.makeText(this, "�����sdcard", Toast.LENGTH_SHORT).show();
			return;
		}
		// ��ȡ���������ļ�
		files = FileUtil.getFiles(path);

		mp = new MediaPlayer();
		// �տ�ʼ����ʱ��ʼ��
		play(this.getIntent().getStringExtra("file"));
		// ��ʾ���
		txtLrc = (TextView) findViewById(R.id.lrc);
		isExistLrc();
		// ��������
		bar = (SeekBar) findViewById(R.id.bar);
		bar.setMax(mp.getDuration());
		currentTime = (TextView) findViewById(R.id.current);
		currentTime.setText("0:00");
		new Thread(thread).start();
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if (fromUser)
				{
					mp.seekTo(progress);
				}
			}
		});
		// ��������
		vbar = (SeekBar) findViewById(R.id.vbar);
		final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		vbar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		vbar.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));
		vbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				am.setStreamVolume(AudioManager.STREAM_MUSIC, vbar.getProgress(), 0);
			}
		});
		final ImageView play = (ImageView) findViewById(R.id.play);
		final ImageView pre = (ImageView) findViewById(R.id.pre);
		final ImageView next = (ImageView) findViewById(R.id.next);
		final ImageView stop = (ImageView) findViewById(R.id.stop);

		ImageView volumn = (ImageView) findViewById(R.id.volumn);

		// ����
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!mp.isPlaying())
				{
					mp.start();
					play.setImageResource(R.drawable.pause);

				} else if (mp.isPlaying())
				{
					mp.pause();
					play.setImageResource(R.drawable.play);
				}
			}
		});
		// ֹͣ
		stop.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				mp.stop();
				play.setImageResource(R.drawable.play);
				try
				{
					mp.prepare();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		// ��һ��
		pre.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int pos = files.indexOf(currentfile);
				if (pos - 1 >= 0)
				{
					play(files.get(pos - 1));
					mp.start();
					play.setImageResource(R.drawable.pause);
				} else
					Toast.makeText(MediaPlayerActivity.this, "û�и���", Toast.LENGTH_SHORT).show();
			}
		});
		// ��һ��
		next.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// ������һ�׸�
				playNextMusic();
				play.setImageResource(R.drawable.pause);
			}
		});

	}

	// ����
	private void play(String filename)
	{
		mp.reset();
		try
		{
			// Log.i("MediaPlayerActivity","�ļ��ڴ棺"+files.size()+"");
			// Log.i("MediaPlayerActivity", "��һ�׸裺"+filename);
			mp.setDataSource(path + "/" + filename);
			currentfile = filename;
			filenameTv.setText(currentfile);
			mp.prepare();
			int m = mp.getDuration() / 1000;
			int s = m / 60;
			int add = m % 60;
			allTime.setText(s + ":" + add);
			lrccon = PlayLrc.read(path, currentfile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onDestroy()
	{
		mp.release();
		mp = null;
		super.onDestroy();
	}

	/**
	 * ���½���
	 */
	private void updateTextView()
	{
		if (mp != null)
		{
			int m = mp.getCurrentPosition() / 1000;
			int s = m / 60;
			int add = m % 60;
			if (add < 10)
				currentTime.setText(s + ":0" + add);
			else
				currentTime.setText(s + ":" + add);
			bar.setProgress(mp.getCurrentPosition());
		}
	}

	/**
	 * ������һ��
	 * 
	 * @param flag
	 *            true:��ʾ�Զ�������һ��
	 */
	private void playNext(boolean flag)
	{
		if (!flag)
		{
			playNextMusic();
		} else
		{
			if (currentTime.getText().equals(allTime.getText()))
			{
				playNextMusic();
			}
		}
	}

	/**
	 * ����һ�׸�
	 */
	private void playNextMusic()
	{
		int pos = files.indexOf(currentfile);
		if (pos + 1 < files.size())
		{
			play(files.get(pos + 1));
			mp.start();
		} else
		{
			play(files.get(0));
			mp.start();
		}
		isExistLrc();
	}

	/**
	 * �жϸ���Ƿ����
	 */
	private void isExistLrc()
	{
		if (PlayLrc.read(path, currentfile).length() == 0)
			txtLrc.setText("��ʲ�����");
	}

	/**
	 * ��ʾ���
	 */
	private void showLrc()
	{
		BufferedReader br = new BufferedReader(new StringReader(lrccon));
		String temp;
		try
		{
			while ((temp = br.readLine()) != null)
			{
				if (mp != null && temp.substring(temp.indexOf("]") + 1).length() > 0)
				{
					Log.i("test", "��ʾ����߳̽�����");
					String time = PlayLrc.formatMS(mp.getCurrentPosition());

					if (temp.contains(time))
					{
						Log.i("test", "��ʽ�����");
						txtLrc.setText(temp.substring(temp.indexOf("]") + 1));
						br.close();
					}
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
