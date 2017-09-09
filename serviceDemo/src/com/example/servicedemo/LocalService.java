package com.example.servicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalService extends Service {
	
	private static final String TAG = "LocalService";
	private NotificationManager mNM;
	
	public class LocalBinder extends Binder {
		LocalService getService(){
			return LocalService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent){
		//Log.i(TAG, "onBind");
		return mBinder;
	}
	
	private final IBinder mBinder = new LocalBinder();
	
	@SuppressWarnings("deprecation")
	private void showNotification(){
		CharSequence text = getText(R.string.local_service_started);
		
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LocalServiceController.class), 0);
		
		notification.setLatestEventInfo(this, getText(R.string.local_service_label), text, contentIntent);
		
		mNM.notify(R.string.local_service_started, notification);
	}
	
	@Override
	public void onCreate(){
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
	}
	
	@Override
	public void onDestroy(){
		//Log.i(TAG, "onDestroy");
		//super.onDestroy();
		mNM.cancel(R.string.local_service_started);
		
		Toast.makeText(this, R.string.local_service_started, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Replaced by onStartCommand since ADK2.0
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId){
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//int retVal = super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "Received startId = " + startId + " : " + intent);
		//return retVal;
		return START_STICKY;
	}
}


























