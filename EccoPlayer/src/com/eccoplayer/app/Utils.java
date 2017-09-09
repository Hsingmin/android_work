package com.eccoplayer.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class Utils {
	
	static public String convertMSecondToTime(long time){
		SimpleDateFormat mSDF = new SimpleDateFormat("mm : ss");
		
		Date date = new Date(time);
		String times = mSDF.format(date);
		
		return times;
	}
	
	static public Bitmap createThemeFromUri(ContentResolver res, Uri albumUri){
		InputStream in = null;
		Bitmap bmp = null;
		try {
			in = res.openInputStream(albumUri);
			BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
			bmp = BitmapFactory.decodeStream(in, null, sBitmapOptions);
			in.close();
		} catch(FileNotFoundException e){
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return bmp;
	}
}

















