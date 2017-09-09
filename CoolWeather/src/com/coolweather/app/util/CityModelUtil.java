package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.coolweather.app.db.CityModelDB;
import com.coolweather.app.db.CityModelOpenHelper;
import com.coolweather.app.model.CityModel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityModelUtil {
	
	private static String response;
	private static CityModelDB cityModelDB;
	private static Context context;
	private static CityModelOpenHelper dbHelper;
	
	public static void load(String filename){
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		
		try{
			
			context = VolleyApplication.getContext();
			in = context.openFileInput(filename);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			cityModelDB = CityModelDB.getInstance(context);
		
			while((line = reader.readLine()) != null){
				content.append(line);
				String array[] = line.split("=");
				CityModel city = new CityModel();
				city.setCityName(new String(array[1].getBytes(), "utf-8"));
				city.setCityCode(new String(array[0].getBytes(), "utf-8"));
				//Log.d("TAG", "City Map : " + city.getCityCode() + " for " + city.getCityName());
				cityModelDB.saveCityModel(city);
			}
			
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch(IOException e){
					e.printStackTrace();
				}
			} 
		}
		
		try{
			response = new String((content.toString()).getBytes(), "utf-8");
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public static void JSONGenerate(){
		//TODO Create JSON Data
		//Log.d("TAG", response);
	}

	public static String queryCityName(String cityCode){
		
		String cityName = "";
		
		context = VolleyApplication.getContext();
		
		dbHelper = new CityModelOpenHelper(context, "city_model", null, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("CityModel", null, null, null, null, null, null);
		
		//Log.d("TAG", "queryCityCode started.");
		
		if(cursor.moveToFirst()){
			do{
				String retName = cursor.getString(cursor.getColumnIndex("city_name"));
				String retCode = cursor.getString(cursor.getColumnIndex("city_code"));
				if(cityCode.equals(retCode)){
					Log.d("TAG", "queryCityCode success : " + retCode + " corresponsed to " + retName);
					cityName = retName;
				} else{
					//Log.d("TAG", "queryCityCode : CityCode = " + retCode + " CityName = " + retName);
				}
			} while(cursor.moveToNext());
			
			//Log.d("TAG", "queryCityCode finished.");
			
		}
		cursor.close();
		return cityName;
	}
	
	public static String queryCityCode(String cityName){
		
		String cityCode = "";
		
		context = VolleyApplication.getContext();
		
		dbHelper = new CityModelOpenHelper(context, "city_model", null, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("CityModel", null, null, null, null, null, null);
		
		//Log.d("TAG", "queryCityCode started.");
		
		if(cursor.moveToFirst()){
			do{
				String retName = cursor.getString(cursor.getColumnIndex("city_name"));
				String retCode = cursor.getString(cursor.getColumnIndex("city_code"));
				if(cityName.equals(retName)){
					Log.d("TAG", "queryCityCode success : " + retCode + " corresponsed to " + retName);
					cityCode = retCode;
				} else{
					//Log.d("TAG", "queryCityCode : CityCode = " + retCode + " CityName = " + retName);
				}
			} while(cursor.moveToNext());
			
			//Log.d("TAG", "queryCityCode finished.");
			
		}
		cursor.close();
		return cityCode;
	}
	
}
















