package com.example.volleytest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CityModelOpenHelper extends SQLiteOpenHelper {
	
	/**
	 * CityModel Table Create
	 */
	public static final String CREATE_CITY_MODEL = "create table CityModel ("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text)";
	
	public CityModelOpenHelper(Context context, String name, CursorFactory factory,
			int version){
		super(context, name, factory, version);
		Log.d("TAG", "CityModelOpenHelper Create CityModelDB Success.");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_CITY_MODEL);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("drop table if exists CityModel");
		onCreate(db);
	}
	
}















