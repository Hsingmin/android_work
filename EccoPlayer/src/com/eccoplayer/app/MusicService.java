package com.eccoplayer.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MusicService extends Service {
	
	public interface OnStateChangeListener {
		void onPlayProgressChange(MusicItem item);
		void onPlay(MusicItem item);
		void onPause(MusicItem item);
	}
	
	private final int MSG_PROGRESS_UPDATE = 0;
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case MSG_PROGRESS_UPDATE:
					mCurrentMusicItem.playedTime = mMusicPlayer.getCurrentPosition();
					mCurrentMusicItem.duration = mMusicPlayer.getDuration();
					for(OnStateChangeListener l : mListenerList){
						l.onPlayProgressChange(mCurrentMusicItem);;
					}
					
					updateMusicItem(mCurrentMusicItem);
					
					sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 1000);
					
				break;
			}
		}
	};
	
	public static final String ACTION_PLAY_MUSIC_PRE = "com.eccoplayer.app.playpre";
	public static final String ACTION_PLAY_MUSIC_NEXT = "com.eccoplayer.app.playnext";
	public static final String ACTION_PLAY_MUSIC_TOGGLE = "com.eccoplayer.app.playtoggle";
	public static final String ACTION_PLAY_MUSIC_UPDATE = "com.eccoplayer.app.playupdate";
	
	private List<OnStateChangeListener> mListenerList = new ArrayList<OnStateChangeListener>();
	private List<MusicItem> mPlayList;
	
	private MusicItem mCurrentMusicItem;
	private MediaPlayer mMusicPlayer;
	private ContentResolver mResolver;
	private boolean mPaused;
	
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			if(ACTION_PLAY_MUSIC_UPDATE.equals(action)){
				updateAppWidget(mCurrentMusicItem);
			}
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		if(intent != null){
			String action = intent.getAction();
			if(action != null){
				if(ACTION_PLAY_MUSIC_PRE.equals(action)){
					playPreInner();
				} else if(ACTION_PLAY_MUSIC_NEXT.equals(action)){
					playNextInner();
				} else if(ACTION_PLAY_MUSIC_TOGGLE.equals(action)){
					if(isPlayingInner()){
						pauseInner();
					} else{
						playInner();
					}
				}
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		mMusicPlayer = new MediaPlayer();
		mResolver = getContentResolver();
		mPlayList = new ArrayList<MusicItem>();
		mPaused = false;
		mMusicPlayer.setOnCompletionListener(mOnCompletionListener);
		
		IntentFilter commandFilter = new IntentFilter();
		commandFilter.addAction(ACTION_PLAY_MUSIC_UPDATE);
		registerReceiver(mIntentReceiver, commandFilter);
		
		initPlayList();
		
		if(mCurrentMusicItem != null){
			prepareToPlay(mCurrentMusicItem);
		}
		
		updateAppWidget(mCurrentMusicItem);
	}
	
	private void prepareToPlay(MusicItem item){
		try {
			mMusicPlayer.reset();
			mMusicPlayer.setDataSource(MusicService.this, item.musicUri);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		if(mMusicPlayer.isPlaying()){
			mMusicPlayer.stop();
		}
		mMusicPlayer.release();
		
		unregisterReceiver(mIntentReceiver);
		mHandler.removeMessages(MSG_PROGRESS_UPDATE);
		mListenerList.clear();
		for(MusicItem item : mPlayList){
			if(item.theme != null){
				item.theme.recycle();
			}
		}
		
		mPlayList.clear();
	}
	
	public class MusicServiceIBinder extends Binder {
		
		public void addPlayList(List<MusicItem> items){
			addPlayListInner(items);
		}
		
		public void addPlayList(MusicItem item){
			addPlayListInner(item, true);
		}
		
		public void play(){
			playInner();
		}
		
		public void playNext(){
			playNextInner();
		}
		
		public void playPre(){
			playPreInner();
		}
		
		public void pause(){
			pauseInner();
		}
		
		public void seekTo(int pos){
			seekToInner(pos);
		}
		
		public void registerOnStateChangeListener(OnStateChangeListener l){
			registerOnStateChangeListenerInner(l);
		}
		
		public void unregisterOnStateChangeListener(OnStateChangeListener l){
			unregisterOnStateChangeListenerInner(l);
		}
		
		public MusicItem getCurrentMusic(){
			return getCurrentMusicInner();
		}
		
		public boolean isPlaying(){
			return isPlayingInner();
		}
		
		public List<MusicItem> getPlayList(){
			return mPlayList;
		}
	}
	
	private final IBinder mBinder = new MusicServiceIBinder();
	
	@Override
	public IBinder onBind(Intent intent){
		return mBinder;
	}
	
	private void addPlayListInner(List<MusicItem> items){
		mResolver.delete(PlayListContentProvider.CONTENT_SONGS_URI, null, null);
		mPlayList.clear();
		
		for(MusicItem item : items){
			addPlayListInner(item, false);
		}
		
		mCurrentMusicItem = mPlayList.get(0);
		playInner();
	}
	
	private void addPlayListInner(MusicItem item, boolean needPlay){
		if(mPlayList.contains(item)){
			return;
		}
		
		mPlayList.add(0, item);
		
		insertMusicItemToContentProvider(item);
		
		if(needPlay){
			mCurrentMusicItem = mPlayList.get(0);
			playInner();
		}
	}
	
	private void playNextInner(){
		int currentIndex = mPlayList.indexOf(mCurrentMusicItem);
		if(currentIndex < mPlayList.size() - 1){
			mCurrentMusicItem = mPlayList.get(currentIndex);
			playMusicItem(mCurrentMusicItem, true);
		}
	}
	
	private void playInner(){
		if(mCurrentMusicItem == null && mPlayList.size() > 0){
			mCurrentMusicItem = mPlayList.get(0);
		}
		
		if(mPaused){
			playMusicItem(mCurrentMusicItem, false);
		} else{
			playMusicItem(mCurrentMusicItem, true);
		}
		
	}
	
	private void playPreInner(){
		int currentIndex = mPlayList.indexOf(mCurrentMusicItem);
		if(currentIndex - 1 > 0){
			mCurrentMusicItem = mPlayList.get(currentIndex);
			playMusicItem(mCurrentMusicItem, true);
		}
	}
	
	private void pauseInner(){
		mPaused = true;
		mMusicPlayer.pause();
		for(OnStateChangeListener l : mListenerList){
			l.onPause(mCurrentMusicItem);
		}
		
		mHandler.removeMessages(MSG_PROGRESS_UPDATE);
		updateAppWidget(mCurrentMusicItem);
	}
	
	private void seekToInner(int pos){
		mMusicPlayer.seekTo(pos);
	}
	
	private void registerOnStateChangeListenerInner(OnStateChangeListener l){
		mListenerList.add(l);
	}
	
	private void unregisterOnStateChangeListenerInner(OnStateChangeListener l){
		mListenerList.remove(l);
	}
	
	private MusicItem getCurrentMusicInner(){
		return mCurrentMusicItem;
	}
	
	private boolean isPlayingInner(){
		return mMusicPlayer.isPlaying();
	}
	
	private void initPlayList(){
		mPlayList.clear();
		
		Cursor cursor = mResolver.query(
				PlayListContentProvider.CONTENT_SONGS_URI, 
				null, 
				null, 
				null, 
				null);
		
		while(cursor.moveToNext()){
			String musicUri = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SONG_URI));
			String albumUri = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ALBUM_URI));
			String name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
			long playedTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.LAST_PLAY_TIME));
			long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.DURATION));
			
			MusicItem item = new MusicItem(Uri.parse(musicUri), Uri.parse(albumUri), name, duration, playedTime);
			mPlayList.add(item);
		}
		
		cursor.close();
		
		if(mPlayList.size() > 0){
			mCurrentMusicItem = mPlayList.get(0);
		}
	}
	
	private void playMusicItem(MusicItem item, boolean reload){
		if(item == null){
			return;
		}
		
		if(reload){
			prepareToPlay(item);
		}
		
		mMusicPlayer.start();
		seekToInner((int) item.playedTime);
		for(OnStateChangeListener l : mListenerList){
			l.onPlay(item);
		}
		mPaused = false;
		
		mHandler.removeMessages(MSG_PROGRESS_UPDATE);
		mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
		
		updateAppWidget(mCurrentMusicItem);
	}
	
	private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener(){
		
		@Override
		public void onCompletion(MediaPlayer mp){
			mCurrentMusicItem.playedTime = 0;
			updateMusicItem(mCurrentMusicItem);
			playNextInner();
		}
	};
	
	private void insertMusicItemToContentProvider(MusicItem item){
		
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.NAME, item.name);
		cv.put(DBHelper.DURATION, item.duration);
		cv.put(DBHelper.LAST_PLAY_TIME, item.playedTime);
		cv.put(DBHelper.SONG_URI, item.musicUri.toString());
		cv.put(DBHelper.ALBUM_URI, item.albumUri.toString());
		Uri uri = mResolver.insert(PlayListContentProvider.CONTENT_SONGS_URI, cv);
	}
	
	private void updateMusicItem(MusicItem item){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.DURATION, item.duration);
		cv.put(DBHelper.LAST_PLAY_TIME, item.playedTime);
		
		String strUri = item.musicUri.toString();
		mResolver.update(PlayListContentProvider.CONTENT_SONGS_URI, cv, DBHelper.SONG_URI + "=\"" + strUri + "\"", null);
	}
	
	private void updateAppWidget(MusicItem item){
		if(item != null){
			if(item.theme == null){
				ContentResolver res = getContentResolver();
				item.theme = Utils.createThemeFromUri(res, item.albumUri);
			}
			//EccoMusicAppWidget.performUpdates(MusicService.this, item.name, isPlayingInner(), item.theme);
		}
	}
}



















