package com.example.volleytest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityModelDB {
	
	/**
	 * DATABASE name
	 */
	public static final String DB_NAME = "city_model";
	
	/**
	 * DATABASE version
	 */
	public static final int VERSION = 1;
	
	private static CityModelDB cityModelDB;
	
	private SQLiteDatabase db;
	
	private CityModelDB(Context context){
		CityModelOpenHelper dbHelper = new CityModelOpenHelper(context, 
				DB_NAME, null, VERSION + 1);
		
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Get CityModelDB instance
	 */
	public synchronized static  CityModelDB getInstance(Context context){
		if(cityModelDB == null){
			cityModelDB = new CityModelDB(context);
		}
		return cityModelDB;
	}
	
	/**
	 * Save CityModel to DB
	 */
	public void saveCityModel(CityModel citymodel){
		if(citymodel != null){
			ContentValues values = new ContentValues();
			values.put("city_name", citymodel.getCityName());
			values.put("city_code", citymodel.getCityCode());
			db.insert("CityModel", null, values);
		}
	}
	
	/**
	 * Load all Cities
	 */
	public List<CityModel> loadCityModel(){
		List<CityModel> list = new ArrayList<CityModel>();
		Cursor cursor = db.query("CityModel", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				CityModel city = new CityModel();
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				list.add(city);
			} while(cursor.moveToNext());
		}
		
		return list;
	}
	
	
	
}


















