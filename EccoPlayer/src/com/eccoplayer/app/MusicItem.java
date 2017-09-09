package com.eccoplayer.app;

import android.graphics.Bitmap;
import android.net.Uri;

public class MusicItem {
	String name;
	Uri musicUri;
	Uri albumUri;
	Bitmap theme;
	long duration;
	long playedTime;
	
	MusicItem(Uri musicUri, Uri albumUri, String name, long duration, long playedTime){
		this.name = name;
		this.albumUri = albumUri;
		this.duration = duration;
		this.musicUri = musicUri;
		this.playedTime = playedTime;
	}
	
	@Override
	public boolean equals(Object o){
		MusicItem another = (MusicItem) o;
		return another.musicUri.equals(this.musicUri);
	}
}
